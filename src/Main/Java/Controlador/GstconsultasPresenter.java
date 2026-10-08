package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JOptionPane;

import Modelo.repositorio.ConexionBD;
import Vista.gui.Gstconsultas;

public class GstconsultasPresenter {
    private final Gstconsultas vista;

    public GstconsultasPresenter(Gstconsultas vista) {
        this.vista = vista;
    }

    public void registrarConsultaEnBD(int idHistorialMedico, int idCita, String sintomas, String diagnostico, String tratamiento, String observaciones) {
        String sql = "INSERT INTO consulta (HISTORIAL_MEDICO_idHISTORIAL_MEDICO, CITA_idCITA, fecha_consulta, hora_consulta, sintomas, diagnostico, tratamiento, observaciones) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idHistorialMedico);
            ps.setInt(2, idCita);
            ps.setObject(3, LocalDate.now());
            ps.setObject(4, LocalTime.now());
            ps.setString(5, sintomas);
            ps.setString(6, diagnostico);
            ps.setString(7, tratamiento);
            ps.setString(8, observaciones);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(vista, "¡Consulta guardada exitosamente en la base de datos!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al guardar la consulta en BD: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}