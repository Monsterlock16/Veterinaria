package Controlador;

import javax.swing.JOptionPane;

import Modelo.entidades.Animal;
import Modelo.repositorio.AnimalRepositorio;
import Modelo.repositorio.RepositorioException;
import Vista.gui.gstanimalesV;

public class GstanimalesPresenter {
    private final gstanimalesV vista;
    private final AnimalRepositorio repositorio;

    public GstanimalesPresenter(gstanimalesV vista) {
        this.vista = vista;
        this.repositorio = new AnimalRepositorio();
        cargarDatosBD();
    }

    public final void cargarDatosBD() {
        try {
            if (repositorio.listarTodos() != null) {
                System.out.println("✅ Conexión establecida con la tabla de animales.");
            }
        } catch (RepositorioException e) {
            JOptionPane.showMessageDialog(
                vista, 
                "Error al conectar con la base de datos: " + e.getMessage(), 
                "Error BD", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void registrarEnBD(Animal animal) {
        try {
            repositorio.registrar(animal);
            JOptionPane.showMessageDialog(vista, "¡Animal guardado exitosamente en phpMyAdmin!");
        } catch (RepositorioException e) {
            JOptionPane.showMessageDialog(
                vista, 
                "Error al guardar en la base de datos: " + e.getMessage(), 
                "Error BD", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}