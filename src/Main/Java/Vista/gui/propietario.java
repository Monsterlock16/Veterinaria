package Vista.gui;

import java.awt.*;
import javax.swing.*;

public class propietario extends JFrame {

    public propietario(String usuario) {
        setTitle("Sistema Veterinario - Módulo principal");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblBienvenida = new JLabel(
            "Bienvenido, propietario " + usuario,
            SwingConstants.CENTER
        );
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnmisAnimales = new JButton("Mis animales");
        JButton btnsolicitarCita = new JButton("Solicitar cita");
        JButton btnmisCitas = new JButton("Mis citas");
        JButton btnmiHistorial = new JButton("Mi historial médico");

        JPanel menu = new JPanel(new GridLayout(2, 2, 10, 10));
        menu.add(btnmisAnimales);
        menu.add(btnsolicitarCita);
        menu.add(btnmisCitas);
        menu.add(btnmiHistorial);

        setLayout(new BorderLayout(10, 20));
        add(lblBienvenida, BorderLayout.NORTH);
        add(menu, BorderLayout.CENTER);
    }
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new propetario("John Doe").setVisible(true));
    }
}