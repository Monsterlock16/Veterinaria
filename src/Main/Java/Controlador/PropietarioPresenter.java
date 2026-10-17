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
import Vista.gui.DatosPropietario;
import Vista.gui.MiHistorialMedico;
import Vista.gui.MisAnimales;
import Vista.gui.MisCitas;
import Vista.gui.SolicitarCita;
import Vista.gui.propietario;

public class PropietarioPresenter {
    private final propietario vista;
    private final DatosPropietario datos;

    public PropietarioPresenter(propietario vista, String usuario) {
        this.vista = vista;
        this.datos = new DatosPropietario(usuario);

        vista.getBtnMisAnimales().addActionListener(e -> ejecutarApertura("Mis animales", this::abrirMisAnimales));
        vista.getBtnSolicitarCita().addActionListener(e -> ejecutarApertura("Solicitar cita", this::abrirSolicitudCita));
        vista.getBtnMisCitas().addActionListener(e -> ejecutarApertura("Mis citas", this::abrirMisCitas));
        vista.getBtnMiHistorial().addActionListener(e -> ejecutarApertura("Mi historial médico", this::abrirMiHistorial));
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

    private void abrirMisAnimales() {
        MisAnimales ventana = new MisAnimales(datos);
        agregarBotonVolver(ventana, vista);
        ventana.setVisible(true);
        vista.dispose();
    }

  private void abrirSolicitudCita() {
        SolicitarCita ventana = new SolicitarCita(datos);
        new SolicitarCitaPresenter(ventana).mostrar();
        vista.dispose();
    }

    private void abrirMisCitas() {
        MisCitas ventana = new MisCitas(datos);
        agregarBotonVolver(ventana, vista);
        ventana.setVisible(true);
        vista.dispose();
    }

    private void abrirMiHistorial() {
        MiHistorialMedico ventana = new MiHistorialMedico(datos);
        agregarBotonVolver(ventana, vista);
        ventana.setVisible(true);
        vista.dispose();
    }

    private void agregarBotonVolver(JFrame ventanaActual, JFrame menuAnterior) {
        if (!(ventanaActual.getLayout() instanceof BorderLayout)) {
            ventanaActual.setLayout(new BorderLayout());
        }

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
        panelSur.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panelSur.add(botonVolver);
        
        ventanaActual.add(panelSur, BorderLayout.SOUTH);
        ventanaActual.pack();
        ventanaActual.setLocationRelativeTo(null);
    }
}