
import java.time.LocalDate;

abstract class Persona{
    private int idpersona;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String documento;
    private String telefono;
    private String correo;

    public Persona (int idpersona, String nombres, String apellidos, LocalDate fechaNacimiento, String documento, String telefono, String correo){

    this.idpersona = idpersona;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.fechaNacimiento = fechaNacimiento;
    this.documento = documento;
    this.telefono = telefono;
    this.correo = correo;

}
public int getIdpersona(){return idpersona;}
public void setIdpersona(int idpersona){this.idpersona = idpersona;}

public String getNombres(){return nombres;}
public void setNombres(String nombres){this.nombres=nombres;}

public String getApellidos(){return apellidos;}
public void setApellidos(String apellidos){this.apellidos=apellidos;}

public LocalDate getFechaNacimiento(){return fechaNacimiento;}
public void setFechaNacimiento(LocalDate fechaNacimiento){this.fechaNacimiento=fechaNacimiento;}

public String getDocumento(){return documento;}
public void setDocumento(String documento){this.documento=documento;}

public String getTelefono(){return telefono;}
public void setTelefono(String telefono){this.telefono=telefono;}

public String getCorreo() {return correo;}
public void setCorreo(String correo) {this.correo=correo;}

}