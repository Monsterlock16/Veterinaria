package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JButton btnSalir;

    // Colores del tema
    private static final Color COLOR_PRIMARIO = new Color(0, 122, 116);   // Verde médico
    private static final Color COLOR_HOVER = new Color(0, 95, 90);
    private static final Color COLOR_FONDO = new Color(245, 247, 250);     // Gris claro limpio
    private static final Color COLOR_TEXTO = new Color(40, 50, 60);

    public login() {
        setTitle("Sistema Veterinario - Iniciar sesión");
        setSize(380, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelContenido = new JPanel(new BorderLayout(0, 15));
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Encabezado
        JLabel lblTitulo = new JLabel("¡Bienvenido!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        JLabel lblSubtitulo = new JLabel("Ingresa tus credenciales para acceder", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(120, 130, 140));

        JPanel panelHeader = new JPanel(new BorderLayout(0, 5));
        panelHeader.setOpaque(false);
        panelHeader.add(lblTitulo, BorderLayout.NORTH);
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(COLOR_TEXTO);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContrasena.setForeground(COLOR_TEXTO);

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(lblUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(txtUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(lblContrasena, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(txtContrasena, gbc);

        // Botones
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setBackground(COLOR_PRIMARIO);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSalir.setBackground(new Color(230, 235, 240));
        btnSalir.setForeground(COLOR_TEXTO);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel panelBotones = new JPanel(new BorderLayout(0, 8));
        panelBotones.setOpaque(false);
        panelBotones.add(btnIngresar, BorderLayout.NORTH);
        panelBotones.add(btnSalir, BorderLayout.SOUTH);

        panelContenido.add(panelHeader, BorderLayout.NORTH);
        panelContenido.add(panelForm, BorderLayout.CENTER);
        panelContenido.add(panelBotones, BorderLayout.SOUTH);

        add(panelContenido);

        btnSalir.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(btnIngresar);
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getContrasena() {
        return new String(txtContrasena.getPassword());
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    public void limpiarContrasena() {
        txtContrasena.setText("");
        txtContrasena.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new login().setVisible(true));
    }
}