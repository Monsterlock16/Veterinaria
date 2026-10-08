package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import Modelo.repositorio.ConexionBD;

public class MisAnimales extends JFrame {
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtEspecie = new JTextField();
    private final JTextField txtRaza = new JTextField();
    private final JSpinner spPeso = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
    private final JTextField txtColor = new JTextField();
    private final JComboBox<String> cbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
    
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Especie", "Raza", "Peso", "Color", "Sexo"}, 0);
    private final JTable tabla = new JTable(modelo);
    private final DatosPropietario datos;

    public MisAnimales(DatosPropietario datos) {
        this.datos = datos;

        setTitle("Mis Mascotas - Propietario");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Registrar Nueva Mascota"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Especie:")); form.add(txtEspecie);
        form.add(new JLabel("Raza:")); form.add(txtRaza);
        form.add(new JLabel("Peso (kg):")); form.add(spPeso);
        form.add(new JLabel("Color:")); form.add(txtColor);
        form.add(new JLabel("Sexo:")); form.add(cbSexo);

        JButton btnGuardar = new JButton("Guardar Mascota");
        btnGuardar.setBackground(new Color(0, 102, 153));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> registrarMascotaBD());

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        cargarMascotasBD();
    }

    private void cargarMascotasBD() {
        modelo.setRowCount(0);
        int idProp = obtenerIdPropietarioActual();
        
        if (idProp == 0) return;

        String sql = "SELECT a.idANIMAL, a.nombre, e.nombre AS especie, r.nombre AS raza, a.peso, a.color, a.sexo "
                   + "FROM animal a "
                   + "JOIN especie e ON e.idESPECIE = a.ESPECIE_idESPECIE "
                   + "JOIN raza r ON r.idRAZA = a.RAZA_idRAZA "
                   + "WHERE a.PROPIETARIO_idPROPIETARIO = ?";
        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProp);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idANIMAL"), rs.getString("nombre"), rs.getString("especie"),
                    rs.getString("raza"), rs.getBigDecimal("peso"), rs.getString("color"), rs.getString("sexo")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar mascotas: " + ex.getMessage());
        }
    }

    private void registrarMascotaBD() {
        int idProp = obtenerIdPropietarioActual();
        if (idProp == 0) {
            JOptionPane.showMessageDialog(this, "No se encontró el registro de propietario para el usuario actual.");
            return;
        }

        try (Connection cn = ConexionBD.obtener()) {
            cn.setAutoCommit(false);

            // 1. Obtener o crear Especie
            int idEspecie = 1;
            PreparedStatement psE = cn.prepareStatement("INSERT INTO especie (nombre) VALUES (?) ON DUPLICATE KEY UPDATE idESPECIE=LAST_INSERT_ID(idESPECIE)", Statement.RETURN_GENERATED_KEYS);
            psE.setString(1, txtEspecie.getText().trim());
            psE.executeUpdate();
            ResultSet rsE = psE.getGeneratedKeys();
            if (rsE.next()) idEspecie = rsE.getInt(1);

            // 2. Obtener o crear Raza
            int idRaza = 1;
            PreparedStatement psR = cn.prepareStatement("INSERT INTO raza (ESPECIE_idESPECIE, nombre) VALUES (?, ?) ON DUPLICATE KEY UPDATE idRAZA=LAST_INSERT_ID(idRAZA)", Statement.RETURN_GENERATED_KEYS);
            psR.setInt(1, idEspecie);
            psR.setString(2, txtRaza.getText().trim());
            psR.executeUpdate();
            ResultSet rsR = psR.getGeneratedKeys();
            if (rsR.next()) idRaza = rsR.getInt(1);

            // 3. Insertar Animal vinculado únicamente al Propietario actual
            String sqlA = "INSERT INTO animal (PROPIETARIO_idPROPIETARIO, ESPECIE_idESPECIE, RAZA_idRAZA, nombre, fecha_nacimiento, sexo, peso, color, estado, fecha_ingreso, hora_ingreso) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'activo', ?, ?)";
            PreparedStatement psA = cn.prepareStatement(sqlA, Statement.RETURN_GENERATED_KEYS);
            psA.setInt(1, idProp);
            psA.setInt(2, idEspecie);
            psA.setInt(3, idRaza);
            psA.setString(4, txtNombre.getText().trim());
            psA.setObject(5, LocalDate.now());
            psA.setString(6, cbSexo.getSelectedItem().toString());
            psA.setBigDecimal(7, BigDecimal.valueOf(((Number) spPeso.getValue()).doubleValue()));
            psA.setString(8, txtColor.getText().trim());
            psA.setObject(9, LocalDate.now());
            psA.setObject(10, LocalTime.now());
            psA.executeUpdate();

            ResultSet rsA = psA.getGeneratedKeys();
            int idAnimalGenerado = 0;
            if (rsA.next()) idAnimalGenerado = rsA.getInt(1);

            // 4. Crear expediente médico
            PreparedStatement psH = cn.prepareStatement("INSERT INTO historial_medico (ANIMAL_idANIMAL, fecha_creacion, observaciones) VALUES (?, ?, 'Expediente inicial')");
            psH.setInt(1, idAnimalGenerado);
            psH.setObject(2, LocalDate.now());
            psH.executeUpdate();

            cn.commit();
            JOptionPane.showMessageDialog(this, "¡Mascota registrada correctamente!");
            cargarMascotasBD();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar mascota: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtEspecie.setText("");
        txtRaza.setText("");
        spPeso.setValue(0.0);
        txtColor.setText("");
        cbSexo.setSelectedIndex(0);
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