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
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        panelCentral.add(new JLabel("Usuario:"));
        panelCentral.add(txtUsuario);
        panelCentral.add(new JLabel("Contraseña:"));
        panelCentral.add(txtContrasena);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnIngresar.setBackground(new Color(0, 102, 153));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegistrarse.setBackground(new Color(220, 225, 230));
        btnRegistrarse.setForeground(Color.BLACK);
        btnRegistrarse.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrarse);

        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        this.presenter = new LoginPresenter(this);

        btnRegistrarse.addActionListener(e -> abrirVentanaRegistro());
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