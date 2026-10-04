package repositorio;

import entidades.Cita;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CitaRepositorio {

    private final List<Cita> citas;

    public CitaRepositorio() {
        this.citas = new ArrayList<>();
    }

    public void agendar(Cita c) {
        if (c == null || buscarPorId(c.getIdCita()) != null) {
            return;
        }
        citas.add(c);
    }

    public List<Cita> obtenerTodas() {
        return new ArrayList<>(citas);
    }

    public Cita buscarPorId(int id) {
        for (Cita c : citas) {
            if (c.getIdCita() == id) {
                return c;
            }
        }
        return null;
    }

    // Para consultar la agenda del día
    public List<Cita> buscarPorFecha(LocalDate fecha) {
        List<Cita> citasDelDia = new ArrayList<>();
        if (fecha == null) {
            return citasDelDia;
        }
        for (Cita c : citas) {
            if (c.getFecha() != null && c.getFecha().equals(fecha)) {
                citasDelDia.add(c);
            }
        }
        return citasDelDia;
    }
}