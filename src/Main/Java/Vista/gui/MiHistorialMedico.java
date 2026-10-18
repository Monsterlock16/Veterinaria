package Vista.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Modelo.repositorio.ConexionBD;
import Modelo.repositorio.RepositorioException;

public class MiHistorialMedico extends JFrame {
    private final JComboBox<String> cbMascotas = new JComboBox<>();
    private final HashMap<String, Integer> mapaMascotas = new HashMap<>();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Fecha", "Veterinario Atendió", "Síntomas", "Diagnóstico", "Tratamiento", "Observaciones"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final JLabel lblResumen = new JLabel(" ");
    private final DatosPropietario datos;

    public MiHistorialMedico(DatosPropietario datos) {
        TemaVeterinaria.aplicarIcono(this);
        this.datos = datos;

        setTitle("Historial Médico de mis Mascotas");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Seleccionar Mascota:"));
        top.add(cbMascotas);

        JButton btnBuscar = new JButton("Buscar Historial");
        btnBuscar.addActionListener(e -> cargarHistorial());
        top.add(btnBuscar);
        top.add(lblResumen);

        add(top, BorderLayout.NORTH);
        add(TablaDetalle.crear(tabla), BorderLayout.CENTER);

        TemaVeterinaria.aplicarEstiloVentana(this);
        cargarMascotasCombo();
        // El historial se carga al elegir la mascota
        cbMascotas.addActionListener(e -> cargarHistorial());
        cargarHistorial();
    }

    // Solo las mascotas del propietario que inicio sesion
    private void cargarMascotasCombo() {
        cbMascotas.removeAllItems();
        mapaMascotas.clear();

        String sql = "SELECT a.idANIMAL, a.nombre FROM animal a "
                   + "JOIN propietario pr ON pr.idPROPIETARIO = a.PROPIETARIO_idPROPIETARIO "
                   + "JOIN usuario u ON u.PERSONA_idPERSONA = pr.PERSONA_idPERSONA "
                   + "WHERE u.usuario = ? ORDER BY a.nombre";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, datos.getNombre());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String item = rs.getString("nombre") + " (ID: " + rs.getInt("idANIMAL") + ")";
                    cbMascotas.addItem(item);
                    mapaMascotas.put(item, rs.getInt("idANIMAL"));
                }
            }
        } catch (SQLException | RepositorioException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar mascotas: " + ex.getMessage());
        }
    }

    private void cargarHistorial() {
        modelo.setRowCount(0);
        lblResumen.setText(" ");
        if (cbMascotas.getSelectedItem() == null) {
            lblResumen.setText("No tiene mascotas registradas.");
            return;
        }
        int idAnimal = mapaMascotas.get(cbMascotas.getSelectedItem().toString());

        String sql = "SELECT co.fecha_consulta, p.nombres AS vet_nombre, p.apellidos AS vet_apellido, "
                   + "co.sintomas, co.diagnostico, co.tratamiento, co.observaciones "
                   + "FROM consulta co "
                   + "JOIN historial_medico hm ON hm.idHISTORIAL_MEDICO = co.HISTORIAL_MEDICO_idHISTORIAL_MEDICO "
                   + "JOIN veterinario v ON v.idVETERINARIO = co.VETERINARIO_idVETERINARIO "
                   + "JOIN persona p ON p.idPERSONA = v.PERSONA_idPERSONA "
                   + "WHERE hm.ANIMAL_idANIMAL = ? "
                   + "ORDER BY co.fecha_consulta DESC, co.idCONSULTA DESC";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAnimal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getDate("fecha_consulta"),
                        rs.getString("vet_nombre") + " " + rs.getString("vet_apellido"),
                        rs.getString("sintomas"), rs.getString("diagnostico"),
                        rs.getString("tratamiento"), rs.getString("observaciones")
                    });
                }
            }
            lblResumen.setText(modelo.getRowCount() == 0
                    ? "Esta mascota aún no tiene consultas registradas."
                    : modelo.getRowCount() + " consulta(s) registradas.");
        } catch (SQLException | RepositorioException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
        }
    }
}
