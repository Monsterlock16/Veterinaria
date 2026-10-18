package Vista.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/** Selector de fecha con tres desplegables: Dia, Mes y Anio. getFecha() devuelve null si falta alguno. */
public class SelectorFecha extends JPanel {
    private static final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    private final JComboBox<String> cbDia = new JComboBox<>();
    private final JComboBox<String> cbMes = new JComboBox<>();
    private final JComboBox<String> cbAnio = new JComboBox<>();
    private boolean ajustando = false;

    /** Anios desde anioDesde hasta anioHasta; descendente = el mas reciente primero. */
    public SelectorFecha(int anioDesde, int anioHasta, boolean descendente) {
        super(new GridBagLayout());
        setOpaque(false);

        cbMes.addItem("Mes");
        for (String mes : MESES) {
            cbMes.addItem(mes);
        }
        cbAnio.addItem("Año");
        if (descendente) {
            for (int a = anioHasta; a >= anioDesde; a--) {
                cbAnio.addItem(String.valueOf(a));
            }
        } else {
            for (int a = anioDesde; a <= anioHasta; a++) {
                cbAnio.addItem(String.valueOf(a));
            }
        }
        reconstruirDias();

        cbMes.addActionListener(e -> { if (!ajustando) { reconstruirDias(); } });
        cbAnio.addActionListener(e -> { if (!ajustando) { reconstruirDias(); } });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 4);
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0.25; add(cbDia, gbc);
        gbc.gridx = 1; gbc.weightx = 0.45; add(cbMes, gbc);
        gbc.gridx = 2; gbc.weightx = 0.30; gbc.insets = new Insets(0, 0, 0, 0); add(cbAnio, gbc);
    }

    /** La lista de dias depende del mes y del anio (28, 29, 30 o 31). */
    private void reconstruirDias() {
        int mes = cbMes.getSelectedIndex();
        int maximo = 31;
        if (mes > 0) {
            int anio = cbAnio.getSelectedIndex() > 0 ? Integer.parseInt((String) cbAnio.getSelectedItem()) : 2000;
            maximo = YearMonth.of(anio, mes).lengthOfMonth();
        }
        int seleccionado = cbDia.getSelectedIndex();
        ajustando = true;
        cbDia.removeAllItems();
        cbDia.addItem("Día");
        for (int d = 1; d <= maximo; d++) {
            cbDia.addItem(String.valueOf(d));
        }
        cbDia.setSelectedIndex(seleccionado >= 0 && seleccionado <= maximo ? seleccionado : 0);
        ajustando = false;
    }

    /** La fecha elegida, o null si falta el dia, el mes o el anio. */
    public LocalDate getFecha() {
        if (cbDia.getSelectedIndex() <= 0 || cbMes.getSelectedIndex() <= 0 || cbAnio.getSelectedIndex() <= 0) {
            return null;
        }
        return LocalDate.of(Integer.parseInt((String) cbAnio.getSelectedItem()),
                cbMes.getSelectedIndex(), cbDia.getSelectedIndex());
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            limpiar();
            return;
        }
        String anio = String.valueOf(fecha.getYear());
        boolean existe = false;
        for (int i = 0; i < cbAnio.getItemCount(); i++) {
            if (anio.equals(cbAnio.getItemAt(i))) {
                existe = true;
                break;
            }
        }
        ajustando = true;
        if (!existe) {
            cbAnio.addItem(anio);
        }
        cbAnio.setSelectedItem(anio);
        cbMes.setSelectedIndex(fecha.getMonthValue());
        ajustando = false;
        reconstruirDias();
        cbDia.setSelectedIndex(fecha.getDayOfMonth());
    }

    public void limpiar() {
        ajustando = true;
        cbMes.setSelectedIndex(0);
        cbAnio.setSelectedIndex(0);
        ajustando = false;
        reconstruirDias();
        cbDia.setSelectedIndex(0);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        cbDia.setEnabled(enabled);
        cbMes.setEnabled(enabled);
        cbAnio.setEnabled(enabled);
    }
}
