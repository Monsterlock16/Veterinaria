package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SolicitarCita extends JFrame {
    private final JComboBox<String> cbMascotas = new JComboBox<>();
    private final JComboBox<String> cbVeterinarios = new JComboBox<>();
    private final HashMap<String, Integer> mapaMascotas = new HashMap<>();
    private final HashMap<String, Integer> mapaVeterinarios = new HashMap<>();

    private final JLabel lblFechaSeleccionada = new JLabel("Fecha seleccionada: Ninguna", SwingConstants.CENTER);
    private LocalDate fechaSeleccionada = null;
    private final JPanel panelDiasCalendario = new JPanel();
    
    private final JComboBox<String> cbHorasDisponibles = new JComboBox<>();
    private final JTextField txtMotivo = new JTextField();
    
    private final JButton btnVolver = new JButton("← Volver al menú");
    private final JButton btnSolicitar = new JButton("Agendar Cita");
    private final DatosPropietario datos;
    private Runnable alSeleccionarFecha;

    public SolicitarCita(DatosPropietario datos) {
        TemaVeterinaria.aplicarIcono(this);
        this.datos = datos;

        setTitle("Solicitar Cita Médica - Sistema Veterinario");
        setSize(720, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(new EmptyBorder(15, 20, 15, 20));
        panelPrincipal.setBackground(TemaVeterinaria.FONDO);

        // 1. Mascota
        JPanel pnlMascota = new JPanel(new BorderLayout(10, 0));
        pnlMascota.setOpaque(false);
        pnlMascota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlMascota.add(new JLabel("Seleccionar Mascota:"), BorderLayout.WEST);
        pnlMascota.add(cbMascotas, BorderLayout.CENTER);
        panelPrincipal.add(pnlMascota);
        panelPrincipal.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));

        // 2. Veterinario
        JPanel pnlVet = new JPanel(new BorderLayout(10, 0));
        pnlVet.setOpaque(false);
        pnlVet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlVet.add(new JLabel("Seleccionar Veterinario:"), BorderLayout.WEST);
        pnlVet.add(cbVeterinarios, BorderLayout.CENTER);
        panelPrincipal.add(pnlVet);
        panelPrincipal.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));

        // 3. Calendario Visual
        JPanel pnlFechaContainer = new JPanel(new BorderLayout(5, 5));
        pnlFechaContainer.setOpaque(false);
        pnlFechaContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        pnlFechaContainer.add(new JLabel("Seleccione el Día Disponible (Mes Actual):"), BorderLayout.NORTH);
        
        panelDiasCalendario.setLayout(new GridLayout(0, 7, 5, 5));
        panelDiasCalendario.setOpaque(false);
        construirCalendarioVisual();
        pnlFechaContainer.add(panelDiasCalendario, BorderLayout.CENTER);
        
        lblFechaSeleccionada.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFechaSeleccionada.setForeground(new Color(0, 102, 153));
        pnlFechaContainer.add(lblFechaSeleccionada, BorderLayout.SOUTH);
        panelPrincipal.add(pnlFechaContainer);
        panelPrincipal.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));

        // 4. Franja Horaria
        JPanel pnlHoraContainer = new JPanel(new BorderLayout(10, 0));
        pnlHoraContainer.setOpaque(false);
        pnlHoraContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlHoraContainer.add(new JLabel("Horas Disponibles (AM / PM):"), BorderLayout.WEST);
        pnlHoraContainer.add(cbHorasDisponibles, BorderLayout.CENTER);
        panelPrincipal.add(pnlHoraContainer);
        panelPrincipal.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));

        // 5. Motivo
        JPanel pnlMotivo = new JPanel(new BorderLayout(10, 0));
        pnlMotivo.setOpaque(false);
        pnlMotivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlMotivo.add(new JLabel("Motivo de Consulta:"), BorderLayout.WEST);
        pnlMotivo.add(txtMotivo, BorderLayout.CENTER);
        panelPrincipal.add(pnlMotivo);
        panelPrincipal.add(javax.swing.Box.createRigidArea(new Dimension(0, 15)));

        // 6. Botones
        JPanel pnlBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnVolver.setBackground(new Color(240, 242, 245));
        btnVolver.setForeground(new Color(45, 55, 65));
        btnVolver.setFocusPainted(false);

        btnSolicitar.setBackground(new Color(0, 102, 153));
        btnSolicitar.setForeground(Color.WHITE);
        btnSolicitar.setFocusPainted(false);

        pnlBotones.add(btnVolver);
        pnlBotones.add(btnSolicitar);
        panelPrincipal.add(pnlBotones);

        add(panelPrincipal, BorderLayout.CENTER);
        TemaVeterinaria.aplicarEstiloVentana(this);
    }

    private void construirCalendarioVisual() {
        panelDiasCalendario.removeAll();
        LocalDate hoy = LocalDate.now();
        YearMonth ym = YearMonth.from(hoy);
        int diasEnMes = ym.lengthOfMonth();

        String[] diasSemana = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
        for (String d : diasSemana) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panelDiasCalendario.add(lbl);
        }

        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fechaDia = LocalDate.of(ym.getYear(), ym.getMonth(), dia);
            JButton btnDia = new JButton(String.valueOf(dia));
            btnDia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnDia.setFocusPainted(false);

            if (fechaDia.isBefore(hoy)) {
                btnDia.setEnabled(false);
                btnDia.setBackground(new Color(220, 220, 220));
            } else {
                btnDia.setBackground(Color.WHITE);
                final LocalDate seleccion = fechaDia;
                btnDia.addActionListener(e -> {
                    fechaSeleccionada = seleccion;
                    lblFechaSeleccionada.setText("Fecha seleccionada: " + fechaSeleccionada.toString());
                    if (alSeleccionarFecha != null) {
                        alSeleccionarFecha.run();
                    }
                });
            }
            panelDiasCalendario.add(btnDia);
        }
        panelDiasCalendario.revalidate();
        panelDiasCalendario.repaint();
    }

    /** El presentador se registra aqui para recalcular las horas cuando se elige un dia. */
    public void setAlSeleccionarFecha(Runnable accion) { this.alSeleccionarFecha = accion; }
    public DatosPropietario getDatos() { return datos; }
    public JComboBox<String> getCbMascotas() { return cbMascotas; }
    public JComboBox<String> getCbVeterinarios() { return cbVeterinarios; }
    public JComboBox<String> getCbHorasDisponibles() { return cbHorasDisponibles; }
    public HashMap<String, Integer> getMapaMascotas() { return mapaMascotas; }
    public HashMap<String, Integer> getMapaVeterinarios() { return mapaVeterinarios; }
    public LocalDate getFechaSeleccionada() { return fechaSeleccionada; }
    public JLabel getLblFechaSeleccionada() { return lblFechaSeleccionada; }
    public JTextField getTxtMotivo() { return txtMotivo; }
    public JButton getBtnVolver() { return btnVolver; }
    public JButton getBtnSolicitar() { return btnSolicitar; }
}