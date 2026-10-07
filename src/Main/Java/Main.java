import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

import javax.swing.SwingUtilities;

import Controlador.LoginPresenter;
import Modelo.entidades.Propietario;
import Modelo.entidades.Veterinario;
import Modelo.repositorio.ConexionBD;
import Modelo.repositorio.PropietarioRepositorio;
import Modelo.repositorio.VeterinarioRepositorio;
import Vista.gui.login;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== VERIFICACIÓN DE BASE DE DATOS Y APLICACIÓN ===");

        // 1. Probar la conexión básica
        try (Connection cn = ConexionBD.obtener()) {
            DatabaseMetaData metaData = cn.getMetaData();
            System.out.println("✅ Conexión exitosa a MySQL!");
            System.out.println("   Base de Datos: " + cn.getCatalog());
            System.out.println("   Motor: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
            System.out.println("------------------------------------------");
        } catch (Exception e) {
            System.err.println("❌ ERROR AL CONECTAR A LA BASE DE DATOS:");
            System.err.println(e.getMessage());
            return;
        }

        // 2. Cargar y verificar registros en consola
        try {
            PropietarioRepositorio propRepo = new PropietarioRepositorio();
            List<Propietario> propietarios = propRepo.obtenerTodos();
            
            System.out.println("\n--- REGISTROS EN LA TABLA PROPIETARIO (" + propietarios.size() + ") ---");
            for (Propietario p : propietarios) {
                System.out.println("ID: " + p.getIdPropietario() 
                                   + " | Nombre: " + p.getNombres() + " " + p.getApellidos()
                                   + " | Doc: " + p.getDocumento()
                                   + " | Correo: " + p.getCorreo());
            }

            VeterinarioRepositorio vetRepo = new VeterinarioRepositorio();
            List<Veterinario> veterinarios = vetRepo.obtenerVeterinarios();
            
            System.out.println("\n--- REGISTROS EN LA TABLA VETERINARIO (" + veterinarios.size() + ") ---");
            for (Veterinario v : veterinarios) {
                System.out.println("ID: " + v.getIdVeterinario() 
                                   + " | Nombre: " + v.getNombres() + " " + v.getApellidos()
                                   + " | Licencia: " + v.getNumeroLicencia()
                                   + " | Especialidad: " + v.getEspecialidad());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Advertencia al leer tablas: " + e.getMessage());
        }

        // 3. Iniciar la interfaz gráfica del sistema (Login bajo MVP)
        System.out.println("\n🚀 Iniciando interfaz gráfica...");
        SwingUtilities.invokeLater(() -> {
            login vistaLogin = new login();
            new LoginPresenter(vistaLogin);
            vistaLogin.setVisible(true);
        });
    }
}