package Controlador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import Modelo.entidades.Animal;
import Modelo.entidades.Especie;
import Modelo.entidades.Propietario;
import Modelo.entidades.Raza;
import Modelo.repositorio.AnimalRepositorio;
import Modelo.repositorio.CatalogoRepositorio;
import Modelo.repositorio.PropietarioRepositorio;
import Modelo.repositorio.RepositorioException;
import Vista.gui.MisAnimales;

public class MisAnimalesPresenter {
    private final MisAnimales vista;
    private final String usuario;

    private final AnimalRepositorio animalRepo = new AnimalRepositorio();
    private final PropietarioRepositorio propietarioRepo = new PropietarioRepositorio();
    private final CatalogoRepositorio catalogoRepo = new CatalogoRepositorio();

    private Propietario propietario;
    // Mascotas que muestra la tabla, en el mismo orden de las filas
    private List<Animal> animales = new ArrayList<>();
    // Evita que los eventos de los combos y de la tabla se disparen al llenar el formulario por codigo
    private boolean cargando = false;

    public MisAnimalesPresenter(MisAnimales vista) {
        this.vista = vista;
        this.usuario = vista.getDatos().getNombre();

        try {
            this.propietario = propietarioRepo.buscarPorUsuario(usuario);
        } catch (RepositorioException e) {
            this.propietario = null;
        }
        inicializarEventos();

        if (propietario == null) {
            JOptionPane.showMessageDialog(vista,
                    "No se encontró el registro de propietario para el usuario que inició sesión.",
                    "Propietario no encontrado", JOptionPane.WARNING_MESSAGE);
            vista.getBtnGuardar().setEnabled(false);
            vista.getBtnActualizar().setEnabled(false);
            vista.getBtnNuevo().setEnabled(false);
            return;
        }
        cargarEspecies();
        cargarTabla();
        modoNuevo();
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnNuevo().addActionListener(e -> modoNuevo());

        // Al elegir (o escribir) una especie, el combo de razas muestra las de esa especie
        vista.getCbEspecie().addActionListener(e -> {
            if (!cargando) {
                cargarRazas(texto(vista.getCbEspecie()));
            }
        });

        // Al seleccionar una mascota de la tabla, sus datos pasan al formulario
        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !cargando) {
                alSeleccionarFila();
            }
        });
    }

    // ---------- Carga de datos ----------

    private void cargarEspecies() {
        cargando = true;
        try {
            vista.getCbEspecie().removeAllItems();
            for (String esp : catalogoRepo.listarEspecies()) {
                vista.getCbEspecie().addItem(esp);
            }
            vista.getCbEspecie().setSelectedItem("");
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudieron cargar las especies: " + ex.getMessage());
        } finally {
            cargando = false;
        }
    }

    private void cargarRazas(String especie) {
        boolean anterior = cargando;
        cargando = true;
        try {
            vista.getCbRaza().removeAllItems();
            for (String raza : catalogoRepo.listarRazas(especie)) {
                vista.getCbRaza().addItem(raza);
            }
            vista.getCbRaza().setSelectedItem("");
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudieron cargar las razas: " + ex.getMessage());
        } finally {
            cargando = anterior;
        }
    }

    private void cargarTabla() {
        cargando = true;
        try {
            vista.getModelo().setRowCount(0);
            animales = animalRepo.buscarPorUsuario(usuario);
            for (Animal a : animales) {
                vista.getModelo().addRow(new Object[]{
                    a.getIdAnimal(), a.getNombre(),
                    a.getRaza().getEspecie().getnombreEspecie(), a.getRaza().getNombre(),
                    a.getFechaNacimiento(), a.getPeso(), a.getColor(), capitalizar(a.getSexo().name())
                });
            }
        } catch (RepositorioException ex) {
            animales = new ArrayList<>();
            JOptionPane.showMessageDialog(vista, "Error al cargar mascotas: " + ex.getMessage());
        } finally {
            cargando = false;
        }
    }

    // ---------- Seleccion de la tabla ----------

    private void alSeleccionarFila() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0 || fila >= animales.size()) {
            modoNuevo();
            return;
        }
        Animal a = animales.get(fila);
        mostrarEnFormulario(a);
        // Ya existe: no se puede "guardar" de nuevo, solo actualizar
        vista.getBtnGuardar().setEnabled(false);
        vista.getBtnActualizar().setEnabled(true);
        vista.setTituloFormulario("Editar mascota (ID " + a.getIdAnimal() + ")");
    }

    private void mostrarEnFormulario(Animal a) {
        cargando = true;
        try {
            vista.getTxtNombre().setText(a.getNombre());
            String especie = a.getRaza().getEspecie().getnombreEspecie();
            vista.getCbEspecie().setSelectedItem(especie);
            cargarRazas(especie);
            vista.getCbRaza().setSelectedItem(a.getRaza().getNombre());
            vista.getSpFechaNacimiento().setValue(aDate(a.getFechaNacimiento()));
            vista.getSpPeso().setValue(a.getPeso().doubleValue());
            vista.getTxtColor().setText(a.getColor());
            vista.getCbSexo().setSelectedItem(capitalizar(a.getSexo().name()));
        } finally {
            cargando = false;
        }
    }

    /** Formulario vacio, sin fila seleccionada y con "Guardar" habilitado. */
    private void modoNuevo() {
        cargando = true;
        try {
            vista.getTabla().clearSelection();
            vista.getTxtNombre().setText("");
            vista.getCbEspecie().setSelectedItem("");
            vista.getCbRaza().removeAllItems();
            vista.getCbRaza().setSelectedItem("");
            vista.getSpFechaNacimiento().setValue(new Date());
            vista.getSpPeso().setValue(0.0);
            vista.getTxtColor().setText("");
            vista.getCbSexo().setSelectedIndex(0);
        } finally {
            cargando = false;
        }
        vista.getBtnGuardar().setEnabled(true);
        vista.getBtnActualizar().setEnabled(false);
        vista.setTituloFormulario("Registrar Nueva Mascota");
    }

    // ---------- Guardar y actualizar ----------

    private void guardar() {
        if (vista.getTabla().getSelectedRow() >= 0) {
            JOptionPane.showMessageDialog(vista,
                    "Esa mascota ya existe. Use Actualizar para cambiarla o Nuevo para registrar otra.");
            return;
        }
        if (!validar(0)) {
            return;
        }
        try {
            Especie especie = new Especie(0, texto(vista.getCbEspecie()));
            Raza raza = new Raza(0, especie, texto(vista.getCbRaza()));
            Animal nueva = new Animal(0, propietario, raza, vista.getTxtNombre().getText().trim(),
                    fechaNacimiento(), sexo(), peso(), vista.getTxtColor().getText().trim(),
                    Animal.EstadoAnimal.ACTIVO, LocalDate.now(), LocalTime.now().withNano(0));
            animalRepo.registrar(nueva);
            JOptionPane.showMessageDialog(vista, "¡Mascota registrada correctamente!");
            cargarTabla();
            modoNuevo();
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "Error al guardar la mascota: " + ex.getMessage());
        }
    }

    private void actualizar() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila < 0 || fila >= animales.size()) {
            JOptionPane.showMessageDialog(vista, "Seleccione una mascota de la tabla.");
            return;
        }
        Animal a = animales.get(fila);
        if (!validar(a.getIdAnimal())) {
            return;
        }
        try {
            a.setNombre(vista.getTxtNombre().getText().trim());
            a.setRaza(new Raza(0, new Especie(0, texto(vista.getCbEspecie())), texto(vista.getCbRaza())));
            a.setFechaNacimiento(fechaNacimiento());
            a.setSexo(sexo());
            a.setPeso(peso());
            a.setColor(vista.getTxtColor().getText().trim());

            if (animalRepo.actualizar(a)) {
                JOptionPane.showMessageDialog(vista, "¡Mascota actualizada correctamente!");
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró la mascota en la base de datos.");
            }
        } catch (RepositorioException ex) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar la mascota: " + ex.getMessage());
        }
        cargarTabla();
        modoNuevo();
    }

    // ---------- Validaciones y utilidades ----------

    /** idIgnorar: id de la mascota que se esta editando (0 si es nueva), para no compararla consigo misma. */
    private boolean validar(int idIgnorar) {
        String nombre = vista.getTxtNombre().getText().trim();
        String especie = texto(vista.getCbEspecie());
        String raza = texto(vista.getCbRaza());

        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || vista.getTxtColor().getText().trim().isEmpty()) {
            aviso("Complete nombre, especie, raza y color.");
            return false;
        }
        if (peso().compareTo(BigDecimal.ZERO) <= 0) {
            aviso("El peso debe ser mayor que 0.");
            return false;
        }
        if (fechaNacimiento().isAfter(LocalDate.now())) {
            aviso("La fecha de nacimiento no puede ser futura.");
            return false;
        }
        for (Animal a : animales) {
            if (a.getIdAnimal() != idIgnorar
                    && a.getNombre().equalsIgnoreCase(nombre)
                    && a.getRaza().getEspecie().getnombreEspecie().equalsIgnoreCase(especie)
                    && a.getRaza().getNombre().equalsIgnoreCase(raza)) {
                aviso("Ya tiene registrada una mascota con ese nombre, especie y raza.");
                return false;
            }
        }
        return true;
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Datos no válidos", JOptionPane.WARNING_MESSAGE);
    }

    /** Texto actual de un combo editable, aunque el usuario no haya presionado Enter. */
    private String texto(JComboBox<String> combo) {
        Object item = combo.getEditor().getItem();
        return item == null ? "" : item.toString().trim();
    }

    private LocalDate fechaNacimiento() {
        Date d = (Date) vista.getSpFechaNacimiento().getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date aDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private BigDecimal peso() {
        double valor = ((Number) vista.getSpPeso().getValue()).doubleValue();
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private Animal.Sexo sexo() {
        return Animal.Sexo.valueOf(vista.getCbSexo().getSelectedItem().toString().toUpperCase(Locale.ROOT));
    }

    private String capitalizar(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase(Locale.ROOT);
    }
}
