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

    // Colores del tema propietario
    private static final Color COLOR_PRIMARIO = new Color(0, 150, 136); // Verde esmeralda
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_TEXTO = new Color(30, 40, 50);

    public propietario(String usuario) {
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

        panelHeader.add(lblBienvenida, BorderLayout.NORTH);
        panelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        // Menú Grid de Opciones
        JButton btnmisAnimales = crearBotonOpcion("🐶 Mis animales");
        JButton btnsolicitarCita = crearBotonOpcion("➕ Solicitar cita");
        JButton btnmisCitas = crearBotonOpcion("📆 Mis citas");
        JButton btnmiHistorial = crearBotonOpcion("📄 Mi historial médico");

        JPanel menu = new JPanel(new GridLayout(2, 2, 20, 20));
        menu.setOpaque(false);
        menu.add(btnmisAnimales);
        menu.add(btnsolicitarCita);
        menu.add(btnmisCitas);
        menu.add(btnmiHistorial);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(menu, BorderLayout.CENTER);

        add(panelPrincipal);
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
        SwingUtilities.invokeLater(() -> new propietario("John Doe").setVisible(true));
    }
}