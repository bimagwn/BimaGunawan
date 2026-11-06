package id.ramenku.ui;

import id.ramenku.Angka;
import id.ramenku.data.Bahan;
import id.ramenku.data.BahanDao;
import id.ramenku.data.Menu;
import id.ramenku.data.MenuDao;
import id.ramenku.data.ResepBaris;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Fitur Menu dan Resep: kelola menu dan harganya, lalu tentukan bahan yang dipakai per porsi. */
public class PanelMenuResep extends JPanel implements Halaman {

    private final MenuDao menuDao = new MenuDao();
    private final BahanDao bahanDao = new BahanDao();

    private final DefaultTableModel modelMenu = Ui.model("Menu", "Harga");
    private final JTable tabelMenu = Ui.tabel(modelMenu);
    private List<Menu> menus = new ArrayList<>();
    private final JTextField txtNama = Ui.isian();
    private final JTextField txtHarga = Ui.isian();

    private final DefaultTableModel modelResep = Ui.model("Bahan", "Jumlah per porsi", "Satuan");
    private final JTable tabelResep = Ui.tabel(modelResep);
    private List<ResepBaris> resep = new ArrayList<>();
    private final JLabel judulResep = new JLabel("Resep");
    private final JComboBox<Bahan> pilihBahan = new JComboBox<>();
    private final JTextField txtJumlah = Ui.isian();
    private final JButton btnSimpanBahan = Ui.utama("Simpan Bahan");
    private final JButton btnHapusBahan = Ui.biasa("Hapus Bahan");

    public PanelMenuResep() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Ui.KERTAS);
        setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));

        JPanel judul = new JPanel(new BorderLayout());
        judul.setOpaque(false);
        judul.add(Ui.judul("Menu dan Resep"), BorderLayout.NORTH);
        judul.add(Ui.anak("Atur menu dan bahan yang terpakai untuk satu porsi. Resep ini dipakai untuk mengurangi stok saat penjualan."), BorderLayout.SOUTH);
        add(judul, BorderLayout.NORTH);

        JPanel isi = new JPanel(new GridLayout(1, 2, 16, 0));
        isi.setOpaque(false);
        isi.add(kartuMenu());
        isi.add(kartuResep());
        add(isi, BorderLayout.CENTER);

        tabelMenu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) menuDipilih();
        });
        tabelResep.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) resepDipilih();
        });
        aturResep(false);
    }

    private JPanel kartuMenu() {
        JPanel kartu = Ui.kartu(new BorderLayout(0, 12));
        JLabel j = new JLabel("Daftar Menu");
        j.setFont(j.getFont().deriveFont(Font.BOLD, 15f));
        kartu.add(j, BorderLayout.NORTH);
        kartu.add(Ui.gulir(tabelMenu), BorderLayout.CENTER);

        JPanel bawah = new JPanel(new BorderLayout(0, 8));
        bawah.setOpaque(false);
        JPanel f = Ui.form();
        Ui.baris(f, "NAMA MENU", txtNama);
        Ui.baris(f, "HARGA (Rp)", txtHarga);
        bawah.add(f, BorderLayout.CENTER);
        JButton tambah = Ui.utama("Tambah");
        JButton ubah = Ui.biasa("Ubah");
        JButton hapus = Ui.biasa("Hapus");
        JButton kosong = Ui.biasa("Kosongkan");
        tambah.addActionListener(e -> Ui.coba(this, this::tambahMenu));
        ubah.addActionListener(e -> Ui.coba(this, this::ubahMenu));
        hapus.addActionListener(e -> Ui.coba(this, this::hapusMenu));
        kosong.addActionListener(e -> kosongkanMenu());
        JPanel tombol = new JPanel(new GridLayout(1, 4, 8, 0));
        tombol.setOpaque(false);
        tombol.add(tambah);
        tombol.add(ubah);
        tombol.add(hapus);
        tombol.add(kosong);
        bawah.add(tombol, BorderLayout.SOUTH);
        kartu.add(bawah, BorderLayout.SOUTH);
        return kartu;
    }

    private JPanel kartuResep() {
        JPanel kartu = Ui.kartu(new BorderLayout(0, 12));
        judulResep.setFont(judulResep.getFont().deriveFont(Font.BOLD, 15f));
        kartu.add(judulResep, BorderLayout.NORTH);
        kartu.add(Ui.gulir(tabelResep), BorderLayout.CENTER);

        JPanel bawah = new JPanel(new BorderLayout(0, 8));
        bawah.setOpaque(false);
        JPanel f = Ui.form();
        pilihBahan.setPreferredSize(new java.awt.Dimension(220, 36));
        pilihBahan.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 36));
        Ui.baris(f, "BAHAN", pilihBahan);
        Ui.baris(f, "JUMLAH PER PORSI", txtJumlah);
        bawah.add(f, BorderLayout.CENTER);
        btnSimpanBahan.addActionListener(e -> Ui.coba(this, this::simpanBahan));
        btnHapusBahan.addActionListener(e -> Ui.coba(this, this::hapusBahan));
        JPanel tombol = new JPanel(new GridLayout(1, 2, 8, 0));
        tombol.setOpaque(false);
        tombol.add(btnSimpanBahan);
        tombol.add(btnHapusBahan);
        bawah.add(tombol, BorderLayout.SOUTH);
        kartu.add(bawah, BorderLayout.SOUTH);
        return kartu;
    }

    @Override public void muat() {
        Ui.coba(this, () -> {
            int idLama = menuTerpilihId();
            menus = menuDao.semua();
            modelMenu.setRowCount(0);
            int pilih = -1;
            for (int i = 0; i < menus.size(); i++) {
                Menu m = menus.get(i);
                modelMenu.addRow(new Object[] {m.getNama(), Angka.rupiah(m.getHarga())});
                if (m.getId() == idLama) pilih = i;
            }
            pilihBahan.removeAllItems();
            for (Bahan b : bahanDao.semua()) pilihBahan.addItem(b);
            if (pilih >= 0) tabelMenu.setRowSelectionInterval(pilih, pilih);
            else menuDipilih();
        });
    }

    private int menuTerpilihId() {
        int baris = tabelMenu.getSelectedRow();
        return baris < 0 || baris >= menus.size() ? -1 : menus.get(baris).getId();
    }

    private Menu menuDipilihObj() {
        int baris = tabelMenu.getSelectedRow();
        if (baris < 0) throw new IllegalStateException("Pilih menu di tabel terlebih dahulu.");
        return menus.get(baris);
    }

    private void menuDipilih() {
        int baris = tabelMenu.getSelectedRow();
        if (baris < 0 || baris >= menus.size()) {
            judulResep.setText("Resep");
            modelResep.setRowCount(0);
            resep = new ArrayList<>();
            aturResep(false);
            return;
        }
        Menu m = menus.get(baris);
        txtNama.setText(m.getNama());
        txtHarga.setText(Angka.tulis(m.getHarga()));
        judulResep.setText("Resep: " + m.getNama());
        aturResep(true);
        Ui.coba(this, this::muatResep);
    }

    private void muatResep() throws Exception {
        resep = menuDao.resep(menuDipilihObj().getId());
        modelResep.setRowCount(0);
        for (ResepBaris r : resep) modelResep.addRow(new Object[] {r.getBahanNama(), Angka.tulis(r.getJumlah()), r.getSatuan()});
    }

    private void resepDipilih() {
        int baris = tabelResep.getSelectedRow();
        if (baris < 0 || baris >= resep.size()) return;
        ResepBaris r = resep.get(baris);
        for (int i = 0; i < pilihBahan.getItemCount(); i++) {
            if (pilihBahan.getItemAt(i).getId() == r.getBahanId()) pilihBahan.setSelectedIndex(i);
        }
        txtJumlah.setText(Angka.tulis(r.getJumlah()));
    }

    private void aturResep(boolean aktif) {
        pilihBahan.setEnabled(aktif);
        txtJumlah.setEnabled(aktif);
        btnSimpanBahan.setEnabled(aktif);
        btnHapusBahan.setEnabled(aktif);
    }

    private void kosongkanMenu() {
        tabelMenu.clearSelection();
        txtNama.setText("");
        txtHarga.setText("");
        txtNama.requestFocusInWindow();
    }

    private Menu dariForm(int id) {
        return new Menu(id, txtNama.getText(), Ui.angka(txtHarga.getText(), "Harga"));
    }

    private void tambahMenu() throws Exception {
        Menu m = dariForm(0);
        menuDao.tambah(m);
        kosongkanMenu();
        muat();
        Ui.info(this, "Menu \"" + m.getNama() + "\" berhasil ditambahkan. Sekarang atur resepnya di sebelah kanan.");
    }

    private void ubahMenu() throws Exception {
        Menu m = dariForm(menuDipilihObj().getId());
        menuDao.ubah(m);
        muat();
        Ui.info(this, "Menu \"" + m.getNama() + "\" berhasil diubah.");
    }

    private void hapusMenu() throws Exception {
        Menu m = menuDipilihObj();
        if (!Ui.tanya(this, "Hapus menu \"" + m.getNama() + "\" beserta resepnya?")) return;
        menuDao.hapus(m.getId());
        kosongkanMenu();
        muat();
    }

    private void simpanBahan() throws Exception {
        Bahan b = (Bahan) pilihBahan.getSelectedItem();
        if (b == null) throw new IllegalStateException("Belum ada bahan. Tambahkan dulu di halaman Stok Bahan.");
        menuDao.simpanResep(menuDipilihObj().getId(), b.getId(), Ui.angka(txtJumlah.getText(), "Jumlah"));
        txtJumlah.setText("");
        muatResep();
    }

    private void hapusBahan() throws Exception {
        int baris = tabelResep.getSelectedRow();
        if (baris < 0) throw new IllegalStateException("Pilih bahan di tabel resep terlebih dahulu.");
        menuDao.hapusResep(resep.get(baris).getId());
        muatResep();
    }
}
