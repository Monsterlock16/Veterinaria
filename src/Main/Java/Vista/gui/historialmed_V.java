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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Modelo.repositorio.ConexionBD;

public class historialmed_V extends JFrame {

    private final JComboBox<String> cbAnimales = new JComboBox<>();
    private final HashMap<String, Integer> mapaAnimales = new HashMap<>();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Fecha", "Veterinario Atendió", "Síntomas", "Diagnóstico", "Tratamiento", "Observaciones"}, 0);
    private final JTable tabla = new JTable(modelo);

    public historialmed_V() {
        setTitle("Historial Clínico de Mascotas");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Seleccionar Mascota:"));
        top.add(cbAnimales);

        JButton btnBuscar = new JButton("Ver Expediente");
        btnBuscar.addActionListener(e -> cargarExpediente());
        top.add(btnBuscar);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarAnimalesCombo();
    }

    private void cargarAnimalesCombo() {
        cbAnimales.removeAllItems();
        mapaAnimales.clear();

        String sql = "SELECT idANIMAL, nombre FROM animal";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String item = rs.getString("nombre") + " (ID: " + rs.getInt("idANIMAL") + ")";
                cbAnimales.addItem(item);
                mapaAnimales.put(item, rs.getInt("idANIMAL"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar lista de animales: " + ex.getMessage());
        }
    }

    private void cargarExpediente() {
        if (cbAnimales.getSelectedItem() == null) return;
        modelo.setRowCount(0);
        int idAnimal = mapaAnimales.get(cbAnimales.getSelectedItem().toString());

        String sql = "SELECT co.fecha_consulta, p.nombres AS vet_nombre, p.apellidos AS vet_apellido, "
                   + "co.sintomas, co.diagnostico, co.tratamiento, co.observaciones "
                   + "FROM consulta co "
                   + "JOIN historial_medico hm ON hm.idHISTORIAL_MEDICO = co.HISTORIAL_MEDICO_idHISTORIAL_MEDICO "
                   + "JOIN veterinario v ON v.idVETERINARIO = co.VETERINARIO_idVETERINARIO "
                   + "JOIN persona p ON p.idPERSONA = v.PERSONA_idPERSONA "
                   + "WHERE hm.ANIMAL_idANIMAL = ? "
                   + "ORDER BY co.fecha_consulta DESC";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAnimal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String vetNombre = rs.getString("vet_nombre") + " " + rs.getString("vet_apellido");
                modelo.addRow(new Object[]{
                    rs.getDate("fecha_consulta"), vetNombre, rs.getString("sintomas"),
                    rs.getString("diagnostico"), rs.getString("tratamiento"), rs.getString("observaciones")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar expediente: " + ex.getMessage());
        }
    }
}