import java.time.LocalDate;

public class Veterinario extends Persona {
    private int idVeterinario;
    private String especialidad;
    private int numeroLicencia;

    public Veterinario(int idVeterinario, String especialidad, int numeroLicencia, int idpersona, String nombres, String apellidos, LocalDate fechaNacimiento, String documento, String telefono, String correo) {

        super(idpersona, nombres, apellidos, fechaNacimiento, documento, telefono, correo);
        this.idVeterinario = idVeterinario;
        this.especialidad = especialidad;
        this.numeroLicencia = numeroLicencia;
    }

    public int getIdVeterinario() {return idVeterinario;}
    public void setIdVeterinario(int idVeterinario) {this.idVeterinario = idVeterinario;}

    public String getEspecialidad() {return especialidad;}
    public void setEspecialidad(String especialidad) {this.especialidad = especialidad;}

    public int getNumeroLicencia() {return numeroLicencia;}
    public void setNumeroLicencia(int numeroLicencia) {this.numeroLicencia = numeroLicencia;}
}
