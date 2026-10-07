package Vista.gui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public login() {
        
        setTitle("Sistema Veterinario - Iniciar sesión");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panel.add(txtContrasena);

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnSalir = new JButton("Salir");
        panel.add(btnIngresar);
        panel.add(btnSalir);

        add(panel);

        btnIngresar.addActionListener(e -> iniciarSesion());
        btnSalir.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Escribe el usuario y la contraseña.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Credenciales temporales para probar el login
        if (usuario.equals("carlos") && contrasena.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Bienvenido al sistema.");
            veterinario ventanaVeterinario = new veterinario(usuario);
            ventanaVeterinario.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this, "Usuario o contraseña incorrectos.",
                "Error de acceso", JOptionPane.ERROR_MESSAGE
            );
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new login().setVisible(true));
    }
}