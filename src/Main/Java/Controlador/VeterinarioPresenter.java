package Controlador;

import Vista.gui.Gstcitas_V;
import Vista.gui.Gstconsultas;
import Vista.gui.gstanimalesV;
import Vista.gui.historialmed_V;
import Vista.gui.veterinario;

public class veterinarioPresenter {
    private final veterinario vista;
    private final String usuario;

    // Constructor principal
    public veterinarioPresenter(veterinario vista, String usuario) {
        this.vista = vista;
        this.usuario = usuario;
        
        inicializarEventos();
    }

    // Constructor sobrecargado por si se llama sin usuario explícito
    public veterinarioPresenter(veterinario vista) {
        this(vista, "veterinario");
    }

    private void inicializarEventos() {
        if (this.vista == null) return;

        // 1. Botón Gestionar Animales
        if (this.vista.getBtnAnimales() != null) {
            this.vista.getBtnAnimales().addActionListener(e -> {
                gstanimalesV ventanaAnimales = new gstanimalesV();
                ventanaAnimales.setVisible(true);
            });
        }

        // 2. Botón Gestionar Citas
        if (this.vista.getBtnCitas() != null) {
            this.vista.getBtnCitas().addActionListener(e -> {
                Gstcitas_V ventanaCitas = new Gstcitas_V();
                ventanaCitas.setVisible(true);
            });
        }

        // 3. Botón Gestionar Consultas (Pasa el usuario autenticado para registrar en BD)
        if (this.vista.getBtnConsultas() != null) {
            this.vista.getBtnConsultas().addActionListener(e -> {
                Gstconsultas ventanaConsultas = new Gstconsultas(this.usuario);
                ventanaConsultas.setVisible(true);
            });
        }

        // 4. Botón Ver Historial Médico
        if (this.vista.getBtnHistorial() != null) {
            this.vista.getBtnHistorial().addActionListener(e -> {
                historialmed_V ventanaHistorial = new historialmed_V();
                ventanaHistorial.setVisible(true);
            });
        }
    }

    public String getUsuario() {
        return usuario;
    }
}