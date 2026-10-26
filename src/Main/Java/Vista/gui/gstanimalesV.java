package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Controlador.GstanimalesPresenter;

/**
 * Vista clinica del veterinario: muestra todos los animales y solo permite
 * modificar el peso y el estado de salud. La logica esta en GstanimalesPresenter.
 */
public class gstanimalesV extends JFrame {
    // Datos de identificacion: solo se muestran
    private final JTextField txtPropietario = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtEspecie = new JTextField();
    private final JTextField txtRaza = new JTextField();
    private final JTextField txtColor = new JTextField();
    private final JTextField txtFechaNacimiento = new JTextField();
    private final JTextField txtSexo = new JTextField();
    // Datos clinicos: estos si se pueden modificar
    private final JSpinner spPeso = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
    private final JComboBox<String> cbEstadoSalud = new JComboBox<>(new String[]{"Activo", "Inactivo"});

    private final JTextField txtBuscar = new JTextField(20);

    // La tabla es solo de consulta: las celdas no se editan con doble clic
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
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);

    private final JButton btnActualizar = crearBoton("Actualizar", new Color(0, 122, 116), Color.WHITE);
    private final JButton btnLimpiar = crearBoton("Limpiar", new Color(220, 225, 230), TemaVeterinaria.TEXTO);

    public gstanimalesV() {
        TemaVeterinaria.aplicarIcono(this);
        setTitle("Gestión de animales - Veterinario");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelPrincipal = (JPanel) getContentPane();
        panelPrincipal.setBackground(TemaVeterinaria.FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(new GridLayout(5, 4, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)),
                    "Datos del animal (solo se modifican el peso y el estado de salud)"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        formulario.add(new JLabel("Propietario:")); formulario.add(txtPropietario);
        formulario.add(new JLabel("Nombre del animal:")); formulario.add(txtNombre);
        formulario.add(new JLabel("Especie:")); formulario.add(txtEspecie);
        formulario.add(new JLabel("Raza:")); formulario.add(txtRaza);
        formulario.add(new JLabel("Peso (kg):")); formulario.add(spPeso);
        formulario.add(new JLabel("Color:")); formulario.add(txtColor);
        formulario.add(new JLabel("Fecha de nacimiento:")); formulario.add(txtFechaNacimiento);
        formulario.add(new JLabel("Sexo:")); formulario.add(txtSexo);
        formulario.add(new JLabel("Estado de salud:")); formulario.add(cbEstadoSalud);
        formulario.add(new JLabel()); formulario.add(new JLabel());

        for (JTextField campo : new JTextField[]{txtPropietario, txtNombre, txtEspecie, txtRaza,
                txtColor, txtFechaNacimiento, txtSexo}) {
            campo.setEditable(false);
        }

        // Busqueda y botones junto al formulario (el boton "Volver al menu" ocupa la parte de abajo)
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        barra.setOpaque(false);
        barra.add(new JLabel("Buscar (propietario o animal):"));
        barra.add(txtBuscar);
        barra.add(btnActualizar);
        barra.add(btnLimpiar);

        JPanel arriba = new JPanel(new BorderLayout(0, 5));
        arriba.setOpaque(false);
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(barra, BorderLayout.SOUTH);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowSorter(sorter);

        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        TemaVeterinaria.aplicarEstiloVentana(this);
        new GstanimalesPresenter(this);
    }

    private static JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    public JTextField getTxtPropietario() { return txtPropietario; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtEspecie() { return txtEspecie; }
    public JTextField getTxtRaza() { return txtRaza; }
    public JTextField getTxtColor() { return txtColor; }
    public JTextField getTxtFechaNacimiento() { return txtFechaNacimiento; }
    public JTextField getTxtSexo() { return txtSexo; }
    public JSpinner getSpPeso() { return spPeso; }
    public JComboBox<String> getCbEstadoSalud() { return cbEstadoSalud; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public DefaultTableModel getModelo() { return modelo; }
    public JTable getTabla() { return tabla; }
    public TableRowSorter<DefaultTableModel> getSorter() { return sorter; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new gstanimalesV().setVisible(true));
    }
}
