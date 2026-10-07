package Controlador;

import javax.swing.JOptionPane;

import Modelo.repositorio.UsuarioRepositorio;
import Vista.gui.login;
import Vista.gui.propietario;
import Vista.gui.veterinario;

public class LoginPresenter {
    private login vista;
    private UsuarioRepositorio usuarioRepo;

    public LoginPresenter(login vista) {
        this.vista = vista;
        this.usuarioRepo = new UsuarioRepositorio();
        this.vista.getBtnIngresar().addActionListener(e -> ejecutarLogin());
    }

    private void ejecutarLogin() {
        String usuario = vista.getUsuario();
        String contrasena = vista.getContrasena();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            vista.mostrarMensaje("Escribe el usuario y la contraseña.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación directa contra la base de datos MySQL
        String rol = usuarioRepo.autenticar(usuario, contrasena);

        if ("VETERINARIO".equalsIgnoreCase(rol)) {
            vista.mostrarMensaje("Bienvenido al sistema (Veterinario).", "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
            veterinario vVista = new veterinario(usuario);
            new VeterinarioPresenter(vVista, usuario);
            vVista.setVisible(true);
            vista.dispose();
        } else if ("PROPIETARIO".equalsIgnoreCase(rol)) {
            vista.mostrarMensaje("Bienvenido al sistema (Propietario).", "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
            propietario pVista = new propietario(usuario);
            pVista.setVisible(true);
            vista.dispose();
        } else {
            vista.mostrarMensaje("Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
            vista.limpiarContrasena();
        }
    }
}