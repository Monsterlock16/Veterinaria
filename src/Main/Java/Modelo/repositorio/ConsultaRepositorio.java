package Modelo.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import Modelo.entidades.Consulta;

public class ConsultaRepositorio {

    public boolean registrarConsulta(Consulta consulta) {
        
        // Sentencia exacta con los 8 campos actuales de la base de datos
        String sql = "INSERT INTO consulta (HISTORIAL_MEDICO_idHISTORIAL_MEDICO, CITA_idCITA, fecha_consulta, hora_consulta, sintomas, diagnostico, tratamiento, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (consulta.getHistorialMedico() != null) {
                stmt.setInt(1, consulta.getHistorialMedico().getIdHistorialMedico());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }

            if (consulta.getCita() != null) {
                stmt.setInt(2, consulta.getCita().getIdCita());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            LocalDate fecha = consulta.getFechaConsulta() != null ? consulta.getFechaConsulta() : LocalDate.now();
            stmt.setDate(3, java.sql.Date.valueOf(fecha));

            LocalTime hora = consulta.getHoraConsulta() != null ? consulta.getHoraConsulta() : LocalTime.now();
            stmt.setTime(4, java.sql.Time.valueOf(hora));

            stmt.setString(5, consulta.getSintomas());
            stmt.setString(6, consulta.getDiagnostico());
            stmt.setString(7, consulta.getTratamiento());
            stmt.setString(8, consulta.getObservaciones());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        consulta.setIdConsulta(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar la consulta medica: " + e.getMessage(), e);
        }
        return false;
    }
}