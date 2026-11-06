package id.ramenku;

import id.ramenku.data.HibernateUtil;
import id.ramenku.ui.Halaman;
import id.ramenku.ui.PanelMenuResep;
import id.ramenku.ui.PanelPenjualan;
import id.ramenku.ui.PanelStokBahan;
import id.ramenku.ui.Ui;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/** Jendela utama RamenKu: beranda dengan tiga fitur, tiap fitur membuka halamannya di jendela yang sama. */
public class JendelaUtama extends JFrame {

    private final CardLayout kartu = new CardLayout();
    private final JPanel isi = new JPanel(kartu);
    private final JLabel status = new JLabel();

    public JendelaUtama(Pengguna pengguna) {
        super("RamenKu");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(batasAtas(pengguna), BorderLayout.NORTH);

        isi.setBackground(Ui.KERTAS);
        isi.add(beranda(), "beranda");
        tambahHalaman("stok", new PanelStokBahan());
        tambahHalaman("menu", new PanelMenuResep());
        tambahHalaman("jual", new PanelPenjualan());
        add(isi, BorderLayout.CENTER);

        setSize(1100, 700);
        setMinimumSize(new Dimension(960, 620));
        setLocationRelativeTo(null);
        cekDatabase();
    }

    private JPanel batasAtas(Pengguna pengguna) {
        JPanel atas = new JPanel(new BorderLayout());
        atas.setBackground(Ui.TINTA);
        atas.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        JLabel judul = new JLabel("RamenKu  |  Halo, " + pengguna.getNama());
        judul.setForeground(Color.WHITE);
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 18f));
        status.setForeground(new Color(0xB8BDC9));
        JButton keluar = new JButton("Keluar");
        keluar.setFocusPainted(false);
        keluar.addActionListener(e -> {
            dispose();
            Main.tampilkanLogin();
        });
        JPanel kanan = new JPanel();
        kanan.setOpaque(false);
        kanan.add(status);
        kanan.add(keluar);
        atas.add(judul, BorderLayout.WEST);
        atas.add(kanan, BorderLayout.EAST);
        return atas;
    }

    private JPanel beranda() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Ui.KERTAS);
        p.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        JPanel judul = new JPanel(new BorderLayout());
        judul.setOpaque(false);
        judul.add(Ui.judul("Beranda"), BorderLayout.NORTH);
        judul.add(Ui.anak("Pilih fitur yang ingin dikerjakan."), BorderLayout.SOUTH);
        judul.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        p.add(judul, BorderLayout.NORTH);

        JPanel tiga = new JPanel(new GridLayout(1, 3, 20, 0));
        tiga.setOpaque(false);
        tiga.add(kotakFitur("Menu dan Resep", "Atur menu, harga, dan bahan yang dipakai per porsi.", "menu"));
        tiga.add(kotakFitur("Stok Bahan", "Pantau persediaan, tandai yang menipis, cetak laporan PDF.", "stok"));
        tiga.add(kotakFitur("Penjualan", "Catat pesanan, simpan nota, dan cetak struk.", "jual"));
        JPanel tengah = new JPanel(new BorderLayout());
        tengah.setOpaque(false);
        tengah.add(tiga, BorderLayout.NORTH);
        tiga.setPreferredSize(new Dimension(0, 220));
        p.add(tengah, BorderLayout.CENTER);
        return p;
    }

    private JButton kotakFitur(String nama, String keterangan, String tujuan) {
        JButton b = new JButton("<html><div style='text-align:center;width:200px'>"
                + "<span style='font-size:17px'><b>" + nama + "</b></span><br><br>"
                + "<span style='color:#6B7080'>" + keterangan + "</span></div></html>");
        b.setBackground(Color.WHITE);
        b.setFocusPainted(false);
        b.addActionListener(e -> tampilkan(tujuan));
        return b;
    }

    private void tambahHalaman(String nama, JComponent halaman) {
        JPanel bungkus = new JPanel(new BorderLayout());
        bungkus.setBackground(Ui.KERTAS);
        JButton kembali = new JButton("←  Beranda");
        kembali.setFocusPainted(false);
        kembali.addActionListener(e -> tampilkan("beranda"));
        JPanel bar = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 24, 12));
        bar.setOpaque(false);
        bar.add(kembali);
        bungkus.add(bar, BorderLayout.NORTH);
        bungkus.add(halaman, BorderLayout.CENTER);
        isi.add(bungkus, nama);
        halamanTerdaftar.put(nama, (Halaman) halaman);
    }

    private final java.util.Map<String, Halaman> halamanTerdaftar = new java.util.HashMap<>();

    private void tampilkan(String nama) {
        Halaman h = halamanTerdaftar.get(nama);
        if (h != null) h.muat();
        kartu.show(isi, nama);
    }

    /** Menampilkan status koneksi Hibernate di pojok kanan atas. */
    private void cekDatabase() {
        try {
            HibernateUtil.cekKoneksi();
            status.setText("Database terhubung   ");
        } catch (RuntimeException e) {
            status.setText("Database belum terhubung   ");
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat terhubung ke database.\nPastikan MySQL di XAMPP sudah menyala.\n\n" + e.getMessage(),
                    "Koneksi gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}
