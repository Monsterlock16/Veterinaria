package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
        TemaVeterinaria.aplicarIcono(this);
        setTitle("Acceso al Sistema - Veterinaria");
        setSize(480, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(TemaVeterinaria.FONDO);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(TemaVeterinaria.FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(26, 32, 24, 32));

        JPanel panelEncabezado = new JPanel(new GridLayout(2, 1, 0, 5));
        panelEncabezado.setOpaque(false);
        JLabel titulo = new JLabel("Clínica Veterinaria");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TemaVeterinaria.PRIMARIO);
        JLabel subtitulo = new JLabel("Ingresa tus datos para continuar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(TemaVeterinaria.TEXTO_SECUNDARIO);
        panelEncabezado.add(titulo);
        panelEncabezado.add(subtitulo);

        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 12, 14));
        panelCentral.setBackground(TemaVeterinaria.INPUT);
        panelCentral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaVeterinaria.BORDE),
                BorderFactory.createEmptyBorder(20, 18, 20, 18)
        ));
        JLabel etiquetaUsuario = new JLabel("Usuario");
        JLabel etiquetaContrasena = new JLabel("Contraseña");
        etiquetaUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaUsuario.setForeground(TemaVeterinaria.TEXTO);
        etiquetaContrasena.setForeground(TemaVeterinaria.TEXTO);
        estilizarCampo(txtUsuario);
        estilizarCampo(txtContrasena);
        panelCentral.add(etiquetaUsuario);
        panelCentral.add(txtUsuario);
        panelCentral.add(etiquetaContrasena);
        panelCentral.add(txtContrasena);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        btnIngresar.setBackground(TemaVeterinaria.PRIMARIO);
        btnIngresar.setForeground(java.awt.Color.WHITE);
        estilizarBoton(btnIngresar, 13);

        btnRegistrarse.setBackground(TemaVeterinaria.INPUT);
        btnRegistrarse.setForeground(TemaVeterinaria.PRIMARIO);
        estilizarBoton(btnRegistrarse, 12);

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrarse);

        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal, BorderLayout.CENTER);

        TemaVeterinaria.aplicarEstiloVentana(this);
        this.presenter = new LoginPresenter(this);

        btnRegistrarse.addActionListener(e -> abrirVentanaRegistro());
    }

    private void estilizarCampo(JTextField campo) {
        TemaVeterinaria.aplicarCampo(campo);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void estilizarBoton(JButton boton, int tamanioFuente) {
        TemaVeterinaria.aplicarBoton(boton, boton.getBackground(), boton.getForeground());
        boton.setFont(new Font("Segoe UI", Font.BOLD, tamanioFuente));
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
        TemaVeterinaria.aplicarIcono(dialogo);
        dialogo.setSize(420, 540);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridLayout(11, 2, 8, 8));

        JTextField txtNombres = new JTextField();
        JTextField txtApellidos = new JTextField();
        JTextField txtFechaNac = new JTextField("2000-01-01");
        JTextField txtDoc = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JComboBox<String> cbxRol = new JComboBox<>(new String[]{"PROPIETARIO", "VETERINARIO"});
        JTextField txtEspecialidad = new JTextField();

        dialogo.add(new JLabel(" Nombres:")); dialogo.add(txtNombres);
        dialogo.add(new JLabel(" Apellidos:")); dialogo.add(txtApellidos);
        dialogo.add(new JLabel(" Fecha Nac. (YYYY-MM-DD):")); dialogo.add(txtFechaNac);
        dialogo.add(new JLabel(" Documento:")); dialogo.add(txtDoc);
        dialogo.add(new JLabel(" Teléfono:")); dialogo.add(txtTelefono);
        dialogo.add(new JLabel(" Correo:")); dialogo.add(txtCorreo);
        dialogo.add(new JLabel(" Usuario:")); dialogo.add(txtUser);
        dialogo.add(new JLabel(" Contraseña:")); dialogo.add(txtPass);
        dialogo.add(new JLabel(" Rol:")); dialogo.add(cbxRol);
        dialogo.add(new JLabel(" Especialidad (Solo Vet):")); dialogo.add(txtEspecialidad);

        JButton btnGuardar = new JButton("Guardar Usuario");
        btnGuardar.setBackground(TemaVeterinaria.PRIMARIO);
        btnGuardar.setForeground(java.awt.Color.WHITE);

        btnGuardar.addActionListener(ev -> {
            boolean exito = presenter.procesarRegistroUsuario(
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtFechaNac.getText().trim(),
                txtDoc.getText().trim(),
                txtTelefono.getText().trim(),
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
        TemaVeterinaria.aplicarEstiloVentana(dialogo);
        dialogo.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new login().setVisible(true));
    }
}