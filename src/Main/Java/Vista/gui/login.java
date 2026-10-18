package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 4, 0);
        tarjeta.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Ingresa tus datos para continuar", SwingConstants.CENTER);
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
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 14, 0);
        tarjeta.add(txtUsuario, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 4, 0);
        tarjeta.add(etiqueta("Contraseña"), gbc);
        estilizarCampo(txtContrasena);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 24, 0);
        tarjeta.add(txtContrasena, gbc);

        btnIngresar.setBackground(TemaVeterinaria.PRIMARIO);
        btnIngresar.setForeground(Color.WHITE);
        estilizarBoton(btnIngresar, 15);
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 10, 0);
        tarjeta.add(btnIngresar, gbc);

        btnRegistrarse.setBackground(TemaVeterinaria.INPUT);
        btnRegistrarse.setForeground(TemaVeterinaria.PRIMARIO);
        estilizarBoton(btnRegistrarse, 14);
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 0, 0);
        tarjeta.add(btnRegistrarse, gbc);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(tarjeta);
        fondo.add(centro, BorderLayout.CENTER);

        getRootPane().setDefaultButton(btnIngresar); // Enter inicia sesion
        this.presenter = new LoginPresenter(this);
        btnRegistrarse.addActionListener(e -> abrirVentanaRegistro());
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TemaVeterinaria.TEXTO);
        return l;
    }

    private void estilizarCampo(JTextField campo) {
        TemaVeterinaria.aplicarCampo(campo);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(300, 40));
    }

    private void estilizarBoton(JButton boton, int tamanioFuente) {
        TemaVeterinaria.aplicarBoton(boton, boton.getBackground(), boton.getForeground());
        boton.setFont(new Font("Segoe UI", Font.BOLD, tamanioFuente));
        boton.setPreferredSize(new Dimension(300, 44));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    // ---------- Ventana de registro (grande, fija, con fecha por desplegables y especialidades) ----------

    private void abrirVentanaRegistro() {
        JDialog dialogo = new JDialog(this, "Registro de Nuevo Usuario", true);
        TemaVeterinaria.aplicarIcono(dialogo);
        dialogo.setSize(420, 540);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridLayout(11, 2, 8, 8));

        JTextField txtNombres = new JTextField();
        JTextField txtApellidos = new JTextField();
        SelectorFecha selFecha = new SelectorFecha(LocalDate.now().getYear() - 100, LocalDate.now().getYear(), true);
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
            LocalDate nacimiento = selFecha.getFecha();
            if (nacimiento == null) {
                JOptionPane.showMessageDialog(dialogo, "Seleccione la fecha de nacimiento completa (día, mes y año).",
                        "Fecha incompleta", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nacimiento.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(dialogo, "La fecha de nacimiento no puede ser futura.",
                        "Fecha no válida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean esVet = "VETERINARIO".equals(cbxRol.getSelectedItem());
            if (esVet && cbxEspecialidad.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialogo, "Seleccione la especialidad del veterinario.",
                        "Especialidad requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean exito = presenter.procesarRegistroUsuario(
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                nacimiento.toString(),
                txtDoc.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtUser.getText().trim(),
                new String(txtPass.getPassword()).trim(),
                cbxRol.getSelectedItem().toString(),
                esVet ? cbxEspecialidad.getSelectedItem().toString() : ""
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

    private void agregarFila(Container c, int fila, JLabel etiqueta, JComponent campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 8;
        gbc.gridx = 0; gbc.weightx = 0.35; gbc.insets = new Insets(10, 25, 10, 10);
        c.add(etiqueta, gbc);
        gbc.gridx = 1; gbc.weightx = 0.65; gbc.insets = new Insets(10, 10, 10, 25);
        c.add(campo, gbc);
    }

    // ---------- Fondo con degradado y patitas ----------

    private static class Fondo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, TemaVeterinaria.PRIMARIO.darker(), w, h, TemaVeterinaria.PRIMARIO.brighter()));
            g2.fillRect(0, 0, w, h);

            // Circulos suaves de adorno
            g2.setColor(new Color(255, 255, 255, 25));
            g2.fillOval(-120, -120, 420, 420);
            g2.fillOval(w - 300, h - 280, 520, 520);

            // Patitas repartidas por el fondo
            g2.setColor(new Color(255, 255, 255, 30));
            int paso = 190;
            for (int fila = 0, y = 40; y < h; y += paso, fila++) {
                for (int x = (fila % 2 == 0 ? 30 : 125); x < w; x += paso) {
                    LogoVeterinaria.dibujarPata(g2, x, y, 56);
                }
            }
            g2.dispose();
        }
    }

    // ---------- Tarjeta blanca redondeada ----------

    private static class Tarjeta extends JPanel {
        Tarjeta() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRoundRect(6, 8, getWidth() - 12, getHeight() - 12, 30, 30);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 30, 30);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new login().setVisible(true));
    }
}
