package Vista.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Controlador.Gstconsultas;

public class Navegacion extends JFrame {

    private static final Color COLOR_TEXTO = new Color(45, 55, 65);
    
    private JPanel panelContenedor;
    private CardLayout cardLayout;
    private String usuarioActual;

    public Navegacion(String usuarioVet) {
        this.usuarioActual = usuarioVet;
        
        setTitle("Gestión Veterinaria - Sistema Principal (" + usuarioVet + ")");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        TemaVeterinaria.aplicarIcono(this);

        // Configurar el layout de tarjetas para alternar paneles sin crear nuevas ventanas
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setBackground(TemaVeterinaria.FONDO);

        // 1. Agregar los módulos/paneles de la aplicación
        // El panel de consultas que ahora funciona como JPanel se integra perfectamente aquí:
        panelContenedor.add(new Gstconsultas(usuarioActual), "CONSULTAS");
        
        // Puedes agregar aquí más paneles en el futuro conforme los vayas migrando:
        // panelContenedor.add(new GstanimalPanel(), "ANIMALES");

        add(panelContenedor, BorderLayout.CENTER);

        // Mostrar el módulo inicial por defecto
        cardLayout.show(panelContenedor, "CONSULTAS");
        
        TemaVeterinaria.aplicarEstiloVentana(this);
    }

    // Método público para cambiar de panel/módulo de forma limpia desde cualquier botón
    public void mostrarModulo(String nombreModulo) {
        cardLayout.show(panelContenedor, nombreModulo);
    }

    public static JButton crearBotonCerrarSesion(JFrame ventanaActual) {
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
        
        boton.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    ventanaActual,
                    "¿Deseas cerrar tu sesión?",
                    "Confirmar cierre de sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                login ventanaLogin = new login();
                ventanaLogin.setVisible(true);
                ventanaActual.dispose();
            }
        });
        return boton;
    }

    // Método auxiliar opcional por si necesitas incrustar un botón de retorno rápido en un panel hijo
    public static void agregarBotonVolver(JPanel panelActual, Navegacion ventanaPrincipal, String nombreModuloDestino) {
        JButton botonVolver = new JButton("←  Volver al menú");
        botonVolver.setFont(new Font("Segoe UI", Font.BOLD, 12));
        TemaVeterinaria.aplicarBoton(botonVolver, TemaVeterinaria.INPUT, TemaVeterinaria.PRIMARIO);
        
        botonVolver.addActionListener(e -> {
            ventanaPrincipal.mostrarModulo(nombreModuloDestino);
        });

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(12, 16, 14, 16));
        pie.add(botonVolver);
        panelActual.add(pie, BorderLayout.SOUTH);
    }
}