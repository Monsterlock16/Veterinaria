import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.swing.SwingUtilities;

import Controlador.LoginPresenter;
import Modelo.repositorio.ConexionBD;
import Vista.gui.login;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== VERIFICACIÓN DE BASE DE DATOS Y APLICACIÓN ===");

        // 1. Probar la conexión básica
        try (Connection cn = ConexionBD.obtener()) {
            DatabaseMetaData metaData = cn.getMetaData();
            System.out.println("Conexión exitosa a MySQL!");
            System.out.println("   Base de Datos: " + cn.getCatalog());
            System.out.println("   Motor: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
            System.out.println("------------------------------------------");
        } catch (Exception e) {
            System.err.println("ERROR AL CONECTAR A LA BASE DE DATOS:");
            System.err.println(e.getMessage());
            return;
        }

        // 2. Iniciar la interfaz gráfica del sistema (Login bajo MVP)
        System.out.println("\n🚀 Iniciando interfaz gráfica...");
        SwingUtilities.invokeLater(() -> {
            login vistaLogin = new login();
            @SuppressWarnings("unused")
            LoginPresenter loginPresenter = new LoginPresenter(vistaLogin);
            vistaLogin.setVisible(true);
        });
    }
}