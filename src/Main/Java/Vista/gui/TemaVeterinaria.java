package Vista.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

public final class TemaVeterinaria {
    public static final Color PRIMARIO = new Color(23, 116, 110);
    public static final Color PRIMARIO_OSCURO = new Color(14, 84, 79);
    public static final Color SECUNDARIO = new Color(47, 140, 180);
    public static final Color FONDO = new Color(245, 248, 250);
    public static final Color PANEL = new Color(232, 244, 239);
    public static final Color INPUT = Color.WHITE;
    public static final Color TEXTO = new Color(38, 52, 61);
    public static final Color TEXTO_SECUNDARIO = new Color(100, 110, 120);
    public static final Color BORDE = new Color(176, 196, 190);
    public static final Color ERROR = new Color(190, 79, 79);
    private static final String RUTA_ICONO = "/iconos/logo_veterinaria.png";
    private static final Path ICONO_EN_FUENTES = Path.of(
        "src", "Main", "resources", "iconos", "logo_veterinaria.png"
    );

    private TemaVeterinaria() {
    }

    public static void aplicarIcono(Window ventana) {
        var recurso = TemaVeterinaria.class.getResource(RUTA_ICONO);
        if (recurso != null) {
            ventana.setIconImage(new ImageIcon(recurso).getImage());
            return;
        }

        if (Files.isRegularFile(ICONO_EN_FUENTES)) {
            ventana.setIconImage(new ImageIcon(ICONO_EN_FUENTES.toString()).getImage());
            return;
        }

        throw new IllegalStateException("No se encontró el icono de la aplicación: " + RUTA_ICONO);
    }

    public static void aplicarTemaGlobal() {
        UIManager.put("Panel.background", FONDO);
        UIManager.put("Button.background", PRIMARIO);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
        UIManager.put("TextField.background", INPUT);
        UIManager.put("TextField.foreground", TEXTO);
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        UIManager.put("ComboBox.background", INPUT);
        UIManager.put("ComboBox.foreground", TEXTO);
        UIManager.put("Label.foreground", TEXTO);
        UIManager.put("Table.background", INPUT);
        UIManager.put("Table.selectionBackground", new Color(158, 214, 201));
        UIManager.put("TableHeader.background", PRIMARIO);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("Table.gridColor", BORDE);
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 12));
    }

    public static void aplicarEstiloVentana(Window ventana) {
        if (ventana instanceof JFrame marco) {
            marco.setResizable(false);
            marco.getContentPane().setBackground(FONDO);
        } else if (ventana instanceof JDialog dialogo) {
            dialogo.setResizable(false);
        }
        Dimension dimensionActual = ventana.getSize();
        Rectangle espacioDisponible = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int anchoAmpliado = Math.min((int) Math.round(dimensionActual.width * 1.2), espacioDisponible.width);
        int altoAmpliado = Math.min((int) Math.round(dimensionActual.height * 1.2), espacioDisponible.height);
        ventana.setSize(anchoAmpliado, altoAmpliado);
        Component referencia = ventana instanceof JDialog dialogo ? dialogo.getOwner() : null;
        ventana.setLocationRelativeTo(referencia);
        estilizarComponentes(ventana);
    }

    private static void estilizarComponentes(Component componente) {
        if (componente instanceof JButton boton) {
            estilizarBotonSegunAccion(boton);
        } else if (componente instanceof JTextComponent campo) {
            campo.setBackground(INPUT);
            campo.setForeground(TEXTO);
            campo.setFont(campo.getFont().deriveFont(Font.PLAIN, Math.max(12, campo.getFont().getSize())));
            campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        } else if (componente instanceof JComboBox<?> combo) {
            aplicarCombo(combo);
        } else if (componente instanceof JSpinner spinner) {
            aplicarSpinner(spinner);
        } else if (componente instanceof JTable tabla) {
            aplicarTabla(tabla);
        } else if (componente instanceof JScrollPane scroll) {
            scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        } else if (componente instanceof JLabel etiqueta) {
            if (etiqueta.getFont().getSize() <= 13) {
                etiqueta.setForeground(TEXTO);
                etiqueta.setFont(etiqueta.getFont().deriveFont(Font.PLAIN));
            }
        } else if (componente instanceof JPanel panel && panel.isOpaque()) {
            Color fondo = panel.getBackground();
            if (fondo == null || fondo.equals(UIManager.getColor("Panel.background"))) {
                panel.setBackground(FONDO);
            }
        }

        if (componente instanceof Container contenedor) {
            for (Component hijo : contenedor.getComponents()) {
                estilizarComponentes(hijo);
            }
        }
    }

    private static void estilizarBotonSegunAccion(JButton boton) {
        String texto = boton.getText() == null ? "" : boton.getText().toLowerCase();
        if (texto.contains("eliminar") || texto.contains("cerrar sesión")) {
            boton.setBackground(INPUT);
            boton.setForeground(ERROR);
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ERROR, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
            ));
        } else if (texto.contains("limpiar") || texto.contains("volver")) {
            boton.setBackground(INPUT);
            boton.setForeground(PRIMARIO);
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
            ));
        } else if (texto.contains("mis animales") || texto.contains("mis citas")
                || texto.contains("solicitar cita") || texto.contains("historial")
                || texto.contains("gestionar")) {
            boton.setBackground(INPUT);
            boton.setForeground(TEXTO);
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        } else {
            boton.setBackground(PRIMARIO);
            boton.setForeground(Color.WHITE);
            boton.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        }

        boton.setFont(boton.getFont().deriveFont(Font.BOLD));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
    }

    private static void aplicarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setForeground(TEXTO);
        tabla.setBackground(INPUT);
        tabla.setSelectionBackground(new Color(158, 214, 201));
        tabla.setSelectionForeground(TEXTO);
        tabla.setGridColor(BORDE);
        tabla.setRowHeight(Math.max(26, tabla.getRowHeight()));
        tabla.setFillsViewportHeight(true);
        tabla.setShowVerticalLines(false);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setBackground(PRIMARIO);
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        encabezado.setOpaque(true);

        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tabla, Object valor, boolean seleccionado, boolean enfocado, int fila, int columna) {
                super.getTableCellRendererComponent(tabla, valor, seleccionado, enfocado, fila, columna);
                if (seleccionado) {
                    setBackground(tabla.getSelectionBackground());
                    setForeground(tabla.getSelectionForeground());
                } else {
                    setBackground(fila % 2 == 0 ? INPUT : FONDO);
                    setForeground(TEXTO);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
        tabla.setDefaultRenderer(Object.class, renderizador);
    }

    public static void aplicarCampo(JTextField campo) {
        campo.setBackground(INPUT);
        campo.setForeground(TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    public static void aplicarCombo(JComboBox<?> combo) {
        combo.setBackground(INPUT);
        combo.setForeground(TEXTO);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBorder(BorderFactory.createLineBorder(BORDE, 1));
    }

    public static void aplicarBoton(JButton boton, Color fondo, Color texto) {
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
    }

    public static void aplicarEtiqueta(JLabel etiqueta) {
        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    public static void aplicarPanelBase(JPanel panel) {
        panel.setBackground(FONDO);
    }

    public static void aplicarSpinner(JSpinner spinner) {
        spinner.setBackground(INPUT);
        spinner.setForeground(TEXTO);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }
}
