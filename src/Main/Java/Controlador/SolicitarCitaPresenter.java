package Controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import Modelo.entidades.Animal;
import Modelo.entidades.Cita;
import Modelo.entidades.Veterinario;
import Modelo.repositorio.AnimalRepositorio;
import Modelo.repositorio.CitaRepositorio;
import Modelo.repositorio.RepositorioException;
import Modelo.repositorio.VeterinarioRepositorio;
import Vista.gui.DatosPropietario;
import Vista.gui.SolicitarCita;
import Vista.gui.propietario;

public class SolicitarCitaPresenter {

    // Horario de atencion: de 8:00 a 18:00 (la ultima cita empieza a las 17:00)
    private static final int HORA_APERTURA = 8;
    private static final int HORA_CIERRE = 18;

    private final SolicitarCita vista;
    private final DatosPropietario datos;

    private final CitaRepositorio citaRepositorio;
    private final VeterinarioRepositorio veterinarioRepositorio;
    private final AnimalRepositorio animalRepositorio;

    // Texto que se ve en el combo de horas ("08:00 AM") -> hora real
    private final Map<String, LocalTime> horasPorTexto = new LinkedHashMap<>();

    public SolicitarCitaPresenter(SolicitarCita vista) {
        this.vista = vista;
        this.datos = vista.getDatos();
        this.citaRepositorio = new CitaRepositorio();
        this.veterinarioRepositorio = new VeterinarioRepositorio();
        this.animalRepositorio = new AnimalRepositorio();

        inicializarEventos();
        cargarMascotasCombo();
        cargarVeterinariosCombo();
    }

    private void inicializarEventos() {
        vista.getBtnVolver().addActionListener(e -> volverAlMenu());
        vista.getCbVeterinarios().addActionListener(e -> actualizarHorasDisponibles());
        // Al elegir un dia en el calendario se recalculan las horas libres
        vista.setAlSeleccionarFecha(this::actualizarHorasDisponibles);
        vista.getBtnSolicitar().addActionListener(e -> agendarCita());
    }

    private void volverAlMenu() {
        vista.dispose();
        propietario menu = new propietario(datos.getNombre());
        new PropietarioPresenter(menu, datos.getNombre());
        menu.setVisible(true);
    }

    private void cargarMascotasCombo() {
        vista.getCbMascotas().removeAllItems();
        vista.getMapaMascotas().clear();

        try {
            // Solo las mascotas del propietario que inicio sesion
            List<Animal> mascotas = animalRepositorio.buscarPorUsuario(datos.getNombre());
            for (Animal a : mascotas) {
                String item = a.getNombre() + " (ID: " + a.getIdAnimal() + ")";
                vista.getCbMascotas().addItem(item);
                vista.getMapaMascotas().put(item, a.getIdAnimal());
            }
            if (mascotas.isEmpty()) {
                vista.getCbMascotas().addItem("No tiene mascotas registradas");
            }
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar mascotas: " + ex.getMessage());
        }
    }

    private void cargarVeterinariosCombo() {
        vista.getCbVeterinarios().removeAllItems();
        vista.getMapaVeterinarios().clear();

        try {
            List<Veterinario> veterinarios = veterinarioRepositorio.obtenerTodos();
            for (Veterinario v : veterinarios) {
                String item = "Dr(a). " + v.getNombres() + " " + v.getApellidos() + " (ID: " + v.getIdVeterinario() + ")";
                vista.getCbVeterinarios().addItem(item);
                vista.getMapaVeterinarios().put(item, v.getIdVeterinario());
            }
            if (veterinarios.isEmpty()) {
                vista.getCbVeterinarios().addItem("No hay veterinarios registrados");
            }
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar veterinarios: " + ex.getMessage());
        }
    }

    /** Llena el combo con las horas libres del veterinario elegido en el dia elegido. */
    public void actualizarHorasDisponibles() {
        vista.getCbHorasDisponibles().removeAllItems();
        horasPorTexto.clear();

        LocalDate fecha = vista.getFechaSeleccionada();
        Object vetSeleccionado = vista.getCbVeterinarios().getSelectedItem();
        if (fecha == null || vetSeleccionado == null) {
            return;
        }
        Integer idVeterinario = vista.getMapaVeterinarios().get(vetSeleccionado.toString());
        if (idVeterinario == null) {
            return;
        }

        try {
            List<LocalTime> ocupadas = horasOcupadas(idVeterinario, fecha);
            LocalTime ahora = LocalTime.now();
            boolean esHoy = fecha.equals(LocalDate.now());

            for (int h = HORA_APERTURA; h < HORA_CIERRE; h++) {
                LocalTime hora = LocalTime.of(h, 0);
                boolean yaPaso = esHoy && !hora.isAfter(ahora);
                if (yaPaso || ocupadas.contains(hora)) {
                    continue;
                }
                String texto = formatearHora(hora);
                horasPorTexto.put(texto, hora);
                vista.getCbHorasDisponibles().addItem(texto);
            }
            if (horasPorTexto.isEmpty()) {
                vista.getCbHorasDisponibles().addItem("Sin horas disponibles");
            }
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudieron consultar las horas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Horas que el veterinario ya tiene tomadas ese dia (todo menos las citas canceladas). */
    private List<LocalTime> horasOcupadas(int idVeterinario, LocalDate fecha) {
        List<LocalTime> ocupadas = new ArrayList<>();
        for (Cita c : citaRepositorio.obtenerTodas()) {
            if (c.getVeterinario() != null
                    && c.getVeterinario().getIdVeterinario() == idVeterinario
                    && fecha.equals(c.getFecha())
                    && c.getEstado() != Cita.EstadoCita.CANCELADA) {
                ocupadas.add(c.getHora());
            }
        }
        return ocupadas;
    }

    private String formatearHora(LocalTime hora) {
        int hora12 = hora.getHour() % 12 == 0 ? 12 : hora.getHour() % 12;
        return String.format("%02d:00 %s", hora12, hora.getHour() >= 12 ? "PM" : "AM");
    }

    private void agendarCita() {
        Object mascotaSel = vista.getCbMascotas().getSelectedItem();
        if (mascotaSel == null || !vista.getMapaMascotas().containsKey(mascotaSel.toString())) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una mascota.");
            return;
        }

        Object vetSel = vista.getCbVeterinarios().getSelectedItem();
        if (vetSel == null || !vista.getMapaVeterinarios().containsKey(vetSel.toString())) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un veterinario válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (vista.getFechaSeleccionada() == null) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una fecha en el calendario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object horaSel = vista.getCbHorasDisponibles().getSelectedItem();
        LocalTime horaCita = horaSel == null ? null : horasPorTexto.get(horaSel.toString());
        if (horaCita == null) {
            JOptionPane.showMessageDialog(vista, "No hay una hora disponible seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String motivo = vista.getTxtMotivo().getText().trim();
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Escriba el motivo de la consulta.");
            return;
        }

        int idAnimal = vista.getMapaMascotas().get(mascotaSel.toString());
        int idVeterinario = vista.getMapaVeterinarios().get(vetSel.toString());

        try {
            if (horasOcupadas(idVeterinario, vista.getFechaSeleccionada()).contains(horaCita)) {
                JOptionPane.showMessageDialog(vista, "Esa hora ya fue tomada. Elija otra.");
                actualizarHorasDisponibles();
                return;
            }
            Animal animal = animalRepositorio.buscarPorId(idAnimal);
            Veterinario veterinario = veterinarioRepositorio.buscarPorId(idVeterinario);
            if (animal == null || veterinario == null) {
                JOptionPane.showMessageDialog(vista, "No se encontró la mascota o el veterinario.");
                return;
            }

            // Queda PENDIENTE hasta que el veterinario la acepte
            Cita nuevaCita = new Cita(0, animal, veterinario, vista.getFechaSeleccionada(),
                    horaCita, motivo, Cita.EstadoCita.PENDIENTE);
            citaRepositorio.agendar(nuevaCita);

            JOptionPane.showMessageDialog(vista, "Solicitud enviada para el " + vista.getFechaSeleccionada()
                    + " a las " + horaSel + ".\nQueda pendiente hasta que el veterinario la acepte.");
            volverAlMenu();
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudo solicitar la cita: " + ex.getMessage());
            actualizarHorasDisponibles();
        }
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}