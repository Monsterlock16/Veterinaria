package Controlador;

import javax.swing.JOptionPane;

import Modelo.repositorio.RepositorioException;
import Vista.gui.DatosPropietario;
import Vista.gui.MiHistorialMedico;
import Vista.gui.MisAnimales;
import Vista.gui.MisCitas;
import Vista.gui.SolicitarCita;
import Vista.gui.Navegacion;
import Vista.gui.propietario;

public class PropietarioPresenter {
    private final propietario vista;
    private final DatosPropietario datos;

    public PropietarioPresenter(propietario vista, String usuario) {
        this.vista = vista;
        this.datos = new DatosPropietario(usuario);

        Navegacion.agregarAccionCerrarSesion(this.vista.getBtnCerrarSesion(), this.vista);
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
        Navegacion.agregarBotonVolver(ventana, vista);
        ventana.setVisible(true);
    }

    private void abrirSolicitudCita() {
        SolicitarCita ventana = new SolicitarCita(datos);
        Navegacion.agregarBotonVolver(ventana, vista);
        new SolicitarCitaPresenter(ventana).mostrar();
    }

    private void abrirMisCitas() {
        MisCitas ventana = new MisCitas(datos);
        Navegacion.agregarBotonVolver(ventana, vista);
        ventana.setVisible(true);
    }

    private void abrirMiHistorial() {
        MiHistorialMedico ventana = new MiHistorialMedico(datos);
        Navegacion.agregarBotonVolver(ventana, vista);
        ventana.setVisible(true);
    }
}