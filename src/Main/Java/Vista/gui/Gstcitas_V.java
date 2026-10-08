package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Modelo.repositorio.ConexionBD;

public class Gstcitas_V extends JFrame {

    private final JComboBox<String> cbAnimales = new JComboBox<>();
    private final JComboBox<String> cbVeterinarios = new JComboBox<>();
    private final HashMap<String, Integer> mapaAnimales = new HashMap<>();
    private final HashMap<String, Integer> mapaVeterinarios = new HashMap<>();

    private final JTextField txtFecha = new JTextField(LocalDate.now().toString());
    private final JTextField txtHora = new JTextField("10:00");
    private final JTextField txtMotivo = new JTextField();
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"PROGRAMADA", "COMPLETADA", "CANCELADA"});

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID Cita", "Mascota", "Veterinario", "Fecha", "Hora", "Motivo", "Estado"}, 0);
    private final JTable tabla = new JTable(modelo);

    public Gstcitas_V() {
        setTitle("Gestión de Citas - Veterinario");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel del formulario con GridBagLayout para 2 columnas perfectas
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Agendar Nueva Cita"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Animal y Veterinario
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        form.add(new JLabel("Seleccionar Animal:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        form.add(cbAnimales, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        form.add(new JLabel("Seleccionar Veterinario:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        form.add(cbVeterinarios, gbc);

        // Fila 1: Fecha y Hora
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        form.add(new JLabel("Fecha Cita:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        form.add(txtFecha, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        form.add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        form.add(txtHora, gbc);

        // Fila 2: Motivo y Estado
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        form.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        form.add(txtMotivo, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        form.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        form.add(cbEstado, gbc);

        JButton btnAgendar = new JButton("Agendar Cita");
        btnAgendar.setBackground(new Color(0, 102, 153));
        btnAgendar.setForeground(Color.WHITE);
        btnAgendar.addActionListener(e -> agendarCitaBD());

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(form, BorderLayout.CENTER);
        top.add(btnAgendar, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarCombosBD();
        cargarCitasBD();
    }

    private void cargarCombosBD() {
        cbAnimales.removeAllItems();
        mapaAnimales.clear();

        cbVeterinarios.removeAllItems();
        mapaVeterinarios.clear();

        // 1. Cargar Mascotas
        String sqlAnimales = "SELECT idANIMAL, nombre FROM animal";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sqlAnimales);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String item = rs.getString("nombre") + " (ID: " + rs.getInt("idANIMAL") + ")";
                cbAnimales.addItem(item);
                mapaAnimales.put(item, rs.getInt("idANIMAL"));
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar animales: " + ex.getMessage());
        }

        // 2. Cargar Veterinarios
        String sqlVets = "SELECT v.idVETERINARIO, p.nombres, p.apellidos "
                       + "FROM veterinario v "
                       + "JOIN persona p ON p.idPERSONA = v.PERSONA_idPERSONA";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sqlVets);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellidos");
                String item = nombreCompleto + " (ID: " + rs.getInt("idVETERINARIO") + ")";
                cbVeterinarios.addItem(item);
                mapaVeterinarios.put(item, rs.getInt("idVETERINARIO"));
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar veterinarios: " + ex.getMessage());
        }
    }

    private void agendarCitaBD() {
        if (cbAnimales.getSelectedItem() == null || cbVeterinarios.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un animal y un veterinario válidos.");
            return;
        }

        if (txtMotivo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba el motivo de la cita.");
            return;
        }

        int idAnimal = mapaAnimales.get(cbAnimales.getSelectedItem().toString());
        int idVet = mapaVeterinarios.get(cbVeterinarios.getSelectedItem().toString());

        String sql = "INSERT INTO cita (ANIMAL_idANIMAL, VETERINARIO_idVETERINARIO, fecha_cita, hora_cita, motivo, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idAnimal);
            ps.setInt(2, idVet);
            ps.setObject(3, LocalDate.parse(txtFecha.getText().trim()));
            ps.setObject(4, LocalTime.parse(txtHora.getText().trim()));
            ps.setString(5, txtMotivo.getText().trim());
            ps.setString(6, cbEstado.getSelectedItem().toString());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "¡Cita agendada exitosamente!");
            cargarCitasBD();
            txtMotivo.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agendar cita: " + ex.getMessage());
        }
    }

    private void cargarCitasBD() {
        modelo.setRowCount(0);
        String sql = "SELECT c.idCITA, a.nombre AS mascota, p.nombres AS vet_nombre, p.apellidos AS vet_apellido, "
                   + "c.fecha_cita, c.hora_cita, c.motivo, c.estado "
                   + "FROM cita c "
                   + "JOIN animal a ON a.idANIMAL = c.ANIMAL_idANIMAL "
                   + "JOIN veterinario v ON v.idVETERINARIO = c.VETERINARIO_idVETERINARIO "
                   + "JOIN persona p ON p.idPERSONA = v.PERSONA_idPERSONA "
                   + "ORDER BY c.idCITA DESC";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String vetNombre = rs.getString("vet_nombre") + " " + rs.getString("vet_apellido");
                modelo.addRow(new Object[]{
                    rs.getInt("idCITA"),
                    rs.getString("mascota"),
                    vetNombre,
                    rs.getDate("fecha_cita"),
                    rs.getTime("hora_cita"),
                    rs.getString("motivo"),
                    rs.getString("estado")
                });
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar lista de citas: " + ex.getMessage());
        }
    }
}