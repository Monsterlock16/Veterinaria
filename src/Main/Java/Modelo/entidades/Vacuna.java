package Modelo.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Vacuna {

    private int idVacuna;
    private HistorialMedico historial;
    private Consulta consulta;
    private String nombre;
    private BigDecimal dosis;
    private LocalDate fechaAplicacion;
    private LocalDate proximaFecha;

    public Vacuna(int idVacuna, HistorialMedico historial, Consulta consulta, String nombre, BigDecimal dosis, LocalDate fechaAplicacion, LocalDate proximaFecha) {
        this.idVacuna = idVacuna;
        this.historial = historial;
        this.consulta = consulta;
        this.nombre = nombre;
        this.dosis = dosis;
        this.fechaAplicacion = fechaAplicacion;
        this.proximaFecha = proximaFecha;
    }

    public int getIdVacuna() { return idVacuna; }
    public void setIdVacuna(int idVacuna) { this.idVacuna = idVacuna; }

    public HistorialMedico getHistorial() { return historial; }
    public void setHistorial(HistorialMedico historial) { this.historial = historial; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getDosis() { return dosis; }
    public void setDosis(BigDecimal dosis) { this.dosis = dosis; }

    public LocalDate getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(LocalDate fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }

    public LocalDate getProximaFecha() { return proximaFecha; }
    public void setProximaFecha(LocalDate proximaFecha) { this.proximaFecha = proximaFecha; }
}
