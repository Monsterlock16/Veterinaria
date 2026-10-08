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

public class MiHistorialMedico extends JFrame {
    private final JComboBox<String> cbMascotas = new JComboBox<>();
    private final HashMap<String, Integer> mapaMascotas = new HashMap<>();
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"Fecha", "Síntomas", "Diagnóstico", "Tratamiento", "Observaciones"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final DatosPropietario datos;

    public MiHistorialMedico(DatosPropietario datos) {
        this.datos = datos;

        setTitle("Historial Médico de mis Mascotas");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Seleccionar Mascota:"));
        top.add(cbMascotas);

        JButton btnBuscar = new JButton("Buscar Historial");
        btnBuscar.addActionListener(e -> cargarHistorial());
        top.add(btnBuscar);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarMascotasCombo();
    }

    private void cargarMascotasCombo() {
        cbMascotas.removeAllItems();
        mapaMascotas.clear();

        int idProp = obtenerIdPropietarioActual();
        if (idProp == 0) return;

        String sql = "SELECT idANIMAL, nombre FROM animal WHERE PROPIETARIO_idPROPIETARIO = ?";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProp);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String item = rs.getString("nombre") + " (ID: " + rs.getInt("idANIMAL") + ")";
                cbMascotas.addItem(item);
                mapaMascotas.put(item, rs.getInt("idANIMAL"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar mascotas: " + ex.getMessage());
        }
    }

    private void cargarHistorial() {
        if (cbMascotas.getSelectedItem() == null) return;
        modelo.setRowCount(0);
        int idAnimal = mapaMascotas.get(cbMascotas.getSelectedItem().toString());

        String sql = "SELECT co.fecha_consulta, co.sintomas, co.diagnostico, co.tratamiento, co.observaciones "
                   + "FROM consulta co "
                   + "JOIN historial_medico hm ON hm.idHISTORIAL_MEDICO = co.HISTORIAL_MEDICO_idHISTORIAL_MEDICO "
                   + "WHERE hm.ANIMAL_idANIMAL = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAnimal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getDate("fecha_consulta"), rs.getString("sintomas"),
                    rs.getString("diagnostico"), rs.getString("tratamiento"), rs.getString("observaciones")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
        }
    }

    private int obtenerIdPropietarioActual() {
    String sql = "SELECT pr.idPROPIETARIO "
               + "FROM propietario pr "
               + "JOIN usuario u ON u.PERSONA_idPERSONA = pr.PERSONA_idPERSONA "
               + "ORDER BY pr.idPROPIETARIO DESC LIMIT 1";

    try (Connection cn = ConexionBD.obtener();
         PreparedStatement ps = cn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        if (rs.next()) {
            return rs.getInt("idPROPIETARIO");
        }
    } catch (SQLException ignored) {}
    return 1;
    }
}