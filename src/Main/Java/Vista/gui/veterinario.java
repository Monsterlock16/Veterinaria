package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class veterinario extends JFrame {

    public veterinario(String usuario) {
        setTitle("Sistema Veterinario - Módulo principal");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblBienvenida = new JLabel(
            "Bienvenido, veterinario " + usuario,
            SwingConstants.CENTER
        );
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnAnimales = new JButton("Gestionar animales");
        JButton btnCitas = new JButton("Gestionar citas");
        JButton btnConsultas = new JButton("Gestionar consultas");
        JButton btnHistorial = new JButton("Historial médico");

        JPanel menu = new JPanel(new GridLayout(2, 2, 10, 10));
        menu.add(btnAnimales);
        menu.add(btnCitas);
        menu.add(btnConsultas);
        menu.add(btnHistorial);

        setLayout(new BorderLayout(10, 20));
        add(lblBienvenida, BorderLayout.NORTH);
        add(menu, BorderLayout.CENTER);
    }
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new veterinario("Jane Doe").setVisible(true));
    }
    
}