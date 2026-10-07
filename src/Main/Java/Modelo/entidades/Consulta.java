package Modelo.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {

    private int idConsulta;
    private HistorialMedico historialMedico;
    private Cita cita;
    private LocalDate fechaConsulta;
    private LocalTime horaConsulta;
    private String sintomas;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    public Consulta() {
    }

    public Consulta(int idConsulta, HistorialMedico historialMedico, Cita cita, LocalDate fechaConsulta, LocalTime horaConsulta, String sintomas, String diagnostico, String tratamiento, String observaciones) {
        this.idConsulta = idConsulta;
        this.historialMedico = historialMedico;
        this.cita = cita;
        this.fechaConsulta = fechaConsulta;
        this.horaConsulta = horaConsulta;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
    }


    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public HistorialMedico getHistorialMedico() { return historialMedico; }
    public void setHistorialMedico(HistorialMedico historialMedico) { this.historialMedico = historialMedico; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }

    public LocalDate getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDate fechaConsulta) { this.fechaConsulta = fechaConsulta; }

    public LocalTime getHoraConsulta() { return horaConsulta; }
    public void setHoraConsulta(LocalTime horaConsulta) { this.horaConsulta = horaConsulta; }

    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}