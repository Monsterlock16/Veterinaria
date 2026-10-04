package repositorio;

import entidades.Animal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnimalRepositorio {

    private final List<Animal> animales;

    public AnimalRepositorio() {
        this.animales = new ArrayList<>();
    }

    public void registrar(Animal a) {
        if (a == null || buscarPorId(a.getIdAnimal()) != null) {
            return;
        }
        animales.add(a);
    }

    public List<Animal> listarTodos() {
        return new ArrayList<>(animales);
    }

    public Animal buscarPorId(int id) {
        for (Animal a : animales) {
            if (a.getIdAnimal() == id) {
                return a;
            }
        }
        return null;
    }

    public List<Animal> buscarPorPropietario(String docPropietario) {
        List<Animal> resultado = new ArrayList<>();
        if (docPropietario == null) {
            return resultado;
        }
        for (Animal a : animales) {
            if (a.getPropietario() != null && Objects.equals(a.getPropietario().getDocumento(), docPropietario)) {
                resultado.add(a);
            }
        }
        return resultado;
    }
}
