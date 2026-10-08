package Controlador;

import javax.swing.JOptionPane;

import Modelo.repositorio.CitaRepositorio;
import Vista.gui.Gstcitas_V;

public class GstcitasPresenter {
    private final Gstcitas_V vista;
    private final CitaRepositorio repositorio;

    public GstcitasPresenter(Gstcitas_V vista) {
        this.vista = vista;
        this.repositorio = new CitaRepositorio();
        cargarCitasBD();
    }

    public final void cargarCitasBD() {
        try {
            if (repositorio.obtenerTodas() != null) {
                System.out.println("✅ Conexión establecida con la tabla de citas.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                vista, 
                "Error al conectar con la base de datos de citas: " + e.getMessage(), 
                "Error BD", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}