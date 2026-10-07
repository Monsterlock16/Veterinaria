package Modelo.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {

    public enum EstadoCita {PROGRAMADA, ATENDIDA, CANCELADA}

    private int idCita;
    private Animal animal;
    private Veterinario veterinario;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private EstadoCita estado;
    public Cita(int idCita, Animal animal, Veterinario veterinario, LocalDate fecha, LocalTime hora, String motivo, EstadoCita estado) {
        this.idCita = idCita;
        this.animal = animal;
        this.veterinario = veterinario;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public Veterinario getVeterinario() { return veterinario; }
    public void setVeterinario(Veterinario veterinario) { this.veterinario = veterinario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
}