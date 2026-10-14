package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class propietario extends JFrame {

    private static final Color COLOR_PRIMARIO = TemaVeterinaria.PRIMARIO;
    private static final Color COLOR_FONDO = TemaVeterinaria.FONDO;
    private static final Color COLOR_TARJETA = TemaVeterinaria.INPUT;
    private static final Color COLOR_TEXTO = TemaVeterinaria.TEXTO;

    private final JButton btnMisAnimales;
    private final JButton btnSolicitarCita;
    private final JButton btnMisCitas;
    private final JButton btnMiHistorial;
    private final JButton btnCerrarSesion;

    public propietario(String usuario) {
        TemaVeterinaria.aplicarIcono(this);
        setTitle("Sistema Veterinario - Módulo principal");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Banner Superior
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuario, SwingConstants.LEFT);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBienvenida.setForeground(COLOR_PRIMARIO);

        JLabel lblSubtitulo = new JLabel("Módulo de Atención al Cliente y Mascotas", SwingConstants.LEFT);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(100, 110, 120));

        JPanel textosHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        textosHeader.setOpaque(false);
        textosHeader.add(lblBienvenida);
        textosHeader.add(lblSubtitulo);

        btnCerrarSesion = Navegacion.crearBotonCerrarSesion();
        panelHeader.add(textosHeader, BorderLayout.CENTER);
        panelHeader.add(btnCerrarSesion, BorderLayout.EAST);

        // Menú Grid de Opciones
        btnMisAnimales = crearBotonOpcion("Mis animales");
        btnSolicitarCita = crearBotonOpcion("Solicitar cita");
        btnMisCitas = crearBotonOpcion("Mis citas");
        btnMiHistorial = crearBotonOpcion("Mi historial médico");

        JPanel menu = new JPanel(new GridLayout(2, 2, 20, 20));
        menu.setOpaque(false);
        menu.add(btnMisAnimales);
        menu.add(btnSolicitarCita);
        menu.add(btnMisCitas);
        menu.add(btnMiHistorial);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(menu, BorderLayout.CENTER);

        add(panelPrincipal);
        TemaVeterinaria.aplicarEstiloVentana(this);
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public JButton getBtnMisAnimales() {
        return btnMisAnimales;
    }

    public JButton getBtnSolicitarCita() {
        return btnSolicitarCita;
    }

    public JButton getBtnMisCitas() {
        return btnMisCitas;
    }

    public JButton getBtnMiHistorial() {
        return btnMiHistorial;
    }

    private JButton crearBotonOpcion(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(COLOR_TARJETA);
        btn.setForeground(COLOR_TEXTO);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            propietario vista = new propietario("John Doe");
            new Controlador.PropietarioPresenter(vista, "John Doe");
            vista.setVisible(true);
        });
    }
}