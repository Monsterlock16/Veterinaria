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

public class veterinario extends JFrame {

    private final JButton btnAnimales;
    private final JButton btnCitas;
    private final JButton btnConsultas;
    private final JButton btnHistorial;
    private final JButton btnCerrarSesion;

    private static final Color COLOR_PRIMARIO = TemaVeterinaria.PRIMARIO;
    private static final Color COLOR_FONDO = TemaVeterinaria.FONDO;
    private static final Color COLOR_TARJETA = TemaVeterinaria.INPUT;
    private static final Color COLOR_TEXTO = TemaVeterinaria.TEXTO;

    public veterinario(String usuario) {
        TemaVeterinaria.aplicarIcono(this);
        setTitle("Sistema Veterinario - Módulo principal");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblBienvenida = new JLabel("Bienvenido, Dr(a). " + usuario, SwingConstants.LEFT);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBienvenida.setForeground(COLOR_PRIMARIO);

        JLabel lblSubtitulo = new JLabel("Módulo de Gestión Médica y Clínica", SwingConstants.LEFT);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(100, 110, 120));

        JPanel textosHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        textosHeader.setOpaque(false);
        textosHeader.add(lblBienvenida);
        textosHeader.add(lblSubtitulo);

        btnCerrarSesion = Navegacion.crearBotonCerrarSesion();
        panelHeader.add(textosHeader, BorderLayout.CENTER);
        panelHeader.add(btnCerrarSesion, BorderLayout.EAST);

        btnAnimales = crearBotonOpcion("Gestionar animales");
        btnCitas = crearBotonOpcion("Gestionar citas");
        btnConsultas = crearBotonOpcion("Gestionar consultas");
        btnHistorial = crearBotonOpcion("Historial médico");

        JPanel menu = new JPanel(new GridLayout(2, 2, 20, 20));
        menu.setOpaque(false);
        menu.add(btnAnimales);
        menu.add(btnCitas);
        menu.add(btnConsultas);
        menu.add(btnHistorial);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(menu, BorderLayout.CENTER);

        add(panelPrincipal);
        TemaVeterinaria.aplicarEstiloVentana(this);
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

    // Getters para el Presentador
    public JButton getBtnAnimales() {
        return btnAnimales;
    }

    public JButton getBtnCitas() {
        return btnCitas;
    }

    public JButton getBtnConsultas() {
        return btnConsultas;
    }

    public JButton getBtnHistorial() {
        return btnHistorial;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            veterinario vista = new veterinario("Jane Doe");
            new Controlador.veterinarioPresenter(vista, "Jane Doe");
            vista.setVisible(true);
        });
    }
}