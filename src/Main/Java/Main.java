import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.SwingUtilities;

import Controlador.LoginPresenter;
import Modelo.entidades.Propietario;
import Modelo.repositorio.ConexionBD;
import Modelo.repositorio.PropietarioRepositorio;
import Vista.gui.login;

public class Main {
    public static void main(String[] args) {
        // Probar conexión inicial
        ConexionBD.probar();

        // Operaciones de prueba existentes
        PropietarioRepositorio repo = new PropietarioRepositorio();
        Propietario p = new Propietario(0, LocalDate.now(), LocalTime.now().withNano(0), 0,
                "Luis", "Gomez", LocalDate.of(1985, 5, 5), "100", "3001234567", "luis@correo.com");
        repo.guardar(p);
        System.out.println("Guardado con id " + p.getIdPropietario());
        System.out.println("Total en la base: " + repo.obtenerTodos().size());

        // Iniciar interfaz MVP con Login conectado a MySQL
        SwingUtilities.invokeLater(() -> {
            login vistaLogin = new login();
            new LoginPresenter(vistaLogin);
            vistaLogin.setVisible(true);
        });
    }
}