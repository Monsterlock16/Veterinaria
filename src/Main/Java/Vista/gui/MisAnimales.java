package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.MisAnimalesPresenter;

/** Vista de las mascotas del propietario: solo dibuja; la logica esta en MisAnimalesPresenter. */
public class MisAnimales extends JFrame {
    private final JTextField txtNombre = new JTextField();
    private final JComboBox<String> cbEspecie = new JComboBox<>();
    private final JComboBox<String> cbRaza = new JComboBox<>();
    private final JSpinner spFechaNacimiento =
            new JSpinner(new SpinnerDateModel(new Date(), null, new Date(), Calendar.DAY_OF_MONTH));
    private final JSpinner spPeso = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
    private final JTextField txtColor = new JTextField();
    private final JComboBox<String> cbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});

    // La tabla es solo de consulta: las celdas no se editan con doble clic
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Especie", "Raza", "Fecha de nacimiento", "Peso", "Color", "Sexo"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JButton btnGuardar = new JButton("Guardar Mascota");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnNuevo = new JButton("Nuevo");

    private final TitledBorder bordeFormulario = BorderFactory.createTitledBorder("Registrar Nueva Mascota");
    private final JPanel formulario = new JPanel(new GridLayout(4, 4, 10, 10));
    private final DatosPropietario datos;

    public MisAnimales(DatosPropietario datos) {
        TemaVeterinaria.aplicarIcono(this);
        this.datos = datos;

        setTitle("Mis Mascotas - Propietario");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        cbEspecie.setEditable(true);
        cbRaza.setEditable(true);
        spFechaNacimiento.setEditor(new JSpinner.DateEditor(spFechaNacimiento, "yyyy-MM-dd"));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        formulario.setBorder(BorderFactory.createCompoundBorder(
                bordeFormulario, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        formulario.add(new JLabel("Nombre:")); formulario.add(txtNombre);
        formulario.add(new JLabel("Especie:")); formulario.add(cbEspecie);
        formulario.add(new JLabel("Raza:")); formulario.add(cbRaza);
        formulario.add(new JLabel("Fecha de nacimiento:")); formulario.add(spFechaNacimiento);
        formulario.add(new JLabel("Peso (kg):")); formulario.add(spPeso);
        formulario.add(new JLabel("Color:")); formulario.add(txtColor);
        formulario.add(new JLabel("Sexo:")); formulario.add(cbSexo);
        formulario.add(new JLabel()); formulario.add(new JLabel());

        btnGuardar.setBackground(new Color(0, 102, 153));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        // Los botones van junto al formulario (arriba): el boton "Volver al menu" ocupa la parte de abajo
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnNuevo);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(botones, BorderLayout.SOUTH);

        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        TemaVeterinaria.aplicarEstiloVentana(this);
        new MisAnimalesPresenter(this);
    }

    public void setTituloFormulario(String titulo) {
        bordeFormulario.setTitle(titulo);
        formulario.repaint();
    }

    public DatosPropietario getDatos() { return datos; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JComboBox<String> getCbEspecie() { return cbEspecie; }
    public JComboBox<String> getCbRaza() { return cbRaza; }
    public JSpinner getSpFechaNacimiento() { return spFechaNacimiento; }
    public JSpinner getSpPeso() { return spPeso; }
    public JTextField getTxtColor() { return txtColor; }
    public JComboBox<String> getCbSexo() { return cbSexo; }
    public DefaultTableModel getModelo() { return modelo; }
    public JTable getTabla() { return tabla; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnNuevo() { return btnNuevo; }
}
