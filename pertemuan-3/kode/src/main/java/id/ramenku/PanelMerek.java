package id.ramenku;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import javax.swing.JPanel;
import javax.swing.Timer;

/** Panel merek di sisi kiri halaman login: mangkuk ramen dengan uap yang bergerak pelan. */
class PanelMerek extends JPanel {

    static final Color TINTA = new Color(0x1F2430);
    static final Color TINTA_TERANG = new Color(0x2C3345);
    static final Color VERMILION = new Color(0xD94A38);
    static final Color KERTAS = new Color(0xF7F4EF);

    private double fase = 0;
    private final Timer waktu = new Timer(40, e -> {
        fase += 0.05;
        repaint();
    });

    PanelMerek() {
        setPreferredSize(new Dimension(300, 440));
        setOpaque(true);
    }

    @Override public void addNotify() {
        super.addNotify();
        waktu.start();
    }

    @Override public void removeNotify() {
        waktu.stop();
        super.removeNotify();
    }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();

        g.setPaint(new GradientPaint(0, 0, TINTA, 0, h, TINTA_TERANG));
        g.fillRect(0, 0, w, h);

        // lingkaran dekoratif samar
        g.setColor(new Color(255, 255, 255, 10));
        g.fill(new Ellipse2D.Double(-90, h - 200, 280, 280));
        g.setColor(new Color(0xD9, 0x4A, 0x38, 28));
        g.fill(new Ellipse2D.Double(w - 120, -70, 190, 190));

        // tulisan kecil di atas
        g.setFont(new Font("Yu Gothic UI", Font.BOLD, 20));
        g.setColor(VERMILION);
        g.drawString("拉麺", 32, 52);
        g.setColor(new Color(255, 255, 255, 70));
        g.fillRect(32, 64, 36, 2);

        gambarMangkuk(g, w / 2.0, 205);

        // nama dan slogan
        g.setColor(Color.WHITE);
        g.setFont(new Font("Georgia", Font.BOLD, 34));
        g.drawString("RamenKu", 32, h - 86);
        g.dispose();
    }

    private void gambarMangkuk(Graphics2D g, double cx, double cy) {
        // uap
        g.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 3; i++) {
            double geser = (fase * 0.6 + i * 0.33) % 1.0;       // 0..1 naik
            int alfa = (int) (150 * Math.sin(Math.PI * geser));
            if (alfa < 4) continue;
            g.setColor(new Color(255, 255, 255, alfa));
            double x0 = cx - 44 + i * 44;
            double yBawah = cy - 30 - geser * 30;
            Path2D uap = new Path2D.Double();
            boolean awal = true;
            for (double y = yBawah; y > yBawah - 62; y -= 3) {
                double x = x0 + Math.sin(y * 0.09 + fase * 2 + i * 1.7) * 7;
                if (awal) { uap.moveTo(x, y); awal = false; } else uap.lineTo(x, y);
            }
            g.draw(uap);
        }

        // badan mangkuk
        Path2D badan = new Path2D.Double();
        badan.moveTo(cx - 82, cy - 4);
        badan.curveTo(cx - 76, cy + 78, cx + 76, cy + 78, cx + 82, cy - 4);
        badan.closePath();
        g.setPaint(new GradientPaint((float) cx, (float) cy, new Color(0xE2573F),
                (float) cx, (float) (cy + 70), new Color(0xB93A2A)));
        g.fill(badan);
        g.setColor(new Color(255, 255, 255, 40));
        g.setStroke(new BasicStroke(2f));
        g.drawArc((int) (cx - 62), (int) (cy + 18), 124, 38, 200, 140);   // garis hias
        g.setColor(new Color(0x9E2F21));
        g.fillRoundRect((int) (cx - 30), (int) (cy + 53), 60, 9, 6, 6);   // kaki

        // bibir dan kuah
        g.setColor(KERTAS);
        g.fill(new Ellipse2D.Double(cx - 84, cy - 17, 168, 28));
        g.setColor(new Color(0xE9B65A));
        g.fill(new Ellipse2D.Double(cx - 75, cy - 13, 150, 20));

        // mi
        g.setColor(new Color(0xFFF1CF));
        g.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 5; i++) {
            double x = cx - 58 + i * 25;
            Path2D mi = new Path2D.Double();
            mi.moveTo(x, cy - 5);
            mi.curveTo(x + 8, cy - 14, x + 14, cy + 2, x + 24, cy - 7);
            g.draw(mi);
        }

        // telur, daun bawang
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(cx - 30, cy - 13, 30, 20));
        g.setColor(new Color(0xF29A2E));
        g.fill(new Ellipse2D.Double(cx - 21, cy - 8, 12, 11));
        g.setColor(new Color(0x7BA05B));
        for (int i = 0; i < 6; i++) g.fill(new Ellipse2D.Double(cx + 14 + i * 7, cy - 9 + (i % 2) * 4, 5, 5));

        // sumpit
        g.setStroke(new BasicStroke(4.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(0xD9B38A));
        g.drawLine((int) (cx + 24), (int) (cy - 8), (int) (cx + 92), (int) (cy - 92));
        g.setColor(new Color(0xC49A6E));
        g.drawLine((int) (cx + 38), (int) (cy - 6), (int) (cx + 104), (int) (cy - 88));
    }
}
