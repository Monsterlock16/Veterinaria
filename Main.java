import java.sql.Connection;
import java.sql.SQLException;
import repositorio.ConexionBD;
import repositorio.RepositorioException;

public class Main {

    public static void main(String[] args) {
        try (Connection cn = ConexionBD.obtener()) {
            System.out.println("Conexion exitosa a la base: " + cn.getCatalog());
        } catch (RepositorioException | SQLException e) {
            System.out.println("Fallo la conexion: " + e.getMessage());
        }
    }
}