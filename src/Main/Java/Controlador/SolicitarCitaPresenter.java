package Controlador;

import Modelo.repositorio.CitaRepositorio;
import Vista.gui.SolicitarCita;

public class SolicitarCitaPresenter {
    private final SolicitarCita vista;
    
    @SuppressWarnings("unused")
    private final CitaRepositorio repositorio;

    public SolicitarCitaPresenter(SolicitarCita vista) {
        this.vista = vista;
        this.repositorio = new CitaRepositorio();
    }

    public void mostrar() {
        this.vista.setVisible(true);
    }
}