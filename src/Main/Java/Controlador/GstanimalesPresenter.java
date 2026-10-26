package Controlador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Modelo.entidades.Animal;
import Modelo.repositorio.AnimalRepositorio;
import Modelo.repositorio.RepositorioException;
import Vista.gui.gstanimalesV;

public class GstanimalesPresenter {
    private final gstanimalesV vista;
    private final AnimalRepositorio repositorio = new AnimalRepositorio();

    // Animales de la tabla, en el mismo orden del modelo (no de lo que se ve si se filtra u ordena)
    private List<Animal> animales = new ArrayList<>();
    // Evita reaccionar a la tabla mientras se llena por codigo
    private boolean cargando = false;

    public GstanimalesPresenter(gstanimalesV vista) {
        this.vista = vista;
        inicializarEventos();
        cargarDatosBD();
        modoSinSeleccion();
    }

    private void inicializarEventos() {
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnLimpiar().addActionListener(e -> modoSinSeleccion());

        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !cargando) {
                alSeleccionarFila();
            }
        });

        vista.getTxtBuscar().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
    }

    /** Lee todos los animales de la base de datos y llena la tabla. */
    public final void cargarDatosBD() {
        cargando = true;
        try {
            vista.getModelo().setRowCount(0);
            animales = repositorio.listarTodos();
            for (Animal a : animales) {
                String propietario = a.getPropietario() != null
                        ? a.getPropietario().getNombres() + " " + a.getPropietario().getApellidos()
                        : "Sin Propietario";
                vista.getModelo().addRow(new Object[]{
                    a.getIdAnimal(),
                    propietario,
                    a.getNombre(),
                    a.getRaza().getEspecie().getnombreEspecie(),
                    a.getRaza().getNombre(),
                    a.getPeso(),
                    a.getColor(),
                    a.getFechaNacimiento(),
                    a.getSexo().name(),
                    a.getEstado().name()
                });
            }
        } catch (RepositorioException e) {
            animales = new ArrayList<>();
            JOptionPane.showMessageDialog(vista, "No se pudieron cargar los animales: " + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        } finally {
            cargando = false;
        }
    }

    private void filtrar() {
        String texto = vista.getTxtBuscar().getText().trim();
        if (texto.isEmpty()) {
            vista.getSorter().setRowFilter(null);
        } else {
            // Columnas 1 (propietario) y 2 (animal); sin distinguir mayusculas
            vista.getSorter().setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 1, 2));
        }
    }

    private Animal animalSeleccionado() {
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista < 0) {
            return null;
        }
        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        return filaModelo < animales.size() ? animales.get(filaModelo) : null;
    }

    private void alSeleccionarFila() {
        Animal a = animalSeleccionado();
        if (a == null) {
            modoSinSeleccion();
            return;
        }
        vista.getTxtPropietario().setText(a.getPropietario() != null
                ? a.getPropietario().getNombres() + " " + a.getPropietario().getApellidos() : "Sin Propietario");
        vista.getTxtNombre().setText(a.getNombre());
        vista.getTxtEspecie().setText(a.getRaza().getEspecie().getnombreEspecie());
        vista.getTxtRaza().setText(a.getRaza().getNombre());
        vista.getTxtColor().setText(a.getColor());
        vista.getTxtFechaNacimiento().setText(String.valueOf(a.getFechaNacimiento()));
        vista.getTxtSexo().setText(capitalizar(a.getSexo().name()));
        vista.getSpPeso().setValue(a.getPeso().doubleValue());
        vista.getCbEstadoSalud().setSelectedItem(capitalizar(a.getEstado().name()));

        vista.getSpPeso().setEnabled(true);
        vista.getCbEstadoSalud().setEnabled(true);
        vista.getBtnActualizar().setEnabled(true);
    }

    /** Formulario vacio y sin nada que editar hasta que se seleccione un animal. */
    private void modoSinSeleccion() {
        cargando = true;
        try {
            vista.getTabla().clearSelection();
        } finally {
            cargando = false;
        }
        vista.getTxtPropietario().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtEspecie().setText("");
        vista.getTxtRaza().setText("");
        vista.getTxtColor().setText("");
        vista.getTxtFechaNacimiento().setText("");
        vista.getTxtSexo().setText("");
        vista.getSpPeso().setValue(0.0);
        vista.getCbEstadoSalud().setSelectedIndex(0);

        vista.getSpPeso().setEnabled(false);
        vista.getCbEstadoSalud().setEnabled(false);
        vista.getBtnActualizar().setEnabled(false);
    }

    /** Guarda en la base de datos el nuevo peso y estado de salud del animal seleccionado. */
    private void actualizar() {
        Animal a = animalSeleccionado();
        if (a == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione un animal de la tabla.");
            return;
        }

        BigDecimal peso = BigDecimal.valueOf(((Number) vista.getSpPeso().getValue()).doubleValue())
                .setScale(2, RoundingMode.HALF_UP);
        if (peso.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(vista, "El peso debe ser mayor que 0.",
                    "Datos no válidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            a.setPeso(peso);
            a.setEstado(Animal.EstadoAnimal.valueOf(
                    vista.getCbEstadoSalud().getSelectedItem().toString().toUpperCase(Locale.ROOT)));

            if (repositorio.actualizar(a)) {
                JOptionPane.showMessageDialog(vista, "¡Animal actualizado correctamente!");
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró el animal en la base de datos.");
            }
        } catch (RepositorioException e) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar en la base de datos: " + e.getMessage(),
                    "Error BD", JOptionPane.ERROR_MESSAGE);
        }
        cargarDatosBD();
        modoSinSeleccion();
    }

    private String capitalizar(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase(Locale.ROOT);
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}
