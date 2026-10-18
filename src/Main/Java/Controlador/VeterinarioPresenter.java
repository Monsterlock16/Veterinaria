package Controlador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Modelo.repositorio.RepositorioException;
import Vista.gui.Gstcitas_V;
import Vista.gui.TemaVeterinaria;
import Vista.gui.gstanimalesV;
import Vista.gui.historialmed_V;
import Vista.gui.veterinario;

public class VeterinarioPresenter {
    private final veterinario vista;
    private final String usuario;

    public VeterinarioPresenter(veterinario vista, String usuario) {
        this.vista = vista;
        this.usuario = usuario;
        inicializarEventos();
    }

    public VeterinarioPresenter(veterinario vista) {
        this(vista, "veterinario");
    }

    private void inicializarEventos() {
        if (this.vista == null) return;

        if (this.vista.getBtnAnimales() != null) {
            this.vista.getBtnAnimales().addActionListener(e -> ejecutarApertura("Gestión de animales", () -> {
                gstanimalesV ventanaAnimales = new gstanimalesV();
                agregarBotonVolver(ventanaAnimales, vista);
                ventanaAnimales.setVisible(true);
                vista.dispose();
            }));
        }

        if (this.vista.getBtnCitas() != null) {
            this.vista.getBtnCitas().addActionListener(e -> ejecutarApertura("Gestión de citas", () -> {
                Gstcitas_V ventanaCitas = new Gstcitas_V();
                new GstcitasPresenter(ventanaCitas, this.usuario);
                agregarBotonVolver(ventanaCitas, vista);
                ventanaCitas.setVisible(true);
                vista.dispose();
            }));
        }

        if (this.vista.getBtnConsultas() != null) {
            this.vista.getBtnConsultas().addActionListener(e -> ejecutarApertura("Gestión de consultas", () -> {
                // Envolvemos el panel Gstconsultas dentro de un JFrame para mostrarlo correctamente
                JFrame ventanaContenedora = new JFrame("Gestión de Consultas");
                ventanaContenedora.setSize(900, 600);
                ventanaContenedora.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                Gstconsultas panelConsultas = new Gstconsultas(this.usuario);
                ventanaContenedora.add(panelConsultas, BorderLayout.CENTER);

                agregarBotonVolver(ventanaContenedora, vista);
                TemaVeterinaria.aplicarEstiloVentana(ventanaContenedora);
                ventanaContenedora.setVisible(true);
                vista.dispose();
            }));
        }

        if (this.vista.getBtnHistorial() != null) {
            this.vista.getBtnHistorial().addActionListener(e -> ejecutarApertura("Historial médico", () -> {
                historialmed_V ventanaHistorial = new historialmed_V();
                agregarBotonVolver(ventanaHistorial, vista);
                ventanaHistorial.setVisible(true);
                vista.dispose();
            }));
        }
    }

    private void agregarBotonVolver(JFrame ventanaActual, JFrame menuAnterior) {
        JButton botonVolver = new JButton("←  Volver al menú");
        botonVolver.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botonVolver.setBackground(new Color(240, 242, 245));
        botonVolver.setForeground(new Color(45, 55, 65));
        botonVolver.setFocusPainted(false);
        botonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        botonVolver.addActionListener(e -> {
            ventanaActual.dispose();
            menuAnterior.setVisible(true);
            menuAnterior.toFront();
        });

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSur.setOpaque(false);
        panelSur.add(botonVolver);
        
        ventanaActual.add(panelSur, BorderLayout.SOUTH);
    }

    private void ejecutarApertura(String modulo, Runnable accion) {
        try {
            accion.run();
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo abrir " + modulo + " porque falló la conexión a la base de datos:\n"
                            + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public String getUsuario() {
        return usuario;
    }
}