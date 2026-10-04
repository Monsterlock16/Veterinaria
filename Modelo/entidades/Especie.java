package entidades;

public class Especie {

    private int idEspecie;
    private String nombreEspecie;

    public Especie (int idEspecie, String nombreEspecie){
        this.idEspecie = idEspecie;
        this.nombreEspecie = nombreEspecie;
    }

    public int getidEspecie(){return idEspecie;}
    public void setidEspecie(int idEspecie){this.idEspecie = idEspecie;}

    public String getnombreEspecie(){return  nombreEspecie;}
    public void setnombreEsepcie(String nombreEspecie){this.nombreEspecie = nombreEspecie;}
    
}
