package Controlador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;

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

public class Gstconsultas extends JFrame {

    private final JComboBox<String> cbAnimales = new JComboBox<>();
    private final HashMap<String, Integer> mapaAnimales = new HashMap<>();
    private final JTextField txtSintomas = new JTextField();
    private final JTextField txtDiagnostico = new JTextField();
    private final JTextField txtTratamiento = new JTextField();
    private final JTextField txtObservaciones = new JTextField();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID Consulta", "Animal", "Fecha", "Síntomas", "Diagnóstico", "Tratamiento"}, 0);
    private final JTable tabla = new JTable(modelo);

    private final String usuarioVet;

    // Constructor sin parámetros para compatibilidad con VeterinarioPresenter
    public Gstconsultas() {
        this("veterinario");
    }

    // Constructor con el parámetro del usuario autenticado
    public Gstconsultas(String usuarioVet) {
        this.usuarioVet = usuarioVet;

        setTitle("Gestión de Consultas Médicas - Veterinario (" + usuarioVet + ")");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.add(new JLabel("Seleccionar Animal:")); form.add(cbAnimales);
        form.add(new JLabel("Síntomas:")); form.add(txtSintomas);
        form.add(new JLabel("Diagnóstico:")); form.add(txtDiagnostico);
        form.add(new JLabel("Tratamiento:")); form.add(txtTratamiento);
        form.add(new JLabel("Observaciones:")); form.add(txtObservaciones);

        JButton btnRegistrar = new JButton("Registrar Consulta");
        btnRegistrar.setBackground(new Color(0, 102, 153));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener(e -> guardarConsultaBD());

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnRegistrar, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarAnimalesCombo();
        cargarConsultasBD();
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

    private void guardarConsultaBD() {
        if (cbAnimales.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un animal.");
            return;
        }

        int idAnimal = mapaAnimales.get(cbAnimales.getSelectedItem().toString());
        int idVet = obtenerIdVeterinarioActual();

        if (idVet == 0) {
            JOptionPane.showMessageDialog(this, "No se encontró el registro del veterinario actual en la BD.");
            return;
        }

        int idHistorial = obtenerOCrearHistorial(idAnimal);

        String sql = "INSERT INTO consulta (HISTORIAL_MEDICO_idHISTORIAL_MEDICO, VETERINARIO_idVETERINARIO, fecha_consulta, sintomas, diagnostico, tratamiento, observaciones) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idHistorial);
            ps.setInt(2, idVet);
            ps.setObject(3, LocalDate.now());
            ps.setString(4, txtSintomas.getText().trim());
            ps.setString(5, txtDiagnostico.getText().trim());
            ps.setString(6, txtTratamiento.getText().trim());
            ps.setString(7, txtObservaciones.getText().trim());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "¡Consulta guardada exitosamente!");
            cargarConsultasBD();
            limpiar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar la consulta: " + ex.getMessage());
        }
    }

    private int obtenerOCrearHistorial(int idAnimal) {
        String sqlSelect = "SELECT idHISTORIAL_MEDICO FROM historial_medico WHERE ANIMAL_idANIMAL = ?";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sqlSelect)) {
            ps.setInt(1, idAnimal);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idHISTORIAL_MEDICO");
            }
        } catch (SQLException ignored) {}

        String sqlInsert = "INSERT INTO historial_medico (ANIMAL_idANIMAL, fecha_creacion) VALUES (?, ?)";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idAnimal);
            ps.setObject(2, LocalDate.now());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ignored) {}

        return 1;
    }

    private int obtenerIdVeterinarioActual() {
        String sql = "SELECT v.idVETERINARIO "
                   + "FROM veterinario v "
                   + "JOIN usuario u ON u.PERSONA_idPERSONA = v.PERSONA_idPERSONA "
                   + "WHERE u.usuario = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuarioVet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idVETERINARIO");
            }
        } catch (SQLException ignored) {}

        // Busca el último veterinario registrado si no encuentra por nombre de usuario
        String sqlFallback = "SELECT idVETERINARIO FROM veterinario ORDER BY idVETERINARIO DESC LIMIT 1";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sqlFallback);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("idVETERINARIO");
            }
        } catch (SQLException ignored) {}

        return 1;
    }

    private void cargarConsultasBD() {
        modelo.setRowCount(0);
        String sql = "SELECT c.idCONSULTA, a.nombre, c.fecha_consulta, c.sintomas, c.diagnostico, c.tratamiento "
                   + "FROM consulta c "
                   + "JOIN historial_medico hm ON hm.idHISTORIAL_MEDICO = c.HISTORIAL_MEDICO_idHISTORIAL_MEDICO "
                   + "JOIN animal a ON a.idANIMAL = hm.ANIMAL_idANIMAL "
                   + "ORDER BY c.idCONSULTA DESC";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idCONSULTA"), rs.getString("nombre"), rs.getDate("fecha_consulta"),
                    rs.getString("sintomas"), rs.getString("diagnostico"), rs.getString("tratamiento")
                });
            }
        } catch (SQLException ex) {
            System.err.println("Error al cargar consultas: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtSintomas.setText("");
        txtDiagnostico.setText("");
        txtTratamiento.setText("");
        txtObservaciones.setText("");
    }
}