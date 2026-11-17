package id.ramenku;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import javax.swing.Timer;

/** Panel merek di sisi kiri halaman login: mangkuk ramen dari atas dengan mi yang berputar pelan. */
class PanelMerek extends JPanel {

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

        gambar(g, w, h);
        g.dispose();
    }

    /** Cokelat shoyu hangat dengan mangkuk ramen dilihat dari atas; mi berputar pelan dan uap tipis melintas. */
    private void gambar(Graphics2D g, int w, int h) {
        g.setPaint(new GradientPaint(0, 0, new Color(0x2B1A12), w, h, new Color(0x4C2819)));
        g.fillRect(0, 0, w, h);

        double cx = w / 2.0, cy = h * 0.43;
        g.setPaint(new RadialGradientPaint(new Point2D.Double(cx, cy), 220f, new float[] {0f, 1f},
                new Color[] {new Color(0xFF, 0xB4, 0x5A, 70), new Color(0xFF, 0xB4, 0x5A, 0)}));
        g.fill(new Ellipse2D.Double(cx - 220, cy - 220, 440, 440));

        // aksara kecil di pojok
        g.setFont(new Font("Yu Gothic UI", Font.BOLD, 20));
        g.setColor(new Color(0xF2C56B));
        g.drawString("拉麺", 32, 52);
        g.setColor(new Color(255, 255, 255, 60));
        g.fillRect(32, 64, 36, 2);

        // bayangan dan bibir mangkuk
        g.setColor(new Color(0, 0, 0, 70));
        g.fill(new Ellipse2D.Double(cx - 116, cy - 104, 232, 232));
        g.setPaint(new GradientPaint((float) cx, (float) (cy - 112), new Color(0xFBF3E4),
                (float) cx, (float) (cy + 112), new Color(0xE3D3BA)));
        g.fill(new Ellipse2D.Double(cx - 112, cy - 112, 224, 224));
        g.setColor(new Color(0xC9B391));
        g.setStroke(new BasicStroke(1.2f));
        g.draw(new Ellipse2D.Double(cx - 104, cy - 104, 208, 208));

        // kuah
        g.setPaint(new RadialGradientPaint(new Point2D.Double(cx - 14, cy - 18), 110f, new float[] {0f, 0.7f, 1f},
                new Color[] {new Color(0xF0B556), new Color(0xD08A32), new Color(0xA9631D)}));
        g.fill(new Ellipse2D.Double(cx - 98, cy - 98, 196, 196));

        java.awt.Shape potong = g.getClip();
        g.setClip(new Ellipse2D.Double(cx - 98, cy - 98, 196, 196));

        // mi yang berputar pelan
        g.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 9; i++) {
            double r0 = 14 + i * 10.5;
            Path2D mi = new Path2D.Double();
            for (int s = 0; s <= 90; s++) {
                double sudut = s / 90.0 * Math.PI * 2 + fase * 0.18 * (i % 2 == 0 ? 1 : -1) + i;
                double r = r0 + Math.sin(sudut * 3 + i) * 3.2;
                double x = cx + Math.cos(sudut) * r, y = cy + Math.sin(sudut) * r;
                if (s == 0) mi.moveTo(x, y); else mi.lineTo(x, y);
            }
            mi.closePath();
            g.setColor(new Color(0xFA, 0xE2, 0x9B, i % 2 == 0 ? 235 : 170));
            g.draw(mi);
        }

        // nori
        java.awt.geom.AffineTransform asal = g.getTransform();
        g.rotate(Math.toRadians(-18), cx + 54, cy - 2);
        g.setColor(new Color(0x16241B));
        g.fillRoundRect((int) (cx + 36), (int) (cy - 34), 30, 62, 4, 4);
        g.setColor(new Color(0x2A4030));
        g.setStroke(new BasicStroke(1f));
        for (int k = 0; k < 4; k++) g.drawLine((int) (cx + 40), (int) (cy - 26 + k * 14), (int) (cx + 62), (int) (cy - 26 + k * 14));
        g.setTransform(asal);

        // telur ajitama (belah dua)
        g.setColor(new Color(0xFFF8EC));
        g.fill(new Ellipse2D.Double(cx - 58, cy - 60, 40, 30));
        g.setColor(new Color(0xE9922F));
        g.fill(new Ellipse2D.Double(cx - 46, cy - 52, 17, 15));
        g.setColor(new Color(255, 255, 255, 140));
        g.fill(new Ellipse2D.Double(cx - 43, cy - 50, 6, 4));

        // chashu
        g.setColor(new Color(0x8C4A2B));
        g.fill(new Ellipse2D.Double(cx - 62, cy + 14, 48, 40));
        g.setColor(new Color(0xB86F47));
        g.fill(new Ellipse2D.Double(cx - 56, cy + 20, 36, 28));
        g.setColor(new Color(0xE8C4A0));
        g.setStroke(new BasicStroke(2f));
        g.drawArc((int) (cx - 50), (int) (cy + 26), 24, 16, 20, 200);

        // narutomaki
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(cx + 6, cy - 72, 34, 34));
        g.setColor(new Color(0xF18AA3));
        g.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D pusar = new Path2D.Double();
        for (int s = 0; s <= 40; s++) {
            double sudut = s / 40.0 * Math.PI * 3.2;
            double r = 1.5 + s / 40.0 * 12;
            double x = cx + 23 + Math.cos(sudut) * r, y = cy - 55 + Math.sin(sudut) * r;
            if (s == 0) pusar.moveTo(x, y); else pusar.lineTo(x, y);
        }
        g.draw(pusar);

        // daun bawang
        g.setColor(new Color(0x7DB05F));
        double[][] titik = {{22, 28}, {34, 38}, {12, 40}, {44, 22}, {30, 10}, {-6, 56}, {-30, 66}, {-72, 24}, {-66, -6}, {70, -26}, {58, 44}, {4, 70}};
        for (double[] t : titik) g.fill(new Ellipse2D.Double(cx + t[0], cy + t[1], 7, 7));
        g.setClip(potong);

        // sumpit tergeletak di tepi mangkuk
        g.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(new Color(0xD9B38A));
        g.drawLine((int) (cx + 18), (int) (cy + 122), (int) (cx + 126), (int) (cy + 84));
        g.setColor(new Color(0xBF8F62));
        g.drawLine((int) (cx + 30), (int) (cy + 138), (int) (cx + 138), (int) (cy + 98));

        // uap tipis yang melintas pelan
        g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 3; i++) {
            double geser = (fase * 0.25 + i * 0.33) % 1.0;
            int alfa = (int) (80 * Math.sin(Math.PI * geser));
            if (alfa < 4) continue;
            g.setColor(new Color(255, 248, 232, alfa));
            double x0 = cx - 40 + i * 40;
            double yBawah = cy - 70 - geser * 26;
            Path2D uap = new Path2D.Double();
            boolean awal = true;
            for (double y = yBawah; y > yBawah - 46; y -= 3) {
                double x = x0 + Math.sin(y * 0.09 + fase * 2 + i * 1.7) * 6;
                if (awal) { uap.moveTo(x, y); awal = false; } else uap.lineTo(x, y);
            }
            g.draw(uap);
        }

        // nama
        g.setFont(new Font("Georgia", Font.PLAIN, 38));
        g.setColor(new Color(0xFBEEDA));
        g.drawString("RamenKu", 32, h - 74);
        g.setColor(new Color(0xDD8A2D));
        g.fillRect(34, h - 60, 44, 2);
        Font kecil = new Font("Segoe UI", Font.PLAIN, 12)
                .deriveFont(java.util.Map.of(java.awt.font.TextAttribute.TRACKING, 0.3f));
        g.setFont(kecil);
        g.setColor(new Color(0xD9B98F));
        g.drawString("KEDAI RAMEN", 34, h - 38);
    }
}
