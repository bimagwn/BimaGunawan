package id.ramenku.oop.ui;

import id.ramenku.oop.model.MenuMakanan;
import id.ramenku.oop.model.MenuMinuman;
import id.ramenku.oop.model.Pesanan;
import id.ramenku.oop.model.Produk;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 * Jendela utama aplikasi. Setiap tombol menjalankan satu konsep OOP,
 * dan hasilnya dicatat di panel "Catatan Konsep OOP" di bagian bawah.
 */
public class JendelaUtama extends JFrame {

    private static final Color MERAH = new Color(0xC8, 0x37, 0x2D);

    // data
    private final List<Produk> daftarProduk = new ArrayList<>();
    private final Pesanan pesanan = new Pesanan();

    // formulir pembuatan objek
    private final JComboBox<String> cbJenis = new JComboBox<>(new String[]{"Makanan", "Minuman"});
    private final JTextField tfNama = new JTextField(14);
    private final JTextField tfHarga = new JTextField(14);
    private final JSpinner spPedas = new JSpinner(new SpinnerNumberModel(2, 0, 5, 1));
    private final JCheckBox chDingin = new JCheckBox("Dingin", true);

    // tabel objek
    private final DefaultTableModel modelTabel = new DefaultTableModel(
            new String[]{"Kode", "Class asli objek", "Nama", "Harga dasar", "deskripsi() (polimorfik)"}, 0) {
        @Override
        public boolean isCellEditable(int baris, int kolom) {
            return false;
        }
    };
    private final JTable tabel = new JTable(modelTabel);

    // perhitungan harga
    private final JSpinner spJumlah = new JSpinner(new SpinnerNumberModel(2, 1, 99, 1));
    private final JComboBox<String> cbMode = new JComboBox<>(new String[]{
            "hitungHarga(jumlah)", "hitungHarga(jumlah, diskonPersen)", "hitungHarga(jumlah, kodePromo)"});
    private final JTextField tfParam = new JTextField("10", 8);
    private final JLabel lblHasil = new JLabel("Hasil: -");

    // pesanan dan catatan
    private final JTextArea taPesanan = new JTextArea();
    private final JLabel lblTotal = new JLabel("Total pesanan: Rp 0");
    private final JTextArea taCatatan = new JTextArea();

    public JendelaUtama() {
        super("RamenKu OOP - Encapsulation, Inheritance, Polymorphism");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel judul = new JLabel("  RamenKu OOP");
        judul.setOpaque(true);
        judul.setBackground(MERAH);
        judul.setForeground(Color.WHITE);
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 22f));
        judul.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane tengah = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelAtas(), panelBawah());
        tengah.setResizeWeight(0.58);

        JPanel isi = new JPanel(new BorderLayout());
        isi.add(judul, BorderLayout.NORTH);
        isi.add(panelFormulir(), BorderLayout.WEST);
        isi.add(tengah, BorderLayout.CENTER);
        setContentPane(isi);

        // beberapa objek contoh supaya tabel tidak kosong saat pertama dibuka
        tambahProduk(new MenuMakanan("Ramen Original", 25000, 1), false);
        tambahProduk(new MenuMakanan("Ramen Pedas", 28000, 3), false);
        tambahProduk(new MenuMinuman("Es Teh", 5000, true), false);
        catat("Class & Object", "3 objek contoh dibuat dengan perintah new: 2 MenuMakanan dan 1 MenuMinuman.");

        pilihBarisPertama();
        setMinimumSize(new Dimension(980, 600));
        setSize(1180, 760);
        setLocationRelativeTo(null);
        tengah.setDividerLocation(360); // bagian atas (tabel) dan bawah (pesanan, catatan) berimbang
    }

    // ============================== TATA LETAK ==============================

    private JPanel panelFormulir() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 6),
                BorderFactory.createTitledBorder("1. Buat objek (new)")));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        baris(p, c, 0, "Jenis (class)", cbJenis);
        baris(p, c, 1, "Nama", tfNama);
        baris(p, c, 2, "Harga (Rp)", tfHarga);
        baris(p, c, 3, "Tingkat pedas (0-5)", spPedas);
        baris(p, c, 4, "Minuman", chDingin);

        JButton buat = new JButton("Buat objek");
        buat.addActionListener(e -> buatObjek());
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        c.insets = new Insets(14, 6, 4, 6);
        p.add(buat, c);

        JLabel petunjuk = new JLabel("<html><i>Harga negatif atau nama kosong<br>akan ditolak oleh setter.</i></html>");
        c.gridy = 6;
        c.insets = new Insets(2, 6, 2, 6);
        p.add(petunjuk, c);

        // pengisi ruang di bawah supaya semua isian menempel di bagian atas panel
        c.gridy = 7;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        p.add(new JLabel(), c);

        cbJenis.addActionListener(e -> aturKolom());
        aturKolom();
        p.setPreferredSize(new Dimension(280, 0));
        return p;
    }

    private void baris(JPanel p, GridBagConstraints c, int y, String teks, java.awt.Component isi) {
        c.gridy = y; c.gridwidth = 1;
        c.gridx = 0; c.weightx = 0;
        p.add(new JLabel(teks), c);
        c.gridx = 1; c.weightx = 1;
        p.add(isi, c);
    }

    private void aturKolom() {
        boolean makanan = "Makanan".equals(cbJenis.getSelectedItem());
        spPedas.setEnabled(makanan);
        chDingin.setEnabled(!makanan);
    }

    private JPanel panelAtas() {
        tabel.setRowHeight(24);
        tabel.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabel.getColumnModel().getColumn(4).setPreferredWidth(360);

        JPanel hitung = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        hitung.setBorder(BorderFactory.createTitledBorder("3. Hitung harga objek terpilih (overloading dan overriding)"));
        hitung.add(new JLabel("Jumlah"));
        hitung.add(spJumlah);
        hitung.add(cbMode);
        hitung.add(new JLabel("Diskon / promo  "));
        hitung.add(tfParam);
        JButton bHitung = new JButton("Hitung");
        bHitung.addActionListener(e -> hitung());
        hitung.add(bHitung);
        JButton bUbah = new JButton("Ubah harga (setter)");
        bUbah.addActionListener(e -> {
            String teks = JOptionPane.showInputDialog(this, "Harga baru untuk objek terpilih:");
            if (teks != null) {
                ubahHarga(teks);
            }
        });
        hitung.add(bUbah);
        lblHasil.setFont(lblHasil.getFont().deriveFont(Font.BOLD, 14f));
        hitung.add(lblHasil);

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 6, 0, 10));
        JScrollPane sp = new JScrollPane(tabel);
        sp.setBorder(BorderFactory.createTitledBorder("2. Daftar objek (satu daftar bertipe Produk berisi campuran subclass)"));
        p.add(sp, BorderLayout.CENTER);
        p.add(hitung, BorderLayout.SOUTH);
        return p;
    }

    private JSplitPane panelBawah() {
        taPesanan.setEditable(false);
        taPesanan.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JButton tambah = new JButton("Tambah objek terpilih ke pesanan");
        tambah.addActionListener(e -> tambahKePesanan());
        JButton kosong = new JButton("Kosongkan");
        kosong.addActionListener(e -> {
            pesanan.kosongkan();
            segarkanPesanan();
            catat("Pesanan", "Pesanan dikosongkan.");
        });
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        tombol.add(tambah);
        tombol.add(kosong);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));

        JPanel kiri = new JPanel(new BorderLayout());
        kiri.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 6, 10, 4),
                BorderFactory.createTitledBorder("4. Pesanan (polimorfisme pada total)")));
        kiri.add(tombol, BorderLayout.NORTH);
        kiri.add(new JScrollPane(taPesanan), BorderLayout.CENTER);
        kiri.add(lblTotal, BorderLayout.SOUTH);

        taCatatan.setEditable(false);
        taCatatan.setLineWrap(true);
        taCatatan.setWrapStyleWord(true);
        JScrollPane spCatatan = new JScrollPane(taCatatan);
        spCatatan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 10, 10),
                BorderFactory.createTitledBorder("Catatan konsep OOP (otomatis)")));

        JSplitPane s = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, kiri, spCatatan);
        s.setResizeWeight(0.45);
        return s;
    }

    // ============================== AKSI (satu per konsep) ==============================

    /** Class dan Object: membuat objek baru dengan new. Inheritance: tipe objek bergantung pada subclass yang dipilih. */
    public void buatObjek() {
        try {
            double harga = angka(tfHarga.getText());
            Produk baru;
            if ("Makanan".equals(cbJenis.getSelectedItem())) {
                baru = new MenuMakanan(tfNama.getText(), harga, (Integer) spPedas.getValue());
                catat("Class & Object / Inheritance", "new MenuMakanan(...) dibuat. MenuMakanan mewarisi Produk lewat extends, "
                        + "sehingga memiliki nama, harga, dan hitungHarga() tanpa menulis ulang.");
            } else {
                baru = new MenuMinuman(tfNama.getText(), harga, chDingin.isSelected());
                catat("Class & Object / Inheritance", "new MenuMinuman(...) dibuat. MenuMinuman juga mewarisi Produk lewat extends.");
            }
            tambahProduk(baru, true);
            tfNama.setText("");
            tfHarga.setText("");
        } catch (IllegalArgumentException e) {
            catat("Encapsulation", "Setter menolak data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Data ditolak", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Encapsulation: harga (private) hanya dapat diubah lewat setHarga(), yang memeriksa nilainya. */
    public void ubahHarga(String teks) {
        Produk p = terpilih();
        if (p == null) {
            return;
        }
        try {
            double lama = p.getHarga();
            p.setHarga(angka(teks));
            muatTabel();
            catat("Encapsulation", p.getNama() + ": setHarga() mengubah harga dari " + Produk.rupiah(lama) + " menjadi "
                    + Produk.rupiah(p.getHarga()) + ".");
        } catch (IllegalArgumentException e) {
            catat("Encapsulation", "setHarga() menolak nilai tersebut: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Data ditolak", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Polymorphism: overloading (tiga versi hitungHarga) dan overriding (versi tiap subclass dipakai otomatis). */
    public void hitung() {
        Produk p = terpilih();
        if (p == null) {
            return;
        }
        try {
            int jumlah = (Integer) spJumlah.getValue();
            double hasil;
            String pemanggilan;
            switch (cbMode.getSelectedIndex()) {
                case 1:
                    double diskon = angka(tfParam.getText());
                    hasil = p.hitungHarga(jumlah, diskon);
                    pemanggilan = "hitungHarga(" + jumlah + ", " + diskon + ")";
                    break;
                case 2:
                    hasil = p.hitungHarga(jumlah, tfParam.getText());
                    pemanggilan = "hitungHarga(" + jumlah + ", \"" + tfParam.getText() + "\")";
                    break;
                default:
                    hasil = p.hitungHarga(jumlah);
                    pemanggilan = "hitungHarga(" + jumlah + ")";
            }
            lblHasil.setText("Hasil: " + Produk.rupiah(hasil));
            catat("Polymorphism", p.getClass().getSimpleName() + "." + pemanggilan + " = " + Produk.rupiah(hasil)
                    + ". Overloading: dipilih versi method sesuai parameter. Overriding: yang dijalankan versi milik "
                    + p.getClass().getSimpleName() + ", bukan versi Produk.");
        } catch (IllegalArgumentException e) {
            catat("Polymorphism", "Perhitungan ditolak: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Data ditolak", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Polymorphism: total() memanggil hitungHarga() pada tiap Produk, tetapi versi yang jalan mengikuti class aslinya. */
    public void tambahKePesanan() {
        Produk p = terpilih();
        if (p == null) {
            return;
        }
        int jumlah = (Integer) spJumlah.getValue();
        pesanan.tambah(p, jumlah);
        segarkanPesanan();
        catat("Polymorphism", "Pesanan.total() memanggil hitungHarga() pada setiap Produk. Isi daftar campuran "
                + "MenuMakanan dan MenuMinuman, dan masing-masing memakai hitungHarga() miliknya sendiri.");
    }

    // ============================== BANTUAN ==============================

    private void tambahProduk(Produk p, boolean pilih) {
        daftarProduk.add(p);
        muatTabel();
        if (pilih) {
            int terakhir = tabel.getRowCount() - 1;
            tabel.setRowSelectionInterval(terakhir, terakhir);
        }
    }

    private void muatTabel() {
        int pilihan = tabel.getSelectedRow();
        modelTabel.setRowCount(0);
        for (Produk p : daftarProduk) {
            modelTabel.addRow(new Object[]{kodeTampil(p), p.getClass().getSimpleName(), p.getNama(),
                    Produk.rupiah(p.getHarga()), p.deskripsi()});
        }
        if (pilihan >= 0 && pilihan < tabel.getRowCount()) {
            tabel.setRowSelectionInterval(pilihan, pilihan);
        }
    }

    private String kodeTampil(Produk p) {
        return String.format("#%02d", daftarProduk.indexOf(p) + 1);
    }

    private void pilihBarisPertama() {
        if (tabel.getRowCount() > 0) {
            tabel.setRowSelectionInterval(0, 0);
        }
    }

    private Produk terpilih() {
        int i = tabel.getSelectedRow();
        if (i < 0 || i >= daftarProduk.size()) {
            JOptionPane.showMessageDialog(this, "Pilih satu objek di tabel dulu.", "RamenKu OOP", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return daftarProduk.get(i);
    }

    private void segarkanPesanan() {
        StringBuilder sb = new StringBuilder();
        for (String baris : pesanan.rincian()) {
            sb.append(baris).append('\n');
        }
        taPesanan.setText(sb.toString());
        lblTotal.setText("Total pesanan: " + Produk.rupiah(pesanan.total()));
    }

    private void catat(String konsep, String pesan) {
        String jam = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        taCatatan.append("[" + jam + "] " + konsep + ": " + pesan + "\n\n");
        taCatatan.setCaretPosition(taCatatan.getDocument().getLength());
    }

    /** Membaca angka berformat Indonesia (25.000 atau 25000, koma sebagai desimal). */
    private static double angka(String teks) {
        String t = teks == null ? "" : teks.trim().replace(" ", "");
        if (t.isEmpty()) {
            throw new IllegalArgumentException("Isian angka tidak boleh kosong");
        }
        if (t.contains(",")) {
            t = t.replace(".", "").replace(",", ".");
        } else if (t.matches("\\d{1,3}(\\.\\d{3})+")) {
            t = t.replace(".", "");
        }
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Isian harus berupa angka");
        }
    }
}
