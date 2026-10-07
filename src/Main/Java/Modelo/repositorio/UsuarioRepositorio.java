package Modelo.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Modelo.entidades.Usuario;

public class UsuarioRepositorio {

    /**
     * Valida las credenciales contra la tabla USUARIO.
     * Retorna el objeto Usuario con su rol si es correcto, o null si falla.
     */
    public Usuario autenticar(String nombreUsuario, String contrasena) {
        String sql = "SELECT idUSUARIO, usuario, contrasena, rol, PERSONA_idPERSONA "
                   + "FROM USUARIO WHERE usuario = ? AND contrasena = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("idUSUARIO"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("rol"),
                        (Integer) rs.getObject("PERSONA_idPERSONA")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RepositorioException("Error al autenticar usuario en BD: " + e.getMessage(), e);
        }

        return null; // Credenciales inválidas
    }
}