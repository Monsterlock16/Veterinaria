package Modelo.repositorio;

import java.util.ArrayList;
import java.util.List;

/** Opciones de los desplegables (especies, razas y especialidades), leidas de la base de datos. */
public class Catalogorepositorio {

    /** Todas las especies animales, ordenadas por nombre. */
    public List<String> listarEspecies() {
        return JdbcUtil.consultar("SELECT nombre FROM especie ORDER BY nombre",
                ps -> { }, rs -> rs.getString(1));
    }

    /** Las razas de una especie, ordenadas por nombre. */
    public List<String> listarRazas(String especie) {
        if (especie == null || especie.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "SELECT r.nombre FROM raza r "
                   + "JOIN especie e ON e.idESPECIE = r.ESPECIE_idESPECIE "
                   + "WHERE e.nombre = ? ORDER BY r.nombre";
        return JdbcUtil.consultar(sql, ps -> ps.setString(1, especie.trim()), rs -> rs.getString(1));
    }

    /** Las especialidades que puede tener un veterinario, ordenadas por nombre. */
    public List<String> listarEspecialidades() {
        return JdbcUtil.consultar("SELECT nombre FROM especialidad ORDER BY nombre",
                ps -> { }, rs -> rs.getString(1));
    }
}
