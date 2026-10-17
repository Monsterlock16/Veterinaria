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
import Modelo.repositorio.RepositorioException;

public class MisCitas extends JFrame {
    // La tabla es solo de consulta: las celdas no se pueden editar con doble clic
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID Cita", "Mascota", "Fecha", "Hora", "Motivo", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final DatosPropietario datos;

    public MisCitas(DatosPropietario datos) {
        TemaVeterinaria.aplicarIcono(this);
        this.datos = datos;

        setTitle("Mis Citas");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        TemaVeterinaria.aplicarEstiloVentana(this);
        cargarCitas();
    }

    // Muestra solo las citas de las mascotas del propietario que inicio sesion
    private void cargarCitas() {
        modelo.setRowCount(0);

        String sql = "SELECT c.idCITA, a.nombre, c.fecha, c.hora, c.motivo, c.estado "
                   + "FROM cita c "
                   + "JOIN animal a ON a.idANIMAL = c.ANIMAL_idANIMAL "
                   + "JOIN propietario pr ON pr.idPROPIETARIO = a.PROPIETARIO_idPROPIETARIO "
                   + "JOIN usuario u ON u.PERSONA_idPERSONA = pr.PERSONA_idPERSONA "
                   + "WHERE u.usuario = ? "
                   + "ORDER BY c.fecha DESC, c.hora DESC";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, datos.getNombre());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("idCITA"), rs.getString("nombre"), rs.getDate("fecha"),
                        rs.getTime("hora"), rs.getString("motivo"), rs.getString("estado")
                    });
                }
            }
        } catch (SQLException | RepositorioException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar citas: " + ex.getMessage());
        }
    }
}