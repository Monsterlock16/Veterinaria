package entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Animal {
    public enum Sexo {MACHO, HEMBRA}
    public enum EstadoAnimal {ACTIVO, INACTIVO}

    private int idAnimal;
    private Propietario propietario;
    private Raza raza;
    private String nombre;
    private LocalDate fechaNacimiento;
    private Sexo sexo;
    private BigDecimal peso;
    private String color;
    private EstadoAnimal estado;
    private LocalDate fechaIngreso;
    private LocalTime horaIngreso;

    public Animal(int idAnimal, Propietario propietario, Raza raza, String nombre,LocalDate fechaNacimiento, Sexo sexo, BigDecimal peso, String color, EstadoAnimal estado, LocalDate fechaIngreso, LocalTime horaIngreso) {
        this.idAnimal = idAnimal;
        this.propietario = propietario;
        this.raza = raza;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.peso = peso;
        this.color = color;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.horaIngreso = horaIngreso;
    }

    public int getIdAnimal() { return idAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }

    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }

    public Raza getRaza() { return raza; }
    public void setRaza(Raza raza) { this.raza = raza; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public EstadoAnimal getEstado() { return estado; }
    public void setEstado(EstadoAnimal estado) { this.estado = estado; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalTime getHoraIngreso() { return horaIngreso; }
    public void setHoraIngreso(LocalTime horaIngreso) { this.horaIngreso = horaIngreso; }
}