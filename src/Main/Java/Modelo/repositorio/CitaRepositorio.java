package Modelo.repositorio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import Modelo.entidades.Cita;

/** Citas guardadas en MySQL (tabla CITA). */
public class CitaRepositorio {

    private static final String SELECT_BASE =
            "SELECT idCITA, ANIMAL_idANIMAL, VETERINARIO_idVETERINARIO, fecha, hora, motivo, estado FROM CITA ";

    private final AnimalRepositorio animales = new AnimalRepositorio();
    private final VeterinarioRepositorio veterinarios = new VeterinarioRepositorio();

    /** El id lo asigna MySQL y se copia al objeto Cita. */
    public void agendar(Cita c) {
        if (c == null || (c.getIdCita() > 0 && buscarPorId(c.getIdCita()) != null)) {
            return;
        }
        JdbcUtil.enTransaccion(cn -> {
            String sql = "INSERT INTO CITA (ANIMAL_idANIMAL, VETERINARIO_idVETERINARIO, fecha, hora, motivo, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, c.getAnimal().getIdAnimal());
                ps.setInt(2, c.getVeterinario().getIdVeterinario());
                ps.setObject(3, c.getFecha());
                ps.setObject(4, c.getHora());
                ps.setString(5, c.getMotivo());
                ps.setString(6, c.getEstado().name().toLowerCase(Locale.ROOT));
                ps.executeUpdate();
                c.setIdCita(JdbcUtil.claveGenerada(ps));
            }
        });
    }

    public List<Cita> obtenerTodas() {
        return JdbcUtil.consultar(SELECT_BASE + "ORDER BY fecha, hora", ps -> { }, this::mapear);
    }

    public Cita buscarPorId(int id) {
        return JdbcUtil.consultarUno(SELECT_BASE + "WHERE idCITA = ?", ps -> ps.setInt(1, id), this::mapear);
    }

    // Para consultar la agenda del dia
    public List<Cita> buscarPorFecha(LocalDate fecha) {
        if (fecha == null) {
            return new java.util.ArrayList<>();
        }
        return JdbcUtil.consultar(SELECT_BASE + "WHERE fecha = ? ORDER BY hora",
                ps -> ps.setObject(1, fecha), this::mapear);
    }

    /** Cambia el estado (programada, atendida, cancelada) de una cita ya guardada. */
    public void actualizarEstado(int idCita, Cita.EstadoCita estado) {
        JdbcUtil.enTransaccion(cn -> {
            try (PreparedStatement ps = cn.prepareStatement("UPDATE CITA SET estado = ? WHERE idCITA = ?")) {
                ps.setString(1, estado.name().toLowerCase(Locale.ROOT));
                ps.setInt(2, idCita);
                ps.executeUpdate();
            }
        });
    }

    private Cita mapear(ResultSet rs) throws SQLException {
        return new Cita(
                rs.getInt("idCITA"),
                animales.buscarPorId(rs.getInt("ANIMAL_idANIMAL")),
                veterinarios.buscarPorId(rs.getInt("VETERINARIO_idVETERINARIO")),
                rs.getObject("fecha", LocalDate.class),
                rs.getObject("hora", LocalTime.class),
                rs.getString("motivo"),
                Cita.EstadoCita.valueOf(rs.getString("estado").toUpperCase(Locale.ROOT)));
    }
}
