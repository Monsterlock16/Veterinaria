package Modelo.entidades;

import java.time.LocalDate;

public class HistorialMedico {

    private int idHistorialMedico;
    private Animal animal;
    private LocalDate fechaCreacion;
    private String observaciones;


    public HistorialMedico(int idHistorialMedico, Animal animal, LocalDate fechaCreacion, String observaciones) {
        this.idHistorialMedico = idHistorialMedico;
        this.animal = animal;
        this.fechaCreacion = fechaCreacion;
        this.observaciones = observaciones;
    }

    public int getIdHistorialMedico() { return idHistorialMedico; }
    public void setIdHistorialMedico(int idHistorialMedico) { this.idHistorialMedico = idHistorialMedico; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}