package Modelo.entidades;

import java.time.LocalDate;

public class Veterinario extends Persona {
    private int idVeterinario;
    private String especialidad;
    private String numeroLicencia;
    private String nombreUsuario;

    public Veterinario(int idVeterinario, String especialidad, String numeroLicencia, 
                       int idpersona, String nombres, String apellidos, LocalDate fechaNacimiento, 
                       String documento, String telefono, String correo, String nombreUsuario) {
        super(idpersona, nombres, apellidos, fechaNacimiento, documento, telefono, correo);
        this.idVeterinario = idVeterinario;
        this.especialidad = especialidad;
        this.numeroLicencia = numeroLicencia;
        this.nombreUsuario = nombreUsuario;
    }

    // Sobrecarga para aceptar numeroLicencia como entero
    public Veterinario(int idVeterinario, String especialidad, int numeroLicencia, 
                       int idpersona, String nombres, String apellidos, LocalDate fechaNacimiento, 
                       String documento, String telefono, String correo) {
        this(idVeterinario, especialidad, String.valueOf(numeroLicencia), idpersona, nombres, apellidos, fechaNacimiento, documento, telefono, correo, null);
    }

    public int getIdVeterinario() { return idVeterinario; }
    public void setIdVeterinario(int idVeterinario) { this.idVeterinario = idVeterinario; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getNumeroLicencia() { return numeroLicencia; }
    public void setNumeroLicencia(String numeroLicencia) { this.numeroLicencia = numeroLicencia; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}