package Vista.gui;

import javax.swing.table.DefaultTableModel;

public class DatosPropietario {
    private final String nombre;
    private int siguienteAnimalId = 1;
    private int siguienteCitaId = 1;

    private final DefaultTableModel animales = crearModelo(
        "ID", "Nombre", "Especie", "Raza", "Sexo");
    private final DefaultTableModel citas = crearModelo(
        "ID cita", "Animal", "Veterinario", "Fecha", "Hora", "Motivo", "Estado");
    private final DefaultTableModel historial = crearModelo(
        "Fecha", "Animal", "Veterinario", "Diagnóstico", "Tratamiento");

    public DatosPropietario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public DefaultTableModel getAnimales() {
        return animales;
    }

    public DefaultTableModel getCitas() {
        return citas;
    }

    public DefaultTableModel getHistorial() {
        return historial;
    }

    public int generarIdAnimal() {
        return siguienteAnimalId++;
    }

    public int generarIdCita() {
        return siguienteCitaId++;
    }

    private static DefaultTableModel crearModelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
