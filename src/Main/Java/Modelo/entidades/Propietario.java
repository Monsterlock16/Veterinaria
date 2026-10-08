package Modelo.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

public class Propietario extends Persona {
    private int idPropietario;
    private LocalDate fechaRegistro;
    private LocalTime horaRegistro;
    private String nombreUsuario;

    public Propietario(int idPropietario, LocalDate fechaRegistro, LocalTime horaRegistro, 
                       int idpersona, String nombres, String apellidos, LocalDate fechaNacimiento, 
                       String documento, String telefono, String correo, String nombreUsuario) {
        super(idpersona, nombres, apellidos, fechaNacimiento, documento, telefono, correo);
        this.idPropietario = idPropietario;
        this.fechaRegistro = fechaRegistro;
        this.horaRegistro = horaRegistro;
        this.nombreUsuario = nombreUsuario;
    }

    // Constructor sobrecargado para mantener compatibilidad con repositorios antiguos
    public Propietario(int idPropietario, LocalDate fechaRegistro, LocalTime horaRegistro, 
                       int idpersona, String nombres, String apellidos, LocalDate fechaNacimiento, 
                       String documento, String telefono, String correo) {
        this(idPropietario, fechaRegistro, horaRegistro, idpersona, nombres, apellidos, fechaNacimiento, documento, telefono, correo, null);
    }

    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalTime getHoraRegistro() { return horaRegistro; }
    public void setHoraRegistro(LocalTime horaRegistro) { this.horaRegistro = horaRegistro; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}