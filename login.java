package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Controlador.LoginPresenter;

public class login extends JFrame {

    private final JTextField txtUsuario = new JTextField(15);
    private final JPasswordField txtContrasena = new JPasswordField(15);
    private final JButton btnIngresar = new JButton("Ingresar");
    private final JButton btnRegistrarse = new JButton("Registrarse");
    private LoginPresenter presenter;

    public login() {
        setTitle("Acceso al Sistema - Veterinaria");
        setSize(480, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(242, 247, 248));

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(242, 247, 248));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(26, 32, 24, 32));

        JPanel panelEncabezado = new JPanel(new GridLayout(2, 1, 0, 5));
        panelEncabezado.setOpaque(false);
        JLabel titulo = new JLabel("Clínica Veterinaria");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 119, 128));
        JLabel subtitulo = new JLabel("Ingresa tus datos para continuar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(100, 110, 120));
        panelEncabezado.add(titulo);
        panelEncabezado.add(subtitulo);

        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 12, 14));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 232)),
                BorderFactory.createEmptyBorder(20, 18, 20, 18)
        ));
        JLabel etiquetaUsuario = new JLabel("Usuario");
        JLabel etiquetaContrasena = new JLabel("Contraseña");
        etiquetaUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaUsuario.setForeground(new Color(45, 55, 65));
        etiquetaContrasena.setForeground(new Color(45, 55, 65));
        estilizarCampo(txtUsuario);
        estilizarCampo(txtContrasena);
        panelCentral.add(etiquetaUsuario);
        panelCentral.add(txtUsuario);
        panelCentral.add(etiquetaContrasena);
        panelCentral.add(txtContrasena);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        btnIngresar.setBackground(new Color(0, 119, 128));
        btnIngresar.setForeground(Color.WHITE);
        estilizarBoton(btnIngresar, 13);

        btnRegistrarse.setBackground(Color.WHITE);
        btnRegistrarse.setForeground(new Color(0, 119, 128));
        estilizarBoton(btnRegistrarse, 12);

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrarse);

        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal, BorderLayout.CENTER);

        this.presenter = new LoginPresenter(this);

        btnRegistrarse.addActionListener(e -> abrirVentanaRegistro());
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 225)),
                BorderFactory.createEmptyBorder(7, 9, 7, 9)
        ));
    }

    private void estilizarBoton(JButton boton, int tamanioFuente) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, tamanioFuente));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 225, 230)),
                BorderFactory.createEmptyBorder(9, 16, 9, 16)
        ));
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getContrasena() {
        return new String(txtContrasena.getPassword()).trim();
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    public void limpiarContrasena() {
        txtContrasena.setText("");
    }

    private void abrirVentanaRegistro() {
        JDialog dialogo = new JDialog(this, "Registro de Nuevo Usuario", true);
        dialogo.setSize(400, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridLayout(10, 2, 8, 8));

        JTextField txtNombres = new JTextField();
        JTextField txtApellidos = new JTextField();
        JTextField txtFechaNac = new JTextField("2000-01-01");
        JTextField txtDoc = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JComboBox<String> cbxRol = new JComboBox<>(new String[]{"PROPIETARIO", "VETERINARIO"});
        JTextField txtEspecialidad = new JTextField();

        dialogo.add(new JLabel(" Nombres:")); dialogo.add(txtNombres);
        dialogo.add(new JLabel(" Apellidos:")); dialogo.add(txtApellidos);
        dialogo.add(new JLabel(" Fecha Nac. (YYYY-MM-DD):")); dialogo.add(txtFechaNac);
        dialogo.add(new JLabel(" Documento:")); dialogo.add(txtDoc);
        dialogo.add(new JLabel(" Correo:")); dialogo.add(txtCorreo);
        dialogo.add(new JLabel(" Usuario:")); dialogo.add(txtUser);
        dialogo.add(new JLabel(" Contraseña:")); dialogo.add(txtPass);
        dialogo.add(new JLabel(" Rol:")); dialogo.add(cbxRol);
        dialogo.add(new JLabel(" Especialidad (Solo Vet):")); dialogo.add(txtEspecialidad);

        JButton btnGuardar = new JButton("Guardar Usuario");
        btnGuardar.setBackground(new Color(0, 102, 153));
        btnGuardar.setForeground(Color.WHITE);

        btnGuardar.addActionListener(ev -> {
            boolean exito = presenter.procesarRegistroUsuario(
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtFechaNac.getText().trim(),
                txtDoc.getText().trim(),
                txtCorreo.getText().trim(),
                txtUser.getText().trim(),
                new String(txtPass.getPassword()).trim(),
                cbxRol.getSelectedItem().toString(),
                txtEspecialidad.getText().trim()
            );

            if (exito) {
                dialogo.dispose();
            }
        });

        dialogo.add(new JLabel());
        dialogo.add(btnGuardar);
        dialogo.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new login().setVisible(true));
    }
}