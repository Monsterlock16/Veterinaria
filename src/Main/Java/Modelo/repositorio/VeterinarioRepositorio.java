package Modelo.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Modelo.entidades.Veterinario;

public class VeterinarioRepositorio {

    /**
     * Busca un veterinario por su ID único en la BD
     */
    public Veterinario buscarPorId(int id) {
        String sql = "SELECT v.idVETERINARIO, v.especialidad, v.numero_licencia, "
                   + "p.idPERSONA, p.nombres, p.apellidos, p.fecha_nacimiento, p.documento, p.telefono, p.correo, u.usuario "
                   + "FROM veterinario v "
                   + "JOIN persona p ON p.idPERSONA = v.PERSONA_idPERSONA "
                   + "LEFT JOIN usuario u ON u.PERSONA_idPERSONA = p.idPERSONA "
                   + "WHERE v.idVETERINARIO = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVeterinario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar veterinario por ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Busca un veterinario por su nombre de usuario de inicio de sesión
     */
    public Veterinario buscarPorUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) return null;

        String sql = "SELECT v.idVETERINARIO, v.especialidad, v.numero_licencia, "
                   + "p.idPERSONA, p.nombres, p.apellidos, p.fecha_nacimiento, p.documento, p.telefono, p.correo, u.usuario "
                   + "FROM veterinario v "
                   + "JOIN persona p ON p.idPERSONA = v.PERSONA_idPERSONA "
                   + "JOIN usuario u ON u.PERSONA_idPERSONA = p.idPERSONA "
                   + "WHERE u.usuario = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVeterinario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar veterinario por usuario: " + e.getMessage());
        }

        return null;
    }

    private Veterinario mapearVeterinario(ResultSet rs) throws SQLException {
        return new Veterinario(
            rs.getInt("idVETERINARIO"),
            rs.getString("especialidad"),
            rs.getString("numero_licencia"),
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