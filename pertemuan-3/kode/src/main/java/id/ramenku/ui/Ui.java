package id.ramenku.ui;

import id.ramenku.Angka;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.swing.Box;
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
        } else if (e instanceof SQLException) {
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
