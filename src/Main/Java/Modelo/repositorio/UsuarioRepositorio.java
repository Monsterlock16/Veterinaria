package Modelo.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JOptionPane;

import Modelo.entidades.Usuario;

public class UsuarioRepositorio {

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

        return null;
    }

    public boolean registrarNuevoUsuarioEnBD(String nombres, String apellidos, String fechaNacimiento, 
                                            String documento, String correo, String usuario, 
                                            String contrasena, String rol, String especialidad) {
        Connection cn = null;
        try {
            cn = ConexionBD.obtener();
            cn.setAutoCommit(false);

            // 1. Guardar en PERSONA con la fecha capturada
            String sqlPersona = "INSERT INTO persona (nombres, apellidos, fecha_nacimiento, documento, telefono, correo) "
                              + "VALUES (?, ?, ?, ?, '3000000000', ?)";
            PreparedStatement psPersona = cn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            psPersona.setString(1, nombres);
            psPersona.setString(2, apellidos);
            psPersona.setString(3, (fechaNacimiento == null || fechaNacimiento.trim().isEmpty()) ? "1990-01-01" : fechaNacimiento.trim());
            psPersona.setString(4, documento);
            psPersona.setString(5, correo);
            psPersona.executeUpdate();

            ResultSet rsP = psPersona.getGeneratedKeys();
            int idPersonaGenerado = 0;
            if (rsP.next()) {
                idPersonaGenerado = rsP.getInt(1);
            } else {
                cn.rollback();
                return false;
            }

            // 2. Guardar en USUARIO
            String sqlUsuario = "INSERT INTO usuario (usuario, contrasena, rol, PERSONA_idPERSONA) "
                              + "VALUES (?, ?, ?, ?)";
            PreparedStatement psUsuario = cn.prepareStatement(sqlUsuario);
            psUsuario.setString(1, usuario);
            psUsuario.setString(2, contrasena);
            psUsuario.setString(3, rol.toUpperCase());
            psUsuario.setInt(4, idPersonaGenerado);
            psUsuario.executeUpdate();

            // 3. Guardar en PROPIETARIO o VETERINARIO
            if ("PROPIETARIO".equalsIgnoreCase(rol)) {
                String sqlProp = "INSERT INTO propietario (PERSONA_idPERSONA, fecha_registro, hora_registro) "
                               + "VALUES (?, ?, ?)";
                PreparedStatement psProp = cn.prepareStatement(sqlProp);
                psProp.setInt(1, idPersonaGenerado);
                psProp.setObject(2, LocalDate.now());
                psProp.setObject(3, LocalTime.now());
                psProp.executeUpdate();

            } else if ("VETERINARIO".equalsIgnoreCase(rol)) {
                String esp = (especialidad == null || especialidad.trim().isEmpty()) ? "General" : especialidad.trim();
                String sqlVet = "INSERT INTO veterinario (PERSONA_idPERSONA, especialidad, numero_licencia) "
                              + "VALUES (?, ?, ?)";
                PreparedStatement psVet = cn.prepareStatement(sqlVet);
                psVet.setInt(1, idPersonaGenerado);
                psVet.setString(2, esp);
                psVet.setString(3, "LIC-" + idPersonaGenerado);
                psVet.executeUpdate();
            }

            cn.commit();
            return true;

        } catch (Exception ex) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException ignored) {}
            }
            JOptionPane.showMessageDialog(null, "Error al guardar en la base de datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            if (cn != null) {
                try { cn.setAutoCommit(true); cn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}