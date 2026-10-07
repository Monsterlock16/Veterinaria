package Modelo.repositorio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import Modelo.entidades.Propietario;

/** Propietarios guardados en MySQL (tablas PERSONA + PROPIETARIO). */
public class PropietarioRepositorio {

    private static final String SELECT_BASE =
            "SELECT pr.idPROPIETARIO, pr.fecha_registro, pr.hora_registro, "
          + "p.idPERSONA, p.nombres, p.apellidos, p.fecha_nacimiento, p.documento, p.telefono, p.correo "
          + "FROM PROPIETARIO pr JOIN PERSONA p ON p.idPERSONA = pr.PERSONA_idPERSONA ";

    public void guardar(Propietario p) {
        if (p == null || buscarPorDocumento(p.getDocumento()) != null) {
            return;
        }
        JdbcUtil.enTransaccion(cn -> {
            int idPersona = JdbcUtil.insertarPersona(cn, p);
            String sql = "INSERT INTO PROPIETARIO (PERSONA_idPERSONA, fecha_registro, hora_registro) VALUES (?, ?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idPersona);
                ps.setObject(2, p.getfechaRegistro());
                ps.setObject(3, p.gethoraRegistro());
                ps.executeUpdate();
                p.setIdpersona(idPersona);
                p.setIdPropietario(JdbcUtil.claveGenerada(ps));
            }
        });
    }

    public List<Propietario> obtenerTodos() {
        return JdbcUtil.consultar(SELECT_BASE + "ORDER BY pr.idPROPIETARIO", ps -> { },
                PropietarioRepositorio::mapear);
    }

    public Propietario buscarPorDocumento(String doc) {
        if (doc == null) {
            return null;
        }
        return JdbcUtil.consultarUno(SELECT_BASE + "WHERE p.documento = ?",
                ps -> ps.setString(1, doc), PropietarioRepositorio::mapear);
    }

    public Propietario buscarPorId(int id) {
        return JdbcUtil.consultarUno(SELECT_BASE + "WHERE pr.idPROPIETARIO = ?",
                ps -> ps.setInt(1, id), PropietarioRepositorio::mapear);
    }

    public boolean eliminar(String doc) {
        Propietario p = buscarPorDocumento(doc);
        if (p == null) {
            return false;
        }
        JdbcUtil.enTransaccion(cn -> {
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM PROPIETARIO WHERE idPROPIETARIO = ?")) {
                ps.setInt(1, p.getIdPropietario());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM PERSONA WHERE idPERSONA = ?")) {
                ps.setInt(1, p.getIdpersona());
                ps.executeUpdate();
            }
        });
        return true;
    }

    private static Propietario mapear(ResultSet rs) throws SQLException {
        return new Propietario(
                rs.getInt("idPROPIETARIO"),
                rs.getObject("fecha_registro", LocalDate.class),
                rs.getObject("hora_registro", LocalTime.class),
                rs.getInt("idPERSONA"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getObject("fecha_nacimiento", LocalDate.class),
                rs.getString("documento"),
                rs.getString("telefono"),
                rs.getString("correo"));
    }
}
