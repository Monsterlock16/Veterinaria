package Modelo.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

public class Propietario extends Persona {
    private int idPropietario;
    private LocalDate fechaRegistro;
    private LocalTime horaRegistro;

    public Propietario(int idPropietario, LocalDate fechaRegistro, LocalTime horaRegistro, int idpersona, String nombres, String apellidos, LocalDate fechaNacimiento, String documento, String telefono, String correo){
        super(idpersona, nombres, apellidos, fechaNacimiento, documento, telefono, correo);
        this.idPropietario = idPropietario;
        this.fechaRegistro = fechaRegistro;
        this.horaRegistro = horaRegistro;
    }
    public int getIdPropietario() {return idPropietario;}
    public void setIdPropietario(int idPropietario) {this.idPropietario = idPropietario;}

    public LocalDate getfechaRegistro() {return fechaRegistro;}
    public void setfechaRegistro(LocalDate fechaRegistro) {this.fechaRegistro = fechaRegistro;}

    public LocalTime gethoraRegistro() {return horaRegistro;}
    public void sethoraRegistro(LocalTime horaRegistro) {this.horaRegistro = horaRegistro;}
}
