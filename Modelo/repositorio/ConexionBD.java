package repositorio;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {

    private static final String ARCHIVO = "/conexion.properties";

    private ConexionBD() {
    }

    /** Abre una conexion nueva. Quien la pida debe cerrarla (try-with-resources). */
    public static Connection obtener() {
        Properties p = cargarConfiguracion();
        String url = "jdbc:mysql://" + p.getProperty("host") + ":" + p.getProperty("puerto")
                + "/" + p.getProperty("base")
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            return DriverManager.getConnection(url, p.getProperty("usuario"), p.getProperty("clave"));
        } catch (SQLException e) {
            throw new RepositorioException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    /** Abre y cierra una conexion; sirve para verificar la configuracion al iniciar. */
    public static void probar() {
        try (Connection cn = obtener()) {
            cn.isValid(3);
        } catch (SQLException e) {
            throw new RepositorioException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    private static Properties cargarConfiguracion() {
        Properties p = new Properties();
        p.setProperty("host", "localhost");
        p.setProperty("puerto", "3306");
        p.setProperty("base", "dbo_veterinaria");
        p.setProperty("usuario", "root");
        p.setProperty("clave", "");
        try (InputStream in = ConexionBD.class.getResourceAsStream(ARCHIVO)) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException e) {
            throw new RepositorioException("No se pudo leer conexion.properties: " + e.getMessage(), e);
        }
        return p;
    }
}