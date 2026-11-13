package id.ramenku.ui;

import id.ramenku.Angka;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.math.BigDecimal;
import jakarta.persistence.PersistenceException;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

/** Tampilan dan perkakas bersama untuk semua halaman: warna, tabel, tombol, dan kotak pesan. */
public final class Ui {

    public static final Color TINTA = new Color(0x1F2430);
    public static final Color KERTAS = new Color(0xF7F4EF);
    public static final Color VERMILION = new Color(0xD94A38);
    public static final Color ABU = new Color(0x6B7080);
    public static final Color GARIS = new Color(0xE3DED5);

    private Ui() {}

    /** Pekerjaan yang boleh melempar galat; galatnya ditampilkan oleh coba(). */
    @FunctionalInterface
    public interface Aksi {
        void jalan() throws Exception;
    }

    /** Menjalankan aksi dan menampilkan pesan yang sesuai bila gagal. */
    public static void coba(Component induk, Aksi aksi) {
        try {
            aksi.jalan();
        } catch (Exception e) {
            galat(induk, e);
        }
    }

    public static void galat(Component induk, Throwable e) {
        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
            JOptionPane.showMessageDialog(induk, e.getMessage(), "Periksa kembali", JOptionPane.WARNING_MESSAGE);
        } else if (e instanceof SQLException || e instanceof PersistenceException || e instanceof HibernateException) {
            JOptionPane.showMessageDialog(induk, "Terjadi masalah pada database.\nPastikan MySQL di XAMPP masih menyala.\n\n" + e.getMessage(),
                    "Database", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(induk, "Terjadi kesalahan: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void info(Component induk, String pesan) {
        JOptionPane.showMessageDialog(induk, pesan, "RamenKu", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean tanya(Component induk, String pesan) {
        return JOptionPane.showConfirmDialog(induk, pesan, "Konfirmasi", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /** Membaca isian angka gaya Indonesia; pesan galatnya menyebut nama isiannya. */
    public static BigDecimal angka(String teks, String nama) {
        try {
            return Angka.baca(teks);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " harus berupa angka, misalnya 5.000 atau 2,5.");
        }
    }

    public static JButton utama(String teks) {
        JButton b = new JButton(teks);
        b.setBackground(VERMILION);
        b.setForeground(Color.WHITE);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 16, 36));
        return b;
    }

    public static JButton biasa(String teks) {
        JButton b = new JButton(teks);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 16, 36));
        return b;
    }

    /**
     * Tombol unduh berwarna dengan ikon berkas: PDF merah, Word biru, dan Excel hijau.
     *
     * @param jenis "PDF", "WORD", atau "EXCEL"
     */
    public static JButton tombolUnduh(String teks, String jenis) {
        Color dasar;
        Color sorot;
        Color tekan;
        String huruf;
        switch (jenis) {
            case "WORD" -> { dasar = new Color(0x2B579A); sorot = new Color(0x3A6DB8); tekan = new Color(0x1F4380); huruf = "DOC"; }
            case "EXCEL" -> { dasar = new Color(0x1D7044); sorot = new Color(0x27905A); tekan = new Color(0x155733); huruf = "XLS"; }
            default -> { dasar = new Color(0xD94A38); sorot = new Color(0xE5604E); tekan = new Color(0xB93A2A); huruf = "PDF"; }
        }
        JButton b = new JButton(teks, new IkonBerkas(huruf, dasar));
        b.setForeground(Color.WHITE);
        b.setBackground(dasar);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setFocusPainted(false);
        b.setIconTextGap(9);
        b.putClientProperty("FlatLaf.style", "background: " + hex(dasar) + "; foreground: #FFFFFF; hoverBackground: " + hex(sorot)
                + "; pressedBackground: " + hex(tekan) + "; borderWidth: 0; arc: 14");
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 18, 38));
        return b;
    }

    private static String hex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    /** Ikon berkas: kertas putih dengan sudut terlipat dan tulisan jenis berkasnya (PDF, DOC, atau XLS). */
    private static final class IkonBerkas implements Icon {
        private final String huruf;
        private final Color warna;

        IkonBerkas(String huruf, Color warna) {
            this.huruf = huruf;
            this.warna = warna;
        }

        @Override public int getIconWidth() {
            return 22;
        }

        @Override public int getIconHeight() {
            return 24;
        }

        @Override public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.translate(x, y);
            // kertas dengan sudut kanan atas terlipat
            Path2D kertas = new Path2D.Float();
            kertas.moveTo(3, 1.5);
            kertas.lineTo(13.5, 1.5);
            kertas.lineTo(19, 7);
            kertas.lineTo(19, 21.5);
            kertas.quadTo(19, 22.5, 18, 22.5);
            kertas.lineTo(4, 22.5);
            kertas.quadTo(3, 22.5, 3, 21.5);
            kertas.closePath();
            g2.setColor(Color.WHITE);
            g2.fill(kertas);
            // lipatan
            Path2D lipat = new Path2D.Float();
            lipat.moveTo(13.5, 1.5);
            lipat.lineTo(13.5, 5.8);
            lipat.quadTo(13.5, 7, 14.7, 7);
            lipat.lineTo(19, 7);
            lipat.closePath();
            g2.setColor(new Color(255, 255, 255, 150));
            g2.fill(lipat);
            // pita berwarna berisi jenis berkas
            g2.setColor(warna.darker().darker());
            g2.fillRoundRect(1, 12, 20, 8, 3, 3);
            g2.setColor(new Color(255, 255, 255, 210));
            g2.setStroke(new java.awt.BasicStroke(1f));
            g2.drawRoundRect(1, 12, 20, 8, 3, 3);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
            int lebar = g2.getFontMetrics().stringWidth(huruf);
            g2.drawString(huruf, 1 + (20 - lebar) / 2f, 18.4f);
            g2.dispose();
        }
    }

    public static JLabel judul(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(new Font("Georgia", Font.BOLD, 24));
        l.setForeground(TINTA);
        return l;
    }

    public static JLabel anak(String teks) {
        JLabel l = new JLabel(teks);
        l.setForeground(ABU);
        return l;
    }

    public static JLabel kecil(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 11f));
        l.setForeground(ABU);
        return l;
    }

    public static JTextField isian() {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(220, 36));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return t;
    }

    /** Kartu putih berbingkai tipis, tempat kelompok isian atau tabel diletakkan. */
    public static JPanel kartu(LayoutManager tata) {
        JPanel p = new JPanel(tata);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(GARIS, 1, true), BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return p;
    }

    /** Panel isian bertumpuk ke bawah (label kecil di atas tiap kolom). */
    public static JPanel form() {
        JPanel f = new JPanel();
        f.setOpaque(false);
        f.setLayout(new javax.swing.BoxLayout(f, javax.swing.BoxLayout.Y_AXIS));
        return f;
    }

    public static void baris(JPanel form, String label, JComponent isi) {
        JLabel l = kecil(label);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        isi.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(l);
        form.add(Box.createVerticalStrut(4));
        form.add(isi);
        form.add(Box.createVerticalStrut(12));
    }

    /** Tabel dasar: baris tidak dapat diedit, hanya dipilih. */
    public static DefaultTableModel model(String... kolom) {
        return new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int baris, int kolom) {
                return false;
            }
        };
    }

    public static JTable tabel(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(32);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(GARIS);
        t.setFillsViewportHeight(true);
        t.setSelectionBackground(new Color(0xFBE3DF));
        t.setSelectionForeground(TINTA);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setPreferredSize(new Dimension(0, 34));
        t.getTableHeader().setBackground(TINTA);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(t.getTableHeader().getFont().deriveFont(Font.BOLD, 12f));
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        return t;
    }

    public static JScrollPane gulir(JTable t) {
        JScrollPane s = new JScrollPane(t);
        s.setBorder(new LineBorder(GARIS, 1, true));
        s.getViewport().setBackground(Color.WHITE);
        return s;
    }
}
