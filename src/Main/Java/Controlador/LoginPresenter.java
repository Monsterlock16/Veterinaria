package Controlador;

import javax.swing.JOptionPane;

import Modelo.entidades.Usuario;
import Modelo.repositorio.RepositorioException;
import Modelo.repositorio.UsuarioRepositorio;
import Vista.gui.login;
import Vista.gui.propietario;
import Vista.gui.veterinario;

public class LoginPresenter {
    private final login vista;
    private final UsuarioRepositorio usuarioRepo;

    public LoginPresenter(login vista) {
        this.vista = vista;
        this.usuarioRepo = new UsuarioRepositorio();
        this.vista.getBtnIngresar().addActionListener(e -> ejecutarLogin());
    }

    private void ejecutarLogin() {
        String username = vista.getUsuario();
        String pass = vista.getContrasena();

        if (username.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Escribe el usuario y la contraseña.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogueado;
        try {
            usuarioLogueado = usuarioRepo.autenticar(username, pass);
        } catch (RepositorioException e) {
            vista.mostrarMensaje(
                    "No se pudo validar el acceso: " + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (usuarioLogueado != null) {
            String rol = usuarioLogueado.getRol();
            if (!"VETERINARIO".equalsIgnoreCase(rol) && !"PROPIETARIO".equalsIgnoreCase(rol)) {
                vista.mostrarMensaje(
                        "El usuario no tiene un rol válido para acceder al sistema.",
                        "Acceso no disponible",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            vista.mostrarMensaje("Bienvenido al sistema (" + rol + ").", "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);

            // Instanciación corregida usando la 'v' minúscula para coincidir con veterinarioPresenter.java
            if ("VETERINARIO".equalsIgnoreCase(rol)) {
                veterinario vVista = new veterinario(usuarioLogueado.getUsuario());
                @SuppressWarnings("unused")
                veterinarioPresenter vPresenter = new veterinarioPresenter(vVista, usuarioLogueado.getUsuario());
                vVista.setVisible(true);
            } else if ("PROPIETARIO".equalsIgnoreCase(rol)) {
                propietario pVista = new propietario(usuarioLogueado.getUsuario());
                @SuppressWarnings("unused")
                PropietarioPresenter pPresenter = new PropietarioPresenter(pVista, usuarioLogueado.getUsuario());
                pVista.setVisible(true);
            }

            vista.dispose();
        } else {
            vista.mostrarMensaje("Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
            vista.limpiarContrasena();
        }
    }

    public boolean procesarRegistroUsuario(String nombres, String apellidos, String fechaNacimiento,
                                           String documento, String correo, String usuario, 
                                           String contrasena, String rol, String especialidad) {
        if (nombres.isEmpty() || apellidos.isEmpty() || documento.isEmpty() || 
            correo.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            vista.mostrarMensaje("Todos los campos obligatorios deben estar diligenciados.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        boolean exito = usuarioRepo.registrarNuevoUsuarioEnBD(
            nombres, apellidos, fechaNacimiento, documento, correo, usuario, contrasena, rol, especialidad
        );

        if (exito) {
            vista.mostrarMensaje("Usuario registrado con éxito como " + rol + ". Ya puede iniciar sesión.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            vista.mostrarMensaje("No se pudo completar el registro en la base de datos.", "Error de registro", JOptionPane.ERROR_MESSAGE);
        }

        return exito;
    }
}