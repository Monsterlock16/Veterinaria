package Modelo.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import Modelo.entidades.Animal;
import Modelo.entidades.Especie;
import Modelo.entidades.Raza;

/** Animales guardados en MySQL (tablas ANIMAL, RAZA y ESPECIE). */
public class AnimalRepositorio {

    private static final String SELECT_BASE =
            "SELECT a.idANIMAL, a.PROPIETARIO_idPROPIETARIO, a.nombre, a.fecha_nacimiento, a.sexo, "
          + "a.peso, a.color, a.estado, a.fecha_ingreso, a.hora_ingreso, "
          + "r.idRAZA, r.nombre AS raza_nombre, e.idESPECIE, e.nombre AS especie_nombre "
          + "FROM ANIMAL a "
          + "JOIN RAZA r ON r.idRAZA = a.RAZA_idRAZA "
          + "JOIN ESPECIE e ON e.idESPECIE = r.ESPECIE_idESPECIE ";

    private final PropietarioRepositorio propietarios = new PropietarioRepositorio();

    /**El id lo asigna MySQL (AUTO_INCREMENT) y se copia al objeto Animal. */
    public void registrar(Animal a) {
        if (a == null || (a.getIdAnimal() > 0 && buscarPorId(a.getIdAnimal()) != null)) {
            return;
        }
        JdbcUtil.enTransaccion(cn -> {
            int idEspecie = obtenerOCrearEspecie(cn, a.getRaza().getEspecie().getnombreEspecie());
            int idRaza = obtenerOCrearRaza(cn, idEspecie, a.getRaza().getNombre());
            a.getRaza().getEspecie().setidEspecie(idEspecie);
            a.getRaza().setIdRaza(idRaza);

            String sql = "INSERT INTO ANIMAL (PROPIETARIO_idPROPIETARIO, ESPECIE_idESPECIE, RAZA_idRAZA, nombre, "
                    + "fecha_nacimiento, sexo, peso, color, estado, fecha_ingreso, hora_ingreso) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, a.getPropietario().getIdPropietario());
                ps.setInt(2, idEspecie);
                ps.setInt(3, idRaza);
                ps.setString(4, a.getNombre());
                ps.setObject(5, a.getFechaNacimiento());
                ps.setString(6, sexoABd(a.getSexo()));
                ps.setBigDecimal(7, a.getPeso());
                ps.setString(8, a.getColor());
                ps.setString(9, a.getEstado().name().toLowerCase(Locale.ROOT));
                ps.setObject(10, a.getFechaIngreso());
                ps.setObject(11, a.getHoraIngreso());
                ps.executeUpdate();
                a.setIdAnimal(JdbcUtil.claveGenerada(ps));
            }
            // Todo animal nuevo tiene su expediente medico (si ya existe, no se duplica)
            try (PreparedStatement psH = cn.prepareStatement(
                    "INSERT IGNORE INTO HISTORIAL_MEDICO (ANIMAL_idANIMAL, fecha_creacion, observaciones) "
                  + "VALUES (?, ?, 'Expediente inicial')")) {
                psH.setInt(1, a.getIdAnimal());
                psH.setObject(2, LocalDate.now());
                psH.executeUpdate();
            }
        });
    }

    public List<Animal> listarTodos() {
        return JdbcUtil.consultar(SELECT_BASE + "ORDER BY a.idANIMAL", ps -> { }, this::mapear);
    }

    public List<Animal> obtenerTodos() {
        return listarTodos();
    }

    public Animal buscarPorId(int id) {
        return JdbcUtil.consultarUno(SELECT_BASE + "WHERE a.idANIMAL = ?", ps -> ps.setInt(1, id), this::mapear);
    }

    // Busca las mascotas de un propietario especifico (por su documento)
    public List<Animal> buscarPorPropietario(String docPropietario) {
        if (docPropietario == null) {
            return new java.util.ArrayList<>();
        }
        String sql = SELECT_BASE
                + "JOIN PROPIETARIO pr ON pr.idPROPIETARIO = a.PROPIETARIO_idPROPIETARIO "
                + "JOIN PERSONA p ON p.idPERSONA = pr.PERSONA_idPERSONA "
                + "WHERE p.documento = ? ORDER BY a.idANIMAL";
        return JdbcUtil.consultar(sql, ps -> ps.setString(1, docPropietario), this::mapear);
    }

    // Mascotas del propietario que inicio sesion (por su nombre de usuario)
    public List<Animal> buscarPorUsuario(String nombreUsuario) {
        if (nombreUsuario == null) {
            return new java.util.ArrayList<>();
        }
        String sql = SELECT_BASE
                + "JOIN PROPIETARIO pr ON pr.idPROPIETARIO = a.PROPIETARIO_idPROPIETARIO "
                + "JOIN USUARIO u ON u.PERSONA_idPERSONA = pr.PERSONA_idPERSONA "
                + "WHERE u.usuario = ? ORDER BY a.idANIMAL";
        return JdbcUtil.consultar(sql, ps -> ps.setString(1, nombreUsuario.trim()), this::mapear);
    }

    /**
     * Guarda los cambios de un animal que ya existe (nombre, especie, raza, fecha de nacimiento,
     * sexo, peso, color y estado). El propietario no cambia. Devuelve false si el animal no existe.
     */
    public boolean actualizar(Animal a) {
        if (a == null || a.getIdAnimal() <= 0) {
            return false;
        }
        boolean[] encontrado = new boolean[1];
        JdbcUtil.enTransaccion(cn -> {
            int idEspecie = obtenerOCrearEspecie(cn, a.getRaza().getEspecie().getnombreEspecie());
            int idRaza = obtenerOCrearRaza(cn, idEspecie, a.getRaza().getNombre());
            a.getRaza().getEspecie().setidEspecie(idEspecie);
            a.getRaza().setIdRaza(idRaza);

            String sql = "UPDATE ANIMAL SET ESPECIE_idESPECIE = ?, RAZA_idRAZA = ?, nombre = ?, "
                    + "fecha_nacimiento = ?, sexo = ?, peso = ?, color = ?, estado = ? WHERE idANIMAL = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, idEspecie);
                ps.setInt(2, idRaza);
                ps.setString(3, a.getNombre());
                ps.setObject(4, a.getFechaNacimiento());
                ps.setString(5, sexoABd(a.getSexo()));
                ps.setBigDecimal(6, a.getPeso());
                ps.setString(7, a.getColor());
                ps.setString(8, a.getEstado().name().toLowerCase(Locale.ROOT));
                ps.setInt(9, a.getIdAnimal());
                encontrado[0] = ps.executeUpdate() > 0;
            }
        });
        return encontrado[0];
    }

    private Animal mapear(ResultSet rs) throws SQLException {
        Especie especie = new Especie(rs.getInt("idESPECIE"), rs.getString("especie_nombre"));
        Raza raza = new Raza(rs.getInt("idRAZA"), especie, rs.getString("raza_nombre"));
        return new Animal(
                rs.getInt("idANIMAL"),
                propietarios.buscarPorId(rs.getInt("PROPIETARIO_idPROPIETARIO")),
                raza,
                rs.getString("nombre"),
                rs.getObject("fecha_nacimiento", LocalDate.class),
                Animal.Sexo.valueOf(rs.getString("sexo").toUpperCase(Locale.ROOT)),
                rs.getBigDecimal("peso"),
                rs.getString("color"),
                Animal.EstadoAnimal.valueOf(rs.getString("estado").toUpperCase(Locale.ROOT)),
                rs.getObject("fecha_ingreso", LocalDate.class),
                rs.getObject("hora_ingreso", LocalTime.class));
    }

    /** En la BD el ENUM es ('Macho','Hembra'). */
    private static String sexoABd(Animal.Sexo sexo) {
        String n = sexo.name();
        return n.charAt(0) + n.substring(1).toLowerCase(Locale.ROOT);
    }

    private int obtenerOCrearEspecie(Connection cn, String nombre) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT idESPECIE FROM ESPECIE WHERE nombre = ?")) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("INSERT INTO ESPECIE (nombre) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            return JdbcUtil.claveGenerada(ps);
        }
    }

    private int obtenerOCrearRaza(Connection cn, int idEspecie, String nombre) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT idRAZA FROM RAZA WHERE ESPECIE_idESPECIE = ? AND nombre = ?")) {
            ps.setInt(1, idEspecie);
            ps.setString(2, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        try (PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO RAZA (ESPECIE_idESPECIE, nombre) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idEspecie);
            ps.setString(2, nombre);
            ps.executeUpdate();
            return JdbcUtil.claveGenerada(ps);
        }
    }
}
