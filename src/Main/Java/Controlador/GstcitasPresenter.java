package Controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import Vista.gui.Gstcitas_V;

public class GstcitasPresenter {

    // Horario de atencion: de 8:00 a 18:00 (la ultima cita empieza a las 17:00)
    private static final int HORA_APERTURA = 8;
    private static final int HORA_CIERRE = 18;

    private final Gstcitas_V vista;
    private final CitaRepositorio citaRepositorio = new CitaRepositorio();
    private final AnimalRepositorio animalRepositorio = new AnimalRepositorio();
    private final VeterinarioRepositorio veterinarioRepositorio = new VeterinarioRepositorio();
    private Veterinario veterinario;

    // Texto del combo ("08:00 AM") -> hora real
    private final Map<String, LocalTime> horasPorTexto = new LinkedHashMap<>();

    /** usuario: el nombre de usuario con el que inicio sesion el veterinario. */
    public GstcitasPresenter(Gstcitas_V vista, String usuario) {
        this.vista = vista;
        try {
            this.veterinario = veterinarioRepositorio.buscarPorUsuario(usuario);
        } catch (RepositorioException e) {
            this.veterinario = null;
        }
        inicializarEventos();

        if (veterinario == null) {
            vista.mostrarMensaje("No se encontró un veterinario asociado al usuario que inició sesión.",
                    "Veterinario no encontrado", JOptionPane.WARNING_MESSAGE);
            deshabilitarAcciones();
            return;
        }
        cargarCombos();
        cargarCitas();
    }

    public GstcitasPresenter(Gstcitas_V vista) {
        this(vista, null);
    }

    private void inicializarEventos() {
        vista.getBtnAgendar().addActionListener(e -> agendarManual());
        // Al elegir un dia en el calendario se recalculan las horas libres
        vista.setAlSeleccionarFecha(this::actualizarHorasDisponibles);
        vista.getBtnAceptar().addActionListener(e -> cambiarEstadoSeleccionado(
                "aceptar", List.of("PENDIENTE"), Cita.EstadoCita.PROGRAMADA, false));
        vista.getBtnRechazar().addActionListener(e -> cambiarEstadoSeleccionado(
                "rechazar", List.of("PENDIENTE", "PROGRAMADA"), Cita.EstadoCita.CANCELADA, true));
        vista.getBtnAtendida().addActionListener(e -> cambiarEstadoSeleccionado(
                "marcar como atendida", List.of("PROGRAMADA"), Cita.EstadoCita.ATENDIDA, false));
        vista.getBtnActualizar().addActionListener(e -> cargarCitas());
    }

    private void deshabilitarAcciones() {
        vista.getBtnAgendar().setEnabled(false);
        vista.getBtnAceptar().setEnabled(false);
        vista.getBtnRechazar().setEnabled(false);
        vista.getBtnAtendida().setEnabled(false);
        vista.getBtnActualizar().setEnabled(false);
    }

    private void cargarCombos() {
        vista.getCbAnimales().removeAllItems();
        vista.getMapaAnimales().clear();
        vista.getCbVeterinarios().removeAllItems();
        vista.getMapaVeterinarios().clear();

        // El veterinario que inicio sesion es el unico de la lista
        String itemVet = veterinario.getNombres() + " " + veterinario.getApellidos()
                + " (ID: " + veterinario.getIdVeterinario() + ")";
        vista.getCbVeterinarios().addItem(itemVet);
        vista.getMapaVeterinarios().put(itemVet, veterinario.getIdVeterinario());
        vista.getCbVeterinarios().setEnabled(false);

        try {
            for (Animal a : animalRepositorio.obtenerTodos()) {
                String item = a.getNombre() + " (ID: " + a.getIdAnimal() + ")";
                vista.getCbAnimales().addItem(item);
                vista.getMapaAnimales().put(item, a.getIdAnimal());
            }
        } catch (RepositorioException ex) {
            vista.mostrarMensaje("Error al cargar animales: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Citas de este veterinario: primero las pendientes, luego por fecha y hora (la mas reciente arriba). */
    private List<Cita> citasDelVeterinario() {
        List<Cita> propias = new ArrayList<>();
        for (Cita c : citaRepositorio.obtenerTodas()) {
            if (c.getVeterinario() != null
                    && c.getVeterinario().getIdVeterinario() == veterinario.getIdVeterinario()) {
                propias.add(c);
            }
        }
        propias.sort(Comparator.comparing((Cita c) -> c.getEstado() != Cita.EstadoCita.PENDIENTE)
                .thenComparing(Cita::getFecha, Comparator.reverseOrder())
                .thenComparing(Cita::getHora, Comparator.reverseOrder()));
        return propias;
    }

    /** Horas que el veterinario ya tiene tomadas ese dia (todo menos las citas canceladas). */
    private List<LocalTime> horasOcupadas(LocalDate fecha) {
        List<LocalTime> ocupadas = new ArrayList<>();
        for (Cita c : citasDelVeterinario()) {
            if (fecha.equals(c.getFecha()) && c.getEstado() != Cita.EstadoCita.CANCELADA) {
                ocupadas.add(c.getHora());
            }
        }
        return ocupadas;
    }

    /** Llena el combo con las horas libres del dia elegido en el calendario. */
    private void actualizarHorasDisponibles() {
        vista.getCbHoras().removeAllItems();
        horasPorTexto.clear();

        LocalDate fecha = vista.getFechaSeleccionada();
        if (fecha == null || veterinario == null) {
            return;
        }

        try {
            List<LocalTime> ocupadas = horasOcupadas(fecha);
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
                vista.getCbHoras().addItem(texto);
            }
            if (horasPorTexto.isEmpty()) {
                vista.getCbHoras().addItem("Sin horas disponibles");
            }
        } catch (RepositorioException ex) {
            vista.mostrarMensaje("No se pudieron consultar las horas: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatearHora(LocalTime hora) {
        int hora12 = hora.getHour() % 12 == 0 ? 12 : hora.getHour() % 12;
        return String.format("%02d:00 %s", hora12, hora.getHour() >= 12 ? "PM" : "AM");
    }

    public final void cargarCitas() {
        vista.getModelo().setRowCount(0);
        if (veterinario == null) {
            return;
        }
        try {
            String nombreVet = veterinario.getNombres() + " " + veterinario.getApellidos();
            List<Cita> citas = citasDelVeterinario();
            for (Cita c : citas) {
                vista.getModelo().addRow(new Object[]{
                    c.getIdCita(),
                    c.getAnimal().getNombre(),
                    nombreVet,
                    c.getFecha(),
                    c.getHora(),
                    c.getMotivo(),
                    c.getEstado().name()
                });
            }
        } catch (RepositorioException ex) {
            vista.mostrarMensaje("Error al cargar la lista de citas: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Aceptar, rechazar o marcar atendida la cita seleccionada en la tabla. */
    private void cambiarEstadoSeleccionado(String accion, List<String> estadosPermitidos,
                                           Cita.EstadoCita nuevoEstado, boolean pedirConfirmacion) {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0) {
            vista.mostrarMensaje("Seleccione una cita de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idCita = (Integer) vista.getModelo().getValueAt(fila, 0);
        String estadoActual = vista.getModelo().getValueAt(fila, 6).toString();

        if (!estadosPermitidos.contains(estadoActual)) {
            vista.mostrarMensaje("No se puede " + accion + " una cita en estado " + estadoActual + ".",
                    "Acción no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (pedirConfirmacion && !vista.confirmar("¿Desea " + accion + " la cita " + idCita + "?")) {
            return;
        }

        try {
            citaRepositorio.actualizarEstado(idCita, nuevoEstado);
            cargarCitas();
        } catch (RepositorioException ex) {
            vista.mostrarMensaje("No se pudo actualizar la cita: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** El veterinario agenda directamente una cita (ya queda programada). */
    private void agendarManual() {
        Object animalSel = vista.getCbAnimales().getSelectedItem();
        if (animalSel == null || !vista.getMapaAnimales().containsKey(animalSel.toString())) {
            vista.mostrarMensaje("Debe seleccionar un animal válido.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fecha = vista.getFechaSeleccionada();
        if (fecha == null) {
            vista.mostrarMensaje("Seleccione un día en el calendario.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object horaSel = vista.getCbHoras().getSelectedItem();
        LocalTime hora = horaSel == null ? null : horasPorTexto.get(horaSel.toString());
        if (hora == null) {
            vista.mostrarMensaje("No hay una hora disponible seleccionada.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String motivo = vista.getTxtMotivo().getText().trim();
        if (motivo.isEmpty()) {
            vista.mostrarMensaje("Escriba el motivo de la cita.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Se verifica otra vez por si la hora se tomo mientras tanto
            if (horasOcupadas(fecha).contains(hora)) {
                vista.mostrarMensaje("Esa hora ya fue tomada. Elija otra.", "Hora ocupada", JOptionPane.WARNING_MESSAGE);
                actualizarHorasDisponibles();
                return;
            }
            Animal animal = animalRepositorio.buscarPorId(vista.getMapaAnimales().get(animalSel.toString()));
            if (animal == null) {
                vista.mostrarMensaje("No se encontró el animal.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Cita cita = new Cita(0, animal, veterinario, fecha, hora, motivo, Cita.EstadoCita.PROGRAMADA);
            citaRepositorio.agendar(cita);
            vista.mostrarMensaje("¡Cita agendada exitosamente!", "Listo", JOptionPane.INFORMATION_MESSAGE);
            vista.getTxtMotivo().setText("");
            actualizarHorasDisponibles();
            cargarCitas();
        } catch (RepositorioException ex) {
            vista.mostrarMensaje("No se pudo agendar la cita: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}