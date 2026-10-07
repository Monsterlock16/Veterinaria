package Modelo.entidades;

public class Raza {
	private int idRaza;
	private Especie especie;
	private String nombre;

	public Raza(int idRaza, Especie especie, String nombre) {
		this.idRaza = idRaza;
		this.especie = especie;
		this.nombre = nombre;
	}

	public int getIdRaza() { return idRaza; }
	public void setIdRaza(int idRaza) { this.idRaza = idRaza; }

	public Especie getEspecie() { return especie; }
	public void setEspecie(Especie especie) { this.especie = especie; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
}
