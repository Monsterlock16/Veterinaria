package Vista.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Modelo.repositorio.ConexionBD;

public class SolicitarCita extends JFrame {
    private final JComboBox<String> cbMascotas = new JComboBox<>();
    private final HashMap<String, Integer> mapaMascotas = new HashMap<>();
    private final JTextField txtFecha = new JTextField(LocalDate.now().toString());
    private final JTextField txtHora = new JTextField("10:00");
    private final JTextField txtMotivo = new JTextField();
    private final DatosPropietario datos;

    public SolicitarCita(DatosPropietario datos) {
        this.datos = datos;

        setTitle("Solicitar Cita Médica");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Seleccionar Mascota:")); add(cbMascotas);
        add(new JLabel("Fecha (YYYY-MM-DD):")); add(txtFecha);
        add(new JLabel("Hora (HH:mm):")); add(txtHora);
        add(new JLabel("Motivo:")); add(txtMotivo);

        JButton btnSolicitar = new JButton("Agendar Cita");
        btnSolicitar.setBackground(new Color(0, 102, 153));
        btnSolicitar.setForeground(Color.WHITE);
        btnSolicitar.addActionListener(e -> agendarCitaBD());

        add(new JLabel()); add(btnSolicitar);

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

    private void agendarCitaBD() {
        if (cbMascotas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No tiene mascotas registradas para agendar una cita.");
            return;
        }

        int idVet = 0;
        try (Connection cn = ConexionBD.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT idVETERINARIO FROM veterinario LIMIT 1")) {
            if (rs.next()) {
                idVet = rs.getInt(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al verificar veterinarios: " + ex.getMessage());
            return;
        }

        if (idVet == 0) {
            JOptionPane.showMessageDialog(this, "No hay veterinarios disponibles en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idAnimalSeleccionado = mapaMascotas.get(cbMascotas.getSelectedItem().toString());

        String sql = "INSERT INTO cita (ANIMAL_idANIMAL, VETERINARIO_idVETERINARIO, fecha, hora, motivo, estado) "
                   + "VALUES (?, ?, ?, ?, ?, 'programada')";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAnimalSeleccionado);
            ps.setInt(2, idVet);
            ps.setObject(3, LocalDate.parse(txtFecha.getText().trim()));
            ps.setObject(4, LocalTime.parse(txtHora.getText().trim()));
            ps.setString(5, txtMotivo.getText().trim());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "¡Cita agendada con éxito!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agendar cita: " + ex.getMessage());
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