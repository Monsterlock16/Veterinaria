package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
        setTitle("Pet Attention - Acceso al sistema");
        setUndecorated(true); // pantalla completa, sin bordes (se cierra con el boton Salir)
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Fondo fondo = new Fondo();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Boton Salir arriba a la derecha
        JButton btnSalir = new JButton("✕  Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        barra.setOpaque(false);
        barra.add(btnSalir);
        fondo.add(barra, BorderLayout.NORTH);

        // Tarjeta central con logo, campos y botones
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(30, 45, 35, 45));
        tarjeta.setPreferredSize(new Dimension(440, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 10, 0);
        JPanel logo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logo.setOpaque(false);
        logo.add(new LogoVeterinaria(110));
        tarjeta.add(logo, gbc);

        JLabel titulo = new JLabel("Pet Attention", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(TemaVeterinaria.PRIMARIO);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 4, 0);
        tarjeta.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Ingresa tus datos para continuar", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(TemaVeterinaria.TEXTO_SECUNDARIO);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 25, 0);
        tarjeta.add(subtitulo, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 4, 0);
        tarjeta.add(etiqueta("Usuario"), gbc);
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
        dialogo.setSize(600, 760);
        dialogo.setResizable(false);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new GridBagLayout());

        JTextField txtNombres = new JTextField();
        JTextField txtApellidos = new JTextField();
        SelectorFecha selFecha = new SelectorFecha(LocalDate.now().getYear() - 100, LocalDate.now().getYear(), true);
        JTextField txtDoc = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JComboBox<String> cbxRol = new JComboBox<>(new String[]{"PROPIETARIO", "VETERINARIO"});

        // Especialidades guardadas en la base de datos (solo se muestran si el rol es VETERINARIO)
        JComboBox<String> cbxEspecialidad = new JComboBox<>();
        cbxEspecialidad.addItem("Seleccione una especialidad");
        for (String esp : presenter.listarEspecialidades()) {
            cbxEspecialidad.addItem(esp);
        }
        JLabel lblEspecialidad = new JLabel("Especialidad:");

        int fila = 0;
        agregarFila(dialogo, fila++, new JLabel("Nombres:"), txtNombres);
        agregarFila(dialogo, fila++, new JLabel("Apellidos:"), txtApellidos);
        agregarFila(dialogo, fila++, new JLabel("Fecha de nacimiento:"), selFecha);
        agregarFila(dialogo, fila++, new JLabel("Documento:"), txtDoc);
        agregarFila(dialogo, fila++, new JLabel("Teléfono:"), txtTelefono);
        agregarFila(dialogo, fila++, new JLabel("Correo:"), txtCorreo);
        agregarFila(dialogo, fila++, new JLabel("Usuario:"), txtUser);
        agregarFila(dialogo, fila++, new JLabel("Contraseña:"), txtPass);
        agregarFila(dialogo, fila++, new JLabel("Rol:"), cbxRol);
        agregarFila(dialogo, fila++, lblEspecialidad, cbxEspecialidad);
        lblEspecialidad.setVisible(false);
        cbxEspecialidad.setVisible(false);

        cbxRol.addActionListener(e -> {
            boolean esVet = "VETERINARIO".equals(cbxRol.getSelectedItem());
            lblEspecialidad.setVisible(esVet);
            cbxEspecialidad.setVisible(esVet);
            dialogo.revalidate();
            dialogo.repaint();
        });

        JButton btnGuardar = new JButton("Guardar Usuario");
        btnGuardar.setBackground(TemaVeterinaria.PRIMARIO);
        btnGuardar.setForeground(Color.WHITE);
        JButton btnCancelar = new JButton("Cancelar");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botones.setOpaque(false);
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 10, 20);
        gbc.weighty = 1; gbc.anchor = GridBagConstraints.NORTH;
        dialogo.add(botones, gbc);

        btnCancelar.addActionListener(e -> dialogo.dispose());
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
