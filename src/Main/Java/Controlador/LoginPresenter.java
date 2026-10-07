package Controlador;

import Modelo.entidades.Usuario;
import Modelo.repositorio.UsuarioRepositorio;
import Vista.gui.login;
import Vista.gui.propietario;
import Vista.gui.veterinario;

import javax.swing.JOptionPane;

public class LoginPresenter {
    private login vista;
    private UsuarioRepositorio usuarioRepo;

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

        // Consulta en la tabla USUARIO vía Repositorio
        Usuario usuarioLogueado = usuarioRepo.autenticar(username, pass);

        if (usuarioLogueado != null) {
            String rol = usuarioLogueado.getRol();
            vista.mostrarMensaje("Bienvenido al sistema (" + rol + ").", "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);

            if ("VETERINARIO".equalsIgnoreCase(rol)) {
                veterinario vVista = new veterinario(usuarioLogueado.getUsuario());
                new VeterinarioPresenter(vVista, usuarioLogueado.getUsuario());
                vVista.setVisible(true);
            } else if ("PROPIETARIO".equalsIgnoreCase(rol)) {
                propietario pVista = new propietario(usuarioLogueado.getUsuario());
                pVista.setVisible(true);
            }

            vista.dispose();
        } else {
            vista.mostrarMensaje("Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
            vista.limpiarContrasena();
        }
    }
}