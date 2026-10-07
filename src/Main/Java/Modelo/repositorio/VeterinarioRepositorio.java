package Modelo.repositorio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import Modelo.entidades.Veterinario;

/** Veterinarios guardados en MySQL (tablas PERSONA + VETERINARIO). */
public class VeterinarioRepositorio {

    private static final String SELECT_BASE =
            "SELECT v.idVETERINARIO, v.especialidad, v.numero_licencia, "
          + "p.idPERSONA, p.nombres, p.apellidos, p.fecha_nacimiento, p.documento, p.telefono, p.correo "
          + "FROM VETERINARIO v JOIN PERSONA p ON p.idPERSONA = v.PERSONA_idPERSONA ";

    public void agregar(Veterinario v) {
        if (v == null || buscarPorDocumento(v.getDocumento()) != null
                || buscarPorTarjetaProf(v.getNumeroLicencia()) != null) {
            return;
        }
        JdbcUtil.enTransaccion(cn -> {
            int idPersona = JdbcUtil.insertarPersona(cn, v);
            String sql = "INSERT INTO VETERINARIO (PERSONA_idPERSONA, especialidad, numero_licencia) VALUES (?, ?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idPersona);
                ps.setString(2, v.getEspecialidad());
                ps.setString(3, String.valueOf(v.getNumeroLicencia()));
                ps.executeUpdate();
                v.setIdpersona(idPersona);
                v.setIdVeterinario(JdbcUtil.claveGenerada(ps));
            }
        });
    }

    public List<Veterinario> obtenerVeterinarios() {
        return JdbcUtil.consultar(SELECT_BASE + "ORDER BY v.idVETERINARIO", ps -> { },
                VeterinarioRepositorio::mapear);
    }

    public Veterinario buscarPorId(int id) {
        return JdbcUtil.consultarUno(SELECT_BASE + "WHERE v.idVETERINARIO = ?",
                ps -> ps.setInt(1, id), VeterinarioRepositorio::mapear);
    }

    public Veterinario buscarPorTarjetaProf(int tp) {
        return buscarPorTarjetaProf(String.valueOf(tp));
    }

    public Veterinario buscarPorTarjetaProf(String tp) {
        if (tp == null) {
            return null;
        }
        return JdbcUtil.consultarUno(SELECT_BASE + "WHERE v.numero_licencia = ?",
                ps -> ps.setString(1, tp), VeterinarioRepositorio::mapear);
    }

    public Veterinario buscarPorDocumento(String doc) {
        if (doc == null) {
            return null;
        }
        return JdbcUtil.consultarUno(SELECT_BASE + "WHERE p.documento = ?",
                ps -> ps.setString(1, doc), VeterinarioRepositorio::mapear);
    }

    private static Veterinario mapear(ResultSet rs) throws SQLException {
        String licencia = rs.getString("numero_licencia");
        int numeroLicencia;
        try {
            numeroLicencia = Integer.parseInt(licencia.trim());
        } catch (NumberFormatException e) {
            // En la BD es VARCHAR(30) y en la clase Veterinario es int
            throw new RepositorioException("La licencia '" + licencia
                    + "' no es numerica y la clase Veterinario la guarda como int.", e);
        }
        return new Veterinario(
                rs.getInt("idVETERINARIO"),
                rs.getString("especialidad"),
                numeroLicencia,
                rs.getInt("idPERSONA"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getObject("fecha_nacimiento", LocalDate.class),
                rs.getString("documento"),
                rs.getString("telefono"),
                rs.getString("correo"));
    }
}
