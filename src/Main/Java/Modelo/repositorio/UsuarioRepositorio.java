package Modelo.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepositorio {

    /**
     * Consulta si las credenciales existen en MySQL.
     * Retorna "VETERINARIO", "PROPIETARIO" o null si no existe.
     */
    public String autenticar(String usuario, String contrasena) {
        // 1. Validar si es Veterinario
        String sqlVet = "SELECT v.idVETERINARIO FROM VETERINARIO v "
                      + "JOIN PERSONA p ON p.idPERSONA = v.PERSONA_idPERSONA "
                      + "WHERE (p.documento = ? OR p.correo = ?) AND p.documento = ?";
        
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sqlVet)) {
            ps.setString(1, usuario);
            ps.setString(2, usuario);
            ps.setString(3, contrasena);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "VETERINARIO";
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al consultar veterinario en BD: " + e.getMessage(), e);
        }

        // 2. Validar si es Propietario
        String sqlProp = "SELECT pr.idPROPIETARIO FROM PROPIETARIO pr "
                       + "JOIN PERSONA p ON p.idPERSONA = pr.PERSONA_idPERSONA "
                       + "WHERE (p.documento = ? OR p.correo = ?) AND p.documento = ?";
        
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sqlProp)) {
            ps.setString(1, usuario);
            ps.setString(2, usuario);
            ps.setString(3, contrasena);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "PROPIETARIO";
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al consultar propietario en BD: " + e.getMessage(), e);
        }

        return null; // Credenciales no encontradas
    }
}