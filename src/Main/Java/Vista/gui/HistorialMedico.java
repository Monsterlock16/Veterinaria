package Vista.gui;

import Modelo.entidades.Animal;
import java.time.LocalDate;

public class HistorialMedico {
    private final int idHistorialMedico;
    private final Animal animal;
    private final LocalDate fechaCreacion;
    private final String observaciones;

    public HistorialMedico(
            int idHistorialMedico,
            Animal animal,
            LocalDate fechaCreacion,
            String observaciones) {
        this.idHistorialMedico = idHistorialMedico;
        this.animal = animal;
        this.fechaCreacion = fechaCreacion;
        this.observaciones = observaciones;
    }

    public int getIdHistorialMedico() {
        return idHistorialMedico;
    }

    public Animal getAnimal() {
        return animal;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
