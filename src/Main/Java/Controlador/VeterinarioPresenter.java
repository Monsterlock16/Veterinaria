package Controlador;

import Vista.gui.gstanimal;
import Vista.gui.veterinario;

public class VeterinarioPresenter {
    private veterinario vista;
    private String usuario;

    public VeterinarioPresenter(veterinario vista, String usuario) {
        this.vista = vista;
        this.usuario = usuario;
        
        this.vista.getBtnAnimales().addActionListener(e -> {
            gstanimal ventanaAnimales = new gstanimal();
            ventanaAnimales.setVisible(true);
        });
    }
}