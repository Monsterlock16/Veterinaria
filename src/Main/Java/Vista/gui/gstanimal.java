package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;

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

public class gstanimal extends JFrame {
    private int siguienteId = 1;
    private final JTextField txtNombre_propetario;
    private final JTextField txtNombre;
    private final JTextField txtEspecie;
    private final JTextField txtRaza;
    private final JSpinner txtPeso;
    private final JTextField txtColor;
    private final JSpinner spfechaNacimiento;
    private final JComboBox<String> txtSexo;
    private final JComboBox<String> txtEstado_salud;

    private final DefaultTableModel modelo;
    private final JTable tabla;

    public gstanimal() {
        setTitle("Gestión de Animales");
        setSize(760, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(12, 12));

        Color fondoGeneral = TemaVeterinaria.FONDO;
        Color fondoPanel = TemaVeterinaria.PANEL;
        Color fondoInputs = TemaVeterinaria.INPUT;
        Color principal = TemaVeterinaria.PRIMARIO;
        Color principalOscuro = TemaVeterinaria.PRIMARIO_OSCURO;
        Color acento = TemaVeterinaria.SECUNDARIO;
        Color texto = TemaVeterinaria.TEXTO;

        getContentPane().setBackground(fondoGeneral);

        JPanel formularioPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formularioPanel.setBackground(fondoPanel);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNombre_propetario = new JTextField();
        txtNombre = new JTextField();
        txtEspecie = new JTextField();
        txtRaza = new JTextField();
        txtPeso = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
        txtColor = new JTextField();
        txtSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        txtEstado_salud = new JComboBox<>(new String[]{"activo", "inactivop"});
        spfechaNacimiento = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spfechaNacimiento.setEditor(new JSpinner.DateEditor(spfechaNacimiento, "yyyy-MM-dd"));

        aplicarEstiloCampo(txtNombre_propetario, fondoInputs, texto);
        aplicarEstiloCampo(txtNombre, fondoInputs, texto);
        aplicarEstiloCampo(txtEspecie, fondoInputs, texto);
        aplicarEstiloCampo(txtRaza, fondoInputs, texto);
        aplicarEstiloCampo((JTextField) txtPeso.getEditor().getComponent(0), fondoInputs, texto);
        aplicarEstiloCampo(txtColor, fondoInputs, texto);
        aplicarEstiloCombo(txtSexo, fondoInputs, texto, principal);
        aplicarEstiloCombo(txtEstado_salud, fondoInputs, texto, principal);
        aplicarEstiloSpinner(spfechaNacimiento, fondoInputs, texto);

        JLabel lblNombrePropietario = new JLabel("Nombre del propietario:");
        JLabel lblNombreAnimal = new JLabel("Nombre del animal:");
        JLabel lblEspecie = new JLabel("Especie:");
        JLabel lblRaza = new JLabel("Raza:");
        JLabel lblPeso = new JLabel("Peso (kg):");
        JLabel lblColor = new JLabel("Color:");
        JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento:");
        JLabel lblSexo = new JLabel("Sexo:");
        JLabel lblEstadoSalud = new JLabel("Estado de salud:");

        estiloEtiqueta(lblNombrePropietario, texto);
        estiloEtiqueta(lblNombreAnimal, texto);
        estiloEtiqueta(lblEspecie, texto);
        estiloEtiqueta(lblRaza, texto);
        estiloEtiqueta(lblPeso, texto);
        estiloEtiqueta(lblColor, texto);
        estiloEtiqueta(lblFechaNacimiento, texto);
        estiloEtiqueta(lblSexo, texto);
        estiloEtiqueta(lblEstadoSalud, texto);

        formularioPanel.add(lblNombrePropietario);
        formularioPanel.add(txtNombre_propetario);
        formularioPanel.add(lblNombreAnimal);
        formularioPanel.add(txtNombre);
        formularioPanel.add(lblEspecie);
        formularioPanel.add(txtEspecie);
        formularioPanel.add(lblRaza);
        formularioPanel.add(txtRaza);
        formularioPanel.add(lblPeso);
        formularioPanel.add(txtPeso);
        formularioPanel.add(lblColor);
        formularioPanel.add(txtColor);
        formularioPanel.add(lblFechaNacimiento);
        formularioPanel.add(spfechaNacimiento);
        formularioPanel.add(lblSexo);
        formularioPanel.add(txtSexo);
        formularioPanel.add(lblEstadoSalud);
        formularioPanel.add(txtEstado_salud);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(fondoGeneral);
        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");

        estilizarBoton(btnGuardar, principal, Color.WHITE);
        estilizarBoton(btnLimpiar, acento, Color.WHITE);
        estilizarBoton(btnEliminar, new Color(190, 79, 79), Color.WHITE);
        estilizarBoton(btnActualizar, principalOscuro, Color.WHITE);

        botones.add(btnGuardar);
        botones.add(btnLimpiar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre del propietario", "Nombre del animal", "Especie", "Raza",
            "Peso (kg)", "Color", "Fecha de nacimiento", "Sexo", "Estado de salud"
        }, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setSelectionBackground(new Color(158, 214, 201));
        tabla.setSelectionForeground(texto);
        tabla.getTableHeader().setBackground(principal);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(formularioPanel, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> registrarAnimal());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarAnimal());
        btnActualizar.addActionListener(e -> actualizarAnimal());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarAnimalSeleccionado();
            }
        });

        TemaVeterinaria.aplicarIcono(this);
        TemaVeterinaria.aplicarEstiloVentana(this);
    }

    private void aplicarEstiloCampo(JTextField campo, Color fondo, Color texto) {
        campo.setBackground(fondo);
        campo.setForeground(texto);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 190), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private void aplicarEstiloSpinner(JSpinner spinner, Color fondo, Color texto) {
        spinner.setBackground(fondo);
        spinner.setForeground(texto);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 190), 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    private void aplicarEstiloCombo(JComboBox<?> combo, Color fondo, Color texto, Color borde) {
        combo.setBackground(fondo);
        combo.setForeground(texto);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBorder(BorderFactory.createLineBorder(borde, 1));
    }

    private void estiloEtiqueta(JLabel etiqueta, Color texto) {
        etiqueta.setForeground(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
    }

    private void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
    }

    private void registrarAnimal() {
        String nombrePropietario = txtNombre_propetario.getText().trim();
        String nombreAnimal = txtNombre.getText().trim();
        String especie = txtEspecie.getText().trim();
        String raza = txtRaza.getText().trim();
        double peso = (double) txtPeso.getValue();
        String color = txtColor.getText().trim();
        Date fechaNacimiento = (Date) spfechaNacimiento.getValue();
        String sexo = (String) txtSexo.getSelectedItem();
        String estadoSalud = (String) txtEstado_salud.getSelectedItem();

        if (nombrePropietario.isEmpty() || nombreAnimal.isEmpty() || especie.isEmpty() ||
            raza.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelo.addRow(new Object[]{
            siguienteId++, nombrePropietario, nombreAnimal, especie, raza,
            peso, color, fechaNacimiento, sexo, estadoSalud
        });

        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombre_propetario.setText("");
        txtNombre.setText("");
        txtEspecie.setText("");
        txtRaza.setText("");
        txtPeso.setValue(0.0);
        txtColor.setText("");
        spfechaNacimiento.setValue(new Date());
        txtSexo.setSelectedIndex(0);
        txtEstado_salud.setSelectedIndex(0);
    }

    private void eliminarAnimal() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            modelo.removeRow(filaSeleccionada);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un animal para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarAnimal() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            String nombrePropietario = txtNombre_propetario.getText().trim();
            String nombreAnimal = txtNombre.getText().trim();
            String especie = txtEspecie.getText().trim();
            String raza = txtRaza.getText().trim();
            double peso = (double) txtPeso.getValue();
            String color = txtColor.getText().trim();
            Date fechaNacimiento = (Date) spfechaNacimiento.getValue();
            String sexo = (String) txtSexo.getSelectedItem();
            String estadoSalud = (String) txtEstado_salud.getSelectedItem();

            if (nombrePropietario.isEmpty() || nombreAnimal.isEmpty() || especie.isEmpty() ||
                raza.isEmpty() || color.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            modelo.setValueAt(nombrePropietario, filaSeleccionada, 1);
            modelo.setValueAt(nombreAnimal, filaSeleccionada, 2);
            modelo.setValueAt(especie, filaSeleccionada, 3);
            modelo.setValueAt(raza, filaSeleccionada, 4);
            modelo.setValueAt(peso, filaSeleccionada, 5);
            modelo.setValueAt(color, filaSeleccionada, 6);
            modelo.setValueAt(fechaNacimiento, filaSeleccionada, 7);
            modelo.setValueAt(sexo, filaSeleccionada, 8);
            modelo.setValueAt(estadoSalud, filaSeleccionada, 9);

            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un animal para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarAnimalSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtNombre_propetario.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
            txtNombre.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
            txtEspecie.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
            txtRaza.setText(modelo.getValueAt(filaSeleccionada, 4).toString());
            double peso = Double.parseDouble(modelo.getValueAt(filaSeleccionada, 5).toString());
            txtPeso.setValue(peso);
            txtColor.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
            spfechaNacimiento.setValue((Date) modelo.getValueAt(filaSeleccionada, 7));
            txtSexo.setSelectedItem(modelo.getValueAt(filaSeleccionada, 8).toString());
            txtEstado_salud.setSelectedItem(modelo.getValueAt(filaSeleccionada, 9).toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new gstanimal().setVisible(true));
    }
}