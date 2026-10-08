package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import Modelo.repositorio.ConexionBD;
import Vista.gui.historialmed_V;

public class HistorialMedicoPresenter {
    private final historialmed_V vista;

    public HistorialMedicoPresenter(historialmed_V vista) {
        this.vista = vista;
    }

    public void crearHistorialEnBD(int idAnimal, String observaciones) {
        String sql = "INSERT INTO historial_medico (ANIMAL_idANIMAL, fecha_creacion, observaciones) VALUES (?, ?, ?)";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idAnimal);
            ps.setObject(2, LocalDate.now());
            ps.setString(3, observaciones);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(vista, "¡Expediente médico creado correctamente!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al crear el historial (Verifica si el animal ya posee expediente): " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}