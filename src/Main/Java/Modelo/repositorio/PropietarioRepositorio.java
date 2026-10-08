package Modelo.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.entidades.Propietario;

public class PropietarioRepositorio {

    /**
     * Busca un propietario por su ID único en la BD
     */
    public Propietario buscarPorId(int id) {
        String sql = "SELECT pr.idPROPIETARIO, pr.fecha_registro, pr.hora_registro, "
                   + "p.idPERSONA, p.nombres, p.apellidos, p.fecha_nacimiento, p.documento, p.telefono, p.correo, u.usuario "
                   + "FROM propietario pr "
                   + "JOIN persona p ON p.idPERSONA = pr.PERSONA_idPERSONA "
                   + "LEFT JOIN usuario u ON u.PERSONA_idPERSONA = p.idPERSONA "
                   + "WHERE pr.idPROPIETARIO = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPropietario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar propietario por ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca un propietario por su número de documento
     */
    public Propietario buscarPorDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) return null;

        String sql = "SELECT pr.idPROPIETARIO, pr.fecha_registro, pr.hora_registro, "
                   + "p.idPERSONA, p.nombres, p.apellidos, p.fecha_nacimiento, p.documento, p.telefono, p.correo, u.usuario "
                   + "FROM propietario pr "
                   + "JOIN persona p ON p.idPERSONA = pr.PERSONA_idPERSONA "
                   + "LEFT JOIN usuario u ON u.PERSONA_idPERSONA = p.idPERSONA "
                   + "WHERE p.documento = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, documento.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPropietario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar propietario por documento: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca un propietario por su nombre de usuario de inicio de sesión
     */
    public Propietario buscarPorUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) return null;

        String sql = "SELECT pr.idPROPIETARIO, pr.fecha_registro, pr.hora_registro, "
                   + "p.idPERSONA, p.nombres, p.apellidos, p.fecha_nacimiento, p.documento, p.telefono, p.correo, u.usuario "
                   + "FROM propietario pr "
                   + "JOIN persona p ON p.idPERSONA = pr.PERSONA_idPERSONA "
                   + "JOIN usuario u ON u.PERSONA_idPERSONA = p.idPERSONA "
                   + "WHERE u.usuario = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPropietario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar propietario por usuario: " + e.getMessage());
        }

        return null;
    }

    private Propietario mapearPropietario(ResultSet rs) throws SQLException {
        return new Propietario(
            rs.getInt("idPROPIETARIO"),
            rs.getDate("fecha_registro") != null ? rs.getDate("fecha_registro").toLocalDate() : null,
            rs.getTime("hora_registro") != null ? rs.getTime("hora_registro").toLocalTime() : null,
            rs.getInt("idPERSONA"),
            rs.getString("nombres"),
            rs.getString("apellidos"),
            rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toLocalDate() : null,
            rs.getString("documento"),
            rs.getString("telefono"),
            rs.getString("correo"),
            rs.getString("usuario")
        );
    }
}