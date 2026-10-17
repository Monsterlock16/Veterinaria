package Vista.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/** Vista de citas del veterinario: solo dibuja la pantalla; la logica esta en GstcitasPresenter. */
public class Gstcitas_V extends JFrame {

    private final JComboBox<String> cbAnimales = new JComboBox<>();
    private final JComboBox<String> cbVeterinarios = new JComboBox<>();
    private final HashMap<String, Integer> mapaAnimales = new HashMap<>();
    private final HashMap<String, Integer> mapaVeterinarios = new HashMap<>();

    // Calendario visual (igual al del propietario): los dias pasados quedan bloqueados
    private final JPanel panelDiasCalendario = new JPanel(new GridLayout(0, 7, 4, 4));
    private final JLabel lblFechaSeleccionada = new JLabel("Fecha seleccionada: Ninguna", SwingConstants.CENTER);
    private LocalDate fechaSeleccionada = null;
    private Runnable alSeleccionarFecha;

    private final JComboBox<String> cbHoras = new JComboBox<>();
    private final JTextField txtMotivo = new JTextField();
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"PROGRAMADA"});

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID Cita", "Mascota", "Veterinario", "Fecha", "Hora", "Motivo", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JButton btnAgendar = new JButton("Agendar Cita");
    private final JButton btnAceptar = new JButton("Aceptar cita");
    private final JButton btnRechazar = new JButton("Rechazar cita");
    private final JButton btnAtendida = new JButton("Marcar atendida");
    private final JButton btnActualizar = new JButton("Actualizar lista");

    public Gstcitas_V() {
        TemaVeterinaria.aplicarIcono(this);
        setTitle("Gestión de Citas - Veterinario");
        setSize(920, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Izquierda: calendario
        JPanel pnlCalendario = new JPanel(new BorderLayout(5, 5));
        pnlCalendario.setBorder(BorderFactory.createTitledBorder("Seleccione el día (mes actual)"));
        pnlCalendario.setPreferredSize(new Dimension(380, 230));
        construirCalendarioVisual();
        pnlCalendario.add(panelDiasCalendario, BorderLayout.CENTER);
        lblFechaSeleccionada.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFechaSeleccionada.setForeground(new Color(0, 102, 153));
        pnlCalendario.add(lblFechaSeleccionada, BorderLayout.SOUTH);

        // Derecha: formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Agendar Nueva Cita"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        agregarFila(form, gbc, 0, "Seleccionar Animal:", cbAnimales);
        agregarFila(form, gbc, 1, "Veterinario:", cbVeterinarios);
        agregarFila(form, gbc, 2, "Horas disponibles (AM / PM):", cbHoras);
        agregarFila(form, gbc, 3, "Motivo:", txtMotivo);
        agregarFila(form, gbc, 4, "Estado:", cbEstado);
        cbEstado.setEnabled(false);

        btnAgendar.setBackground(new Color(0, 102, 153));
        btnAgendar.setForeground(Color.WHITE);

        JPanel centroSuperior = new JPanel(new BorderLayout(10, 0));
        centroSuperior.add(pnlCalendario, BorderLayout.WEST);
        centroSuperior.add(form, BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(centroSuperior, BorderLayout.CENTER);
        top.add(btnAgendar, BorderLayout.SOUTH);

        // Botones para gestionar las citas que aparecen en la tabla
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        acciones.add(btnAceptar);
        acciones.add(btnRechazar);
        acciones.add(btnAtendida);
        acciones.add(btnActualizar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(acciones, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        TemaVeterinaria.aplicarEstiloVentana(this);
    }

    private void agregarFila(JPanel form, GridBagConstraints gbc, int fila, String texto, java.awt.Component campo) {
        gbc.gridy = fila;
        gbc.gridx = 0; gbc.weightx = 0.3;
        form.add(new JLabel(texto), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(campo, gbc);
    }

    private void construirCalendarioVisual() {
        panelDiasCalendario.removeAll();
        LocalDate hoy = LocalDate.now();
        YearMonth ym = YearMonth.from(hoy);

        String[] diasSemana = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
        for (String d : diasSemana) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panelDiasCalendario.add(lbl);
        }

        // Espacios vacios para que el dia 1 caiga en su dia de la semana real
        int huecos = ym.atDay(1).getDayOfWeek().getValue() - 1;
        for (int i = 0; i < huecos; i++) {
            panelDiasCalendario.add(new JLabel());
        }

        for (int dia = 1; dia <= ym.lengthOfMonth(); dia++) {
            LocalDate fechaDia = ym.atDay(dia);
            JButton btnDia = new JButton(String.valueOf(dia));
            btnDia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnDia.setFocusPainted(false);
            btnDia.setMargin(new Insets(2, 2, 2, 2));

            if (fechaDia.isBefore(hoy)) {
                btnDia.setEnabled(false);
                btnDia.setBackground(new Color(220, 220, 220));
            } else {
                btnDia.setBackground(Color.WHITE);
                btnDia.addActionListener(e -> {
                    fechaSeleccionada = fechaDia;
                    lblFechaSeleccionada.setText("Fecha seleccionada: " + fechaSeleccionada);
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

    public LocalDate getFechaSeleccionada() { return fechaSeleccionada; }
    public JComboBox<String> getCbAnimales() { return cbAnimales; }
    public JComboBox<String> getCbVeterinarios() { return cbVeterinarios; }
    public HashMap<String, Integer> getMapaAnimales() { return mapaAnimales; }
    public HashMap<String, Integer> getMapaVeterinarios() { return mapaVeterinarios; }
    public JComboBox<String> getCbHoras() { return cbHoras; }
    public JTextField getTxtMotivo() { return txtMotivo; }
    public JComboBox<String> getCbEstado() { return cbEstado; }
    public DefaultTableModel getModelo() { return modelo; }
    public JTable getTabla() { return tabla; }
    public JButton getBtnAgendar() { return btnAgendar; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public JButton getBtnAtendida() { return btnAtendida; }
    public JButton getBtnActualizar() { return btnActualizar; }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    public boolean confirmar(String pregunta) {
        return JOptionPane.showConfirmDialog(this, pregunta, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}