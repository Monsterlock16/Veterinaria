package Controlador;

import javax.swing.JOptionPane;

import Modelo.repositorio.RepositorioException;
import Vista.gui.Gstcitas_V;
import Vista.gui.Gstconsultas;
import Vista.gui.gstanimalesV;
import Vista.gui.historialmed_V;
import Vista.gui.Navegacion;
import Vista.gui.veterinario;

public class VeterinarioPresenter {
    private final veterinario vista;
    private final String usuario;

    // Constructor principal
    public VeterinarioPresenter(veterinario vista, String usuario) {
        this.vista = vista;
        this.usuario = usuario;
        
        inicializarEventos();
    }

    // Constructor sobrecargado por si se llama sin usuario explícito
    public VeterinarioPresenter(veterinario vista) {
        this(vista, "veterinario");
    }

    private void inicializarEventos() {
        if (this.vista == null) return;

        Navegacion.agregarAccionCerrarSesion(this.vista.getBtnCerrarSesion(), this.vista);

        // 1. Botón Gestionar Animales
        if (this.vista.getBtnAnimales() != null) {
            this.vista.getBtnAnimales().addActionListener(e -> ejecutarApertura("Gestión de animales", () -> {
                gstanimalesV ventanaAnimales = new gstanimalesV();
                Navegacion.agregarBotonVolver(ventanaAnimales, this.vista);
                ventanaAnimales.setVisible(true);
            }));
        }

        // 2. Botón Gestionar Citas
        if (this.vista.getBtnCitas() != null) {
            this.vista.getBtnCitas().addActionListener(e -> ejecutarApertura("Gestión de citas", () -> {
                Gstcitas_V ventanaCitas = new Gstcitas_V();
                Navegacion.agregarBotonVolver(ventanaCitas, this.vista);
                ventanaCitas.setVisible(true);
            }));
        }

        // 3. Botón Gestionar Consultas (Pasa el usuario autenticado para registrar en BD)
        if (this.vista.getBtnConsultas() != null) {
            this.vista.getBtnConsultas().addActionListener(e -> ejecutarApertura("Gestión de consultas", () -> {
                Gstconsultas ventanaConsultas = new Gstconsultas(this.usuario);
                Navegacion.agregarBotonVolver(ventanaConsultas, this.vista);
                ventanaConsultas.setVisible(true);
            }));
        }

        // 4. Botón Ver Historial Médico
        if (this.vista.getBtnHistorial() != null) {
            this.vista.getBtnHistorial().addActionListener(e -> ejecutarApertura("Historial médico", () -> {
                historialmed_V ventanaHistorial = new historialmed_V();
                Navegacion.agregarBotonVolver(ventanaHistorial, this.vista);
                ventanaHistorial.setVisible(true);
            }));
        }
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