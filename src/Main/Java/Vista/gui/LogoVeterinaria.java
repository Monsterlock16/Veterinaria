package Vista.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/** Logo de la clinica: circulo con una patita, dibujado por codigo (no necesita imagen). */
public class LogoVeterinaria extends JComponent {

    public LogoVeterinaria(int tamano) {
        setPreferredSize(new Dimension(tamano, tamano));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int t = Math.min(getWidth(), getHeight());
        g2.setPaint(new GradientPaint(0, 0, TemaVeterinaria.PRIMARIO.brighter(), t, t, TemaVeterinaria.PRIMARIO.darker()));
        g2.fillOval(0, 0, t, t);
        g2.setColor(Color.WHITE);
        dibujarPata(g2, (int) (t * 0.22), (int) (t * 0.22), (int) (t * 0.56));
        g2.dispose();
    }

    /** Dibuja una patita de animal con su esquina superior izquierda en (x, y) y de ancho/alto s. */
    static void dibujarPata(Graphics2D g, int x, int y, int s) {
        g.fillOval(x + (int) (0.22 * s), y + (int) (0.50 * s), (int) (0.56 * s), (int) (0.46 * s));
        g.fillOval(x, y + (int) (0.30 * s), (int) (0.20 * s), (int) (0.26 * s));
        g.fillOval(x + (int) (0.20 * s), y + (int) (0.06 * s), (int) (0.20 * s), (int) (0.30 * s));
        g.fillOval(x + (int) (0.60 * s), y + (int) (0.06 * s), (int) (0.20 * s), (int) (0.30 * s));
        g.fillOval(x + (int) (0.80 * s), y + (int) (0.30 * s), (int) (0.20 * s), (int) (0.26 * s));
    }
}
