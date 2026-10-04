package repositorio;

import entidades.Propietario;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PropietarioRepositorio {
    
    private final List<Propietario> propietarios;

    public PropietarioRepositorio() {
        this.propietarios = new ArrayList<>();
    }

    public void guardar(Propietario p) {
        if (p == null || buscarPordocumento(p.getDocumento()) != null) {
            return;
        }
        propietarios.add(p);
    }

    public List<Propietario> obtenerTodos() {
        return new ArrayList<>(propietarios);
    }

    public Propietario buscarPordocumento(String doc) {
        if (doc == null) {
            return null;
        }
        for (Propietario p : propietarios) {
            if (Objects.equals(p.getDocumento(), doc)) {
                return p;
            }
        }
        return null;
    }

    public boolean eliminar(String doc) {
        Propietario p = buscarPordocumento(doc);
        if (p != null) {
            propietarios.remove(p);
            return true;
        }
        return false;
    }
}