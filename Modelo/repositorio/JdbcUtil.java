package repositorio;

import entidades.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Utilidades JDBC compartidas por los repositorios (solo visible en este paquete). */
final class JdbcUtil {

    @FunctionalInterface
    interface Parametros {
        void asignar(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    interface Fila<T> {
        T leer(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    interface Transaccion {
        void ejecutar(Connection cn) throws SQLException;
    }

    private JdbcUtil() {
    }

    /** Ejecuta un SELECT y convierte cada fila con el mapeador dado. */
    static <T> List<T> consultar(String sql, Parametros parametros, Fila<T> fila) {
        List<T> resultado = new ArrayList<>();
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            parametros.asignar(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(fila.leer(rs));
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al consultar datos: " + e.getMessage(), e);
        }
        return resultado;
    }

    /** Primer resultado de un SELECT, o null si no hay filas. */
    static <T> T consultarUno(String sql, Parametros parametros, Fila<T> fila) {
        List<T> lista = consultar(sql, parametros, fila);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /** Ejecuta varias sentencias como una sola operacion: o se guardan todas o ninguna. */
    static void enTransaccion(Transaccion t) {
        try (Connection cn = ConexionBD.obtener()) {
            cn.setAutoCommit(false);
            try {
                t.ejecutar(cn);
                cn.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    cn.rollback();
                } catch (SQLException ignorada) {
                    // se informa el error original
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error en la transacción: " + e.getMessage(), e);
        }
    }

    static int insertarPersona(Connection cn, Persona p) throws SQLException {
        String sql = "INSERT INTO PERSONA (nombres, apellidos, fecha_nacimiento, documento, telefono, correo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setObject(3, p.getFechaNacimiento());
            ps.setString(4, p.getDocumento());
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getCorreo());
            ps.executeUpdate();
            return claveGenerada(ps);
        }
    }

    static int claveGenerada(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("La base de datos no devolvio el id generado.");
        }
    }
}
