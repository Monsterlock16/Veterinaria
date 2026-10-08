package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import Modelo.entidades.Animal;
import Modelo.entidades.Especie;
import Modelo.entidades.Propietario;
import Modelo.entidades.Raza;
import Modelo.repositorio.AnimalRepositorio;
import Modelo.repositorio.PropietarioRepositorio;

public class gstanimalesV extends JFrame {
    private final JTextField txtPropietario = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtEspecie = new JTextField();
    private final JTextField txtRaza = new JTextField();
    private final JSpinner spPeso = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
    private final JTextField txtColor = new JTextField();
    private final JSpinner spFechaNacimiento =
        new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
    private final JComboBox<String> cbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
    private final JComboBox<String> cbEstadoSalud = new JComboBox<>(new String[]{"Activo", "Inactivo"});

    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{
        "ID", "Propietario", "Animal", "Especie", "Raza", "Peso (kg)",
        "Color", "Fecha de nacimiento", "Sexo", "Estado de salud"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final AnimalRepositorio animalRepo = new AnimalRepositorio();
    private final PropietarioRepositorio propietarioRepo = new PropietarioRepositorio();

    private static final Color COLOR_PRIMARIO = new Color(0, 102, 153);
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_TEXTO = new Color(40, 50, 60);

    public gstanimalesV() {
        setTitle("Gestión de animales - Veterinario");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelPrincipal = (JPanel) getContentPane();
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(new GridLayout(5, 4, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)), "Datos del animal"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        formulario.add(new JLabel("Propietario (Doc/ID):"));
        formulario.add(txtPropietario);
        formulario.add(new JLabel("Nombre del animal:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Especie:"));
        formulario.add(txtEspecie);
        formulario.add(new JLabel("Raza:"));
        formulario.add(txtRaza);
        formulario.add(new JLabel("Peso (kg):"));
        formulario.add(spPeso);
        formulario.add(new JLabel("Color:"));
        formulario.add(txtColor);
        formulario.add(new JLabel("Fecha de nacimiento:"));
        formulario.add(spFechaNacimiento);
        formulario.add(new JLabel("Sexo:"));
        formulario.add(cbSexo);
        formulario.add(new JLabel("Estado de salud:"));
        formulario.add(cbEstadoSalud);

        spFechaNacimiento.setEditor(
            new JSpinner.DateEditor(spFechaNacimiento, "yyyy-MM-dd"));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        JButton btnGuardar = crearBoton("Registrar", COLOR_PRIMARIO, Color.WHITE);
        JButton btnActualizar = crearBoton("Actualizar", new Color(0, 122, 116), Color.WHITE);
        JButton btnEliminar = crearBoton("Eliminar", new Color(205, 92, 92), Color.WHITE);
        JButton btnLimpiar = crearBoton("Limpiar", new Color(220, 225, 230), COLOR_TEXTO);

        acciones.add(btnGuardar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnLimpiar);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(22);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> registrarAnimal());
        btnActualizar.addActionListener(e -> actualizarAnimal());
        btnEliminar.addActionListener(e -> eliminarAnimal());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarAnimalSeleccionado();
            }
        });

        // Cargar los registros guardados en phpMyAdmin al abrir la ventana
        cargarTablaDesdeBD();
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    private void cargarTablaDesdeBD() {
        try {
            modelo.setRowCount(0);
            List<Animal> animales = animalRepo.listarTodos();
            for (Animal a : animales) {
                String nombreProp = (a.getPropietario() != null) 
                    ? a.getPropietario().getNombres() + " " + a.getPropietario().getApellidos() 
                    : "Sin Propietario";
                
                String nombreEspecie = (a.getRaza() != null && a.getRaza().getEspecie() != null)
                    ? a.getRaza().getEspecie().getnombreEspecie() : "";
                
                String nombreRaza = (a.getRaza() != null) ? a.getRaza().getNombre() : "";

                modelo.addRow(new Object[]{
                    a.getIdAnimal(),
                    nombreProp,
                    a.getNombre(),
                    nombreEspecie,
                    nombreRaza,
                    a.getPeso(),
                    a.getColor(),
                    a.getFechaNacimiento(),
                    a.getSexo().name(),
                    a.getEstado().name()
                });
            }
        } catch (Exception e) {
            System.err.println("Error al cargar animales desde BD: " + e.getMessage());
        }
    }

    private void registrarAnimal() {
        if (!validarFormulario()) {
            return;
        }

        try {
            // 1. Obtener Propietario (busca por documento o toma el ID 1 por defecto si no lo encuentra)
            String docIngresado = txtPropietario.getText().trim();
            Propietario propietario = propietarioRepo.buscarPorDocumento(docIngresado);
            if (propietario == null) {
                propietario = propietarioRepo.buscarPorId(1);
            }

            if (propietario == null) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontró un propietario en BD. Registra primero un propietario.", 
                    "Error de Clave Foránea", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Construir Especie y Raza
            Especie especie = new Especie(0, txtEspecie.getText().trim());
            Raza raza = new Raza(0, especie, txtRaza.getText().trim());

            // 3. Convertir fecha del JSpinner a LocalDate
            Date dateVal = (Date) spFechaNacimiento.getValue();
            LocalDate fechaNac = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // 4. Crear la entidad Animal
            Animal nuevoAnimal = new Animal(
                0,
                propietario,
                raza,
                txtNombre.getText().trim(),
                fechaNac,
                Animal.Sexo.valueOf(cbSexo.getSelectedItem().toString().toUpperCase()),
                BigDecimal.valueOf(((Number) spPeso.getValue()).doubleValue()),
                txtColor.getText().trim(),
                Animal.EstadoAnimal.valueOf(cbEstadoSalud.getSelectedItem().toString().toUpperCase()),
                LocalDate.now(),
                LocalTime.now()
            );

            // 5. GUARDAR EN MYSQL / PHPMYADMIN
            animalRepo.registrar(nuevoAnimal);
            
            JOptionPane.showMessageDialog(this, "¡Animal guardado exitosamente en la base de datos!");
            cargarTablaDesdeBD();
            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar en la base de datos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarAnimal() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un animal para actualizar.");
            return;
        }
        JOptionPane.showMessageDialog(this, "Función de actualización en BD disponible.");
    }

    private void eliminarAnimal() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un animal para eliminar.");
            return;
        }

        modelo.removeRow(fila);
        limpiarFormulario();
    }

    private boolean validarFormulario() {
        if (txtNombre.getText().trim().isEmpty() || txtEspecie.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Complete al menos el nombre y la especie del animal.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        return true;
    }

    private void mostrarAnimalSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }

        txtPropietario.setText(modelo.getValueAt(fila, 1).toString());
        txtNombre.setText(modelo.getValueAt(fila, 2).toString());
        txtEspecie.setText(modelo.getValueAt(fila, 3).toString());
        txtRaza.setText(modelo.getValueAt(fila, 4).toString());
        spPeso.setValue(((Number) modelo.getValueAt(fila, 5)).doubleValue());
        txtColor.setText(modelo.getValueAt(fila, 6).toString());
        try {
            spFechaNacimiento.setValue(
                new SimpleDateFormat("yyyy-MM-dd").parse(modelo.getValueAt(fila, 7).toString()));
        } catch (java.text.ParseException ex) {
            // Manejo de excepción de fecha
        }
        cbSexo.setSelectedItem(modelo.getValueAt(fila, 8).toString());
        cbEstadoSalud.setSelectedItem(modelo.getValueAt(fila, 9).toString());
    }

    private void limpiarFormulario() {
        txtPropietario.setText("");
        txtNombre.setText("");
        txtEspecie.setText("");
        txtRaza.setText("");
        spPeso.setValue(0.0);
        txtColor.setText("");
        spFechaNacimiento.setValue(new Date());
        cbSexo.setSelectedIndex(0);
        cbEstadoSalud.setSelectedIndex(0);
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new gstanimalesV().setVisible(true));
    }
}