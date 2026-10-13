package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class Navegacion {
    private static final Color COLOR_TEXTO = new Color(45, 55, 65);

    private Navegacion() {
    }

    public static JButton crearBotonCerrarSesion() {
        JButton boton = new JButton("Cerrar sesión");
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(new Color(160, 55, 55));
        boton.setBackground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 205, 205)),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
        ));
        return boton;
    }

    public static void agregarAccionCerrarSesion(JButton boton, JFrame menu) {
        boton.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    menu,
                    "¿Deseas cerrar tu sesión?",
                    "Confirmar cierre de sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                login ventanaLogin = new login();
                ventanaLogin.setVisible(true);
                for (Window ventana : Window.getWindows()) {
                    if (ventana != ventanaLogin && ventana.isDisplayable()) {
                        ventana.dispose();
                    }
                }
            }
        });
    }

    public static void agregarBotonVolver(JFrame ventana, JFrame menuAnterior) {
        Container contenidoActual = ventana.getContentPane();
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(new Color(245, 247, 250));
        contenido.add(contenidoActual, BorderLayout.CENTER);

        JButton botonVolver = new JButton("←  Volver al menú anterior");
        botonVolver.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botonVolver.setForeground(COLOR_TEXTO);
        botonVolver.setBackground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonVolver.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 225, 230)),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
        ));
        botonVolver.addActionListener(e -> {
            ventana.dispose();
            menuAnterior.setVisible(true);
            menuAnterior.toFront();
            menuAnterior.requestFocus();
        });

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(12, 16, 14, 16));
        pie.add(botonVolver);
        contenido.add(pie, BorderLayout.SOUTH);
        ventana.setContentPane(contenido);
        ventana.setSize(ventana.getWidth(), ventana.getHeight() + 58);
        ventana.setLocationRelativeTo(menuAnterior);
    }

}
