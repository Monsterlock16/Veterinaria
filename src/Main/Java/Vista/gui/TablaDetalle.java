package Vista.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

/** Tabla de consulta con un panel debajo que muestra completa la fila seleccionada (solo lectura). */
public final class TablaDetalle {
    private static final String AYUDA = "Seleccione una fila para ver el detalle completo.";

    private TablaDetalle() {
    }

    public static JPanel crear(JTable tabla) {
        JTextArea detalle = new JTextArea(6, 40);
        detalle.setEditable(false);
        detalle.setLineWrap(true);
        detalle.setWrapStyleWord(true);
        detalle.setText(AYUDA);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                detalle.setText(AYUDA);
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < tabla.getColumnCount(); c++) {
                Object valor = tabla.getValueAt(fila, c);
                sb.append(tabla.getColumnName(c)).append(": ").append(valor == null ? "" : valor).append('\n');
            }
            detalle.setText(sb.toString());
            detalle.setCaretPosition(0);
        });

        JScrollPane scrollDetalle = new JScrollPane(detalle);
        scrollDetalle.setBorder(BorderFactory.createTitledBorder("Detalle"));

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(scrollDetalle, BorderLayout.SOUTH);
        return panel;
    }
}
