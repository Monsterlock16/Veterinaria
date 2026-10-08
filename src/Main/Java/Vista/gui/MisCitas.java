package Vista.gui;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Modelo.repositorio.ConexionBD;

public class MisCitas extends JFrame {
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID Cita", "Mascota", "Fecha", "Hora", "Motivo", "Estado"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final DatosPropietario datos;

    public MisCitas(DatosPropietario datos) {
        this.datos = datos;

        setTitle("Mis Citas Programadas");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        cargarCitas();
    }

    private void cargarCitas() {
        modelo.setRowCount(0);
        int idProp = obtenerIdPropietarioActual();
        if (idProp == 0) return;

        String sql = "SELECT c.idCITA, a.nombre, c.fecha, c.hora, c.motivo, c.estado "
                   + "FROM cita c "
                   + "JOIN animal a ON a.idANIMAL = c.ANIMAL_idANIMAL "
                   + "WHERE a.PROPIETARIO_idPROPIETARIO = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProp);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idCITA"), rs.getString("nombre"), rs.getDate("fecha"),
                    rs.getTime("hora"), rs.getString("motivo"), rs.getString("estado")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar citas: " + ex.getMessage());
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