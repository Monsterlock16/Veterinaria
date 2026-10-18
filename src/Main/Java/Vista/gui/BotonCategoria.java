package Vista.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

/** Boton grande de los menus principales: tarjeta redondeada con icono dibujado y efecto al pasar el mouse. */
public class BotonCategoria extends JButton {

    public enum Icono { PATA, CALENDARIO, CRUZ, DOCUMENTO }

    private final Icono icono;

    public BotonCategoria(String texto, Icono icono) {
        super(texto);
        this.icono = icono;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth() - 6;
        int h = getHeight() - 6;
        boolean activo = getModel().isRollover() || getModel().isArmed();

        // Sombra y fondo
        g2.setColor(new Color(0, 0, 0, 28));
        g2.fillRoundRect(3, 5, w, h, 26, 26);
        g2.setPaint(activo
                ? new GradientPaint(0, 0, TemaVeterinaria.PRIMARIO.brighter(), w, h, TemaVeterinaria.PRIMARIO_OSCURO)
                : new GradientPaint(0, 0, Color.WHITE, 0, h, TemaVeterinaria.PANEL));
        g2.fillRoundRect(0, 0, w, h, 26, 26);
        g2.setColor(activo ? TemaVeterinaria.PRIMARIO : TemaVeterinaria.BORDE);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 26, 26);

        // Circulo con el icono
        int d = Math.min(64, h / 2);
        int cx = w / 2 - d / 2;
        int cy = (int) (h * 0.17);
        g2.setColor(activo ? new Color(255, 255, 255, 230) : TemaVeterinaria.PRIMARIO);
        g2.fillOval(cx, cy, d, d);
        g2.setColor(activo ? TemaVeterinaria.PRIMARIO : Color.WHITE);
        g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        dibujarIcono(g2, cx + d / 4, cy + d / 4, d / 2);

        // Texto
        g2.setFont(getFont());
        g2.setColor(activo ? Color.WHITE : TemaVeterinaria.TEXTO);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, cy + d + 14 + fm.getAscent() / 2 + (h - cy - d - 14) / 3);
        g2.dispose();
    }

    private void dibujarIcono(Graphics2D g, int x, int y, int s) {
        switch (icono) {
            case PATA:
                LogoVeterinaria.dibujarPata(g, x, y, s);
                break;
            case CALENDARIO:
                g.drawRoundRect(x, y + s / 8, s, s - s / 8, 6, 6);
                g.drawLine(x, y + (int) (s * 0.4), x + s, y + (int) (s * 0.4));
                g.drawLine(x + (int) (s * 0.28), y, x + (int) (s * 0.28), y + s / 4);
                g.drawLine(x + (int) (s * 0.72), y, x + (int) (s * 0.72), y + s / 4);
                g.fillOval(x + (int) (s * 0.22), y + (int) (s * 0.58), 4, 4);
                g.fillOval(x + (int) (s * 0.5) - 2, y + (int) (s * 0.58), 4, 4);
                g.fillOval(x + (int) (s * 0.72), y + (int) (s * 0.58), 4, 4);
                break;
            case CRUZ:
                g.drawRoundRect(x, y, s, s, 8, 8);
                g.fillRect(x + (int) (s * 0.42), y + (int) (s * 0.2), Math.max(3, (int) (s * 0.16)), (int) (s * 0.6));
                g.fillRect(x + (int) (s * 0.2), y + (int) (s * 0.42), (int) (s * 0.6), Math.max(3, (int) (s * 0.16)));
                break;
            case DOCUMENTO:
                g.drawRoundRect(x + s / 8, y, s - s / 4, s, 6, 6);
                for (int i = 1; i <= 3; i++) {
                    g.drawLine(x + (int) (s * 0.3), y + (int) (s * 0.22 * i + s * 0.08),
                               x + (int) (s * 0.7), y + (int) (s * 0.22 * i + s * 0.08));
                }
                break;
            default:
                break;
        }
    }
}
