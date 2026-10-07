package Modelo.entidades;

import java.math.BigDecimal;

public class ConsultaMedicamento {

    private int idConsultaMedicamento;
    private Consulta consulta;
    private Medicamento medicamento;
    private BigDecimal dosis;
    private String frecuencia;
    private String duracion;
    private String indicaciones;


    public ConsultaMedicamento(int idConsultaMedicamento, Consulta consulta, Medicamento medicamento, BigDecimal dosis, String frecuencia, String duracion, String indicaciones) {
        this.idConsultaMedicamento = idConsultaMedicamento;
        this.consulta = consulta;
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.indicaciones = indicaciones;
    }


    public int getIdConsultaMedicamento() { return idConsultaMedicamento; }
    public void setIdConsultaMedicamento(int idConsultaMedicamento) { this.idConsultaMedicamento = idConsultaMedicamento; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public BigDecimal getDosis() { return dosis; }
    public void setDosis(BigDecimal dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }
}