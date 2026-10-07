package Modelo.entidades;

public class Usuario {
    private int idUsuario;
    private String usuario;
    private String contrasena;
    private String rol;
    private Integer idPersona;

    public Usuario() {
    }

    public Usuario(int idUsuario, String usuario, String contrasena, String rol, Integer idPersona) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.idPersona = idPersona;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Integer getIdPersona() { return idPersona; }
    public void setIdPersona(Integer idPersona) { this.idPersona = idPersona; }
}