import java.time.LocalDate;
import java.time.LocalTime;

import Modelo.entidades.Propietario;
import Modelo.repositorio.PropietarioRepositorio;

public class Main {
    public static void main(String[] args) {
        PropietarioRepositorio repo = new PropietarioRepositorio();
        Propietario p = new Propietario(0, LocalDate.now(), LocalTime.now().withNano(0), 0,
                "Luis", "Gomez", LocalDate.of(1985, 5, 5), "100", "3001234567", "luis@correo.com");
        repo.guardar(p);
        System.out.println("Guardado con id " + p.getIdPropietario());
        System.out.println("Total en la base: " + repo.obtenerTodos().size());
    }
}