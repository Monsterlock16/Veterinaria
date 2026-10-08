package Controlador;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;

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

        buscarBoton("Mis animales").addActionListener(e -> abrirMisAnimales());
        buscarBoton("Solicitar cita").addActionListener(e -> abrirSolicitudCita());
        buscarBoton("Mis citas").addActionListener(e -> abrirMisCitas());
        buscarBoton("Mi historial médico").addActionListener(e -> abrirMiHistorial());
    }

    private JButton buscarBoton(String texto) {
        JButton boton = buscarBoton(vista.getContentPane(), texto);
        if (boton == null) {
            throw new IllegalStateException("No se encontró el botón del propietario: " + texto);
        }
        return boton;
    }

    private JButton buscarBoton(Container contenedor, String texto) {
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof JButton boton && boton.getText().contains(texto)) {
                return boton;
            }
            if (componente instanceof Container hijo) {
                JButton boton = buscarBoton(hijo, texto);
                if (boton != null) {
                    return boton;
                }
            }
        }
        return null;
    }

    private void abrirMisAnimales() {
        new MisAnimales(datos).setVisible(true);
    }

    private void abrirSolicitudCita() {
        new SolicitarCitaPresenter(new SolicitarCita(datos)).mostrar();
    }

    private void abrirMisCitas() {
        new MisCitas(datos).setVisible(true);
    }

    private void abrirMiHistorial() {
        new MiHistorialMedico(datos).setVisible(true);
    }
}