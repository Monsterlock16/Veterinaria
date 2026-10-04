package repositorio;

import entidades.Veterinario;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VeterinarioRepositorio{

    private final List<Veterinario> veterinarios;

    public VeterinarioRepositorio() {
        this.veterinarios = new ArrayList<>();
    }

    public void agregar(Veterinario v) {
        if (v == null || buscarPorDocumento(v.getDocumento()) != null || buscarPorTarjetaProf(v.getNumeroLicencia()) != null) {
            return;
        }
        veterinarios.add(v);
    }

    public List<Veterinario> obtenerVeterinarios() {
        return new ArrayList<>(veterinarios);
    }

    public Veterinario buscarPorTarjetaProf(int tp) {
        for (Veterinario v : veterinarios) {
            if (v.getNumeroLicencia() == tp) {
                return v;
            }
        }
        return null;
    }

    public Veterinario buscarPorTarjetaProf(String tp) {
        if (tp == null) {
            return null;
        }
        for (Veterinario v : veterinarios) {
            if (Objects.equals(String.valueOf(v.getNumeroLicencia()), tp)) {
                return v;
            }
        }
        return null;
    }

    public Veterinario buscarPorDocumento(String doc) {
        if (doc == null) {
            return null;
        }
        for (Veterinario v : veterinarios) {
            if (Objects.equals(v.getDocumento(), doc)) {
                return v;
            }
        }
        return null;
    }
}