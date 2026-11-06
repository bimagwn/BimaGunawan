package id.ramenku.ui;

import id.ramenku.Angka;
import id.ramenku.data.BarisNota;
import id.ramenku.data.ItemPesanan;
import id.ramenku.data.Menu;
import id.ramenku.data.MenuDao;
import id.ramenku.data.Nota;
import id.ramenku.data.PenjualanDao;
import id.ramenku.laporan.Laporan;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/** Fitur Penjualan: kasir (pesanan, simpan nota, struk PDF) dan riwayat nota. */
public class PanelPenjualan extends JPanel implements Halaman {

    private static final DateTimeFormatter WAKTU = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", new Locale("id", "ID"));

    private final MenuDao menuDao = new MenuDao();
    private final PenjualanDao penjualanDao = new PenjualanDao();

    private final DefaultTableModel modelMenu = Ui.model("Menu", "Harga");
    private final JTable tabelMenu = Ui.tabel(modelMenu);
    private List<Menu> menus = new ArrayList<>();
    private final JSpinner jumlah = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

    private final DefaultTableModel modelPesanan = Ui.model("Menu", "Jumlah", "Harga", "Subtotal");
    private final JTable tabelPesanan = Ui.tabel(modelPesanan);
    private final List<ItemPesanan> pesanan = new ArrayList<>();
    private final JLabel total = new JLabel("Rp 0");
    private final JButton btnSimpan = Ui.utama("Simpan dan Cetak Struk");

    private final DefaultTableModel modelRiwayat = Ui.model("No. Nota", "Waktu", "Total");
    private final JTable tabelRiwayat = Ui.tabel(modelRiwayat);
    private List<Nota> riwayat = new ArrayList<>();
    private final DefaultTableModel modelIsi = Ui.model("Menu", "Jumlah", "Harga", "Subtotal");
    private final JTable tabelIsi = Ui.tabel(modelIsi);

    public PanelPenjualan() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Ui.KERTAS);
        setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));

        JPanel judul = new JPanel(new BorderLayout());
        judul.setOpaque(false);
        judul.add(Ui.judul("Penjualan"), BorderLayout.NORTH);
        judul.add(Ui.anak("Catat pesanan, simpan nota, dan cetak struk. Stok bahan berkurang otomatis sesuai resep."), BorderLayout.SOUTH);
        add(judul, BorderLayout.NORTH);

        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Kasir", panelKasir());
        tab.addTab("Riwayat Nota", panelRiwayat());
        add(tab, BorderLayout.CENTER);
        tab.addChangeListener(e -> {
            if (tab.getSelectedIndex() == 1) muatRiwayat();
        });
    }

    private JPanel panelKasir() {
        JPanel isi = new JPanel(new GridLayout(1, 2, 16, 0));
        isi.setOpaque(false);
        isi.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JPanel kiri = Ui.kartu(new BorderLayout(0, 12));
        JLabel j1 = new JLabel("Pilih Menu");
        j1.setFont(j1.getFont().deriveFont(Font.BOLD, 15f));
        kiri.add(j1, BorderLayout.NORTH);
        kiri.add(Ui.gulir(tabelMenu), BorderLayout.CENTER);
        JPanel aksiKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        aksiKiri.setOpaque(false);
        aksiKiri.add(Ui.kecil("JUMLAH"));
        jumlah.setPreferredSize(new java.awt.Dimension(70, 36));
        aksiKiri.add(jumlah);
        JButton tambah = Ui.utama("Tambah ke Pesanan");
        tambah.addActionListener(e -> Ui.coba(this, this::tambahKePesanan));
        aksiKiri.add(tambah);
        kiri.add(aksiKiri, BorderLayout.SOUTH);
        tabelMenu.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) Ui.coba(PanelPenjualan.this, PanelPenjualan.this::tambahKePesanan);
            }
        });

        JPanel kanan = Ui.kartu(new BorderLayout(0, 12));
        JLabel j2 = new JLabel("Pesanan");
        j2.setFont(j2.getFont().deriveFont(Font.BOLD, 15f));
        kanan.add(j2, BorderLayout.NORTH);
        kanan.add(Ui.gulir(tabelPesanan), BorderLayout.CENTER);
        JPanel bawah = new JPanel(new BorderLayout(0, 10));
        bawah.setOpaque(false);
        JPanel barisTotal = new JPanel(new BorderLayout());
        barisTotal.setOpaque(false);
        barisTotal.add(Ui.kecil("TOTAL"), BorderLayout.WEST);
        total.setFont(new Font("Georgia", Font.BOLD, 26));
        total.setForeground(Ui.VERMILION);
        total.setHorizontalAlignment(SwingConstants.RIGHT);
        barisTotal.add(total, BorderLayout.EAST);
        bawah.add(barisTotal, BorderLayout.NORTH);
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tombol.setOpaque(false);
        JButton hapus = Ui.biasa("Hapus Baris");
        JButton kosong = Ui.biasa("Kosongkan");
        hapus.addActionListener(e -> Ui.coba(this, this::hapusBaris));
        kosong.addActionListener(e -> {
            pesanan.clear();
            segarkanPesanan();
        });
        btnSimpan.addActionListener(e -> simpanDanCetak());
        tombol.add(hapus);
        tombol.add(kosong);
        tombol.add(btnSimpan);
        bawah.add(tombol, BorderLayout.SOUTH);
        kanan.add(bawah, BorderLayout.SOUTH);

        isi.add(kiri);
        isi.add(kanan);
        return isi;
    }

    private JPanel panelRiwayat() {
        JPanel kartu = Ui.kartu(new BorderLayout(0, 12));
        JPanel atas = new JPanel(new BorderLayout(0, 8));
        atas.setOpaque(false);
        atas.add(Ui.gulir(tabelRiwayat), BorderLayout.CENTER);
        JPanel bawah = new JPanel(new BorderLayout(0, 8));
        bawah.setOpaque(false);
        JLabel j = new JLabel("Isi nota yang dipilih");
        j.setFont(j.getFont().deriveFont(Font.BOLD, 13f));
        bawah.add(j, BorderLayout.NORTH);
        bawah.add(Ui.gulir(tabelIsi), BorderLayout.CENTER);
        JSplitPane belah = new JSplitPane(JSplitPane.VERTICAL_SPLIT, atas, bawah);
        belah.setResizeWeight(0.5);
        belah.setBorder(null);
        belah.setOpaque(false);
        kartu.add(belah, BorderLayout.CENTER);

        JButton ulang = Ui.utama("Cetak Ulang Struk");
        ulang.addActionListener(e -> Ui.coba(this, this::cetakUlang));
        JPanel aksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        aksi.setOpaque(false);
        aksi.add(ulang);
        kartu.add(aksi, BorderLayout.SOUTH);

        tabelRiwayat.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) Ui.coba(this, this::muatIsiNota);
        });
        return kartu;
    }

    @Override public void muat() {
        Ui.coba(this, () -> {
            menus = menuDao.semua();
            modelMenu.setRowCount(0);
            for (Menu m : menus) modelMenu.addRow(new Object[] {m.getNama(), Angka.rupiah(m.getHarga())});
            // harga bisa berubah di halaman Menu dan Resep, jadi pesanan lama disegarkan ke harga terbaru
            List<ItemPesanan> baru = new ArrayList<>();
            for (ItemPesanan i : pesanan) {
                for (Menu m : menus) {
                    if (m.getId() == i.getMenu().getId()) baru.add(new ItemPesanan(m, i.getJumlah()));
                }
            }
            pesanan.clear();
            pesanan.addAll(baru);
            segarkanPesanan();
        });
    }

    private void tambahKePesanan() {
        int baris = tabelMenu.getSelectedRow();
        if (baris < 0) throw new IllegalStateException("Pilih menu di tabel terlebih dahulu.");
        Menu m = menus.get(baris);
        int qty = (Integer) jumlah.getValue();
        for (ItemPesanan i : pesanan) {
            if (i.getMenu().getId() == m.getId()) {
                i.setJumlah(i.getJumlah() + qty);
                segarkanPesanan();
                return;
            }
        }
        pesanan.add(new ItemPesanan(m, qty));
        segarkanPesanan();
    }

    private void hapusBaris() {
        int baris = tabelPesanan.getSelectedRow();
        if (baris < 0) throw new IllegalStateException("Pilih baris pesanan yang akan dihapus.");
        pesanan.remove(baris);
        segarkanPesanan();
    }

    private void segarkanPesanan() {
        modelPesanan.setRowCount(0);
        BigDecimal jumlahTotal = BigDecimal.ZERO;
        for (ItemPesanan i : pesanan) {
            modelPesanan.addRow(new Object[] {i.getMenu().getNama(), i.getJumlah(), Angka.rupiah(i.getMenu().getHarga()), Angka.rupiah(i.getSubtotal())});
            jumlahTotal = jumlahTotal.add(i.getSubtotal());
        }
        total.setText(Angka.rupiah(jumlahTotal));
    }

    /** Menyimpan nota (satu transaksi), lalu membuat dan membuka struk PDF-nya. */
    private void simpanDanCetak() {
        if (pesanan.isEmpty()) {
            Ui.galat(this, new IllegalStateException("Pesanan masih kosong. Tambahkan menu dulu."));
            return;
        }
        btnSimpan.setEnabled(false);
        List<ItemPesanan> salinan = new ArrayList<>(pesanan);
        new SwingWorker<Integer, Void>() {
            private Path struk;
            private Exception galatStruk;

            @Override protected Integer doInBackground() throws Exception {
                int id = penjualanDao.simpan(salinan);
                try {
                    struk = Laporan.struk(id);
                } catch (Exception e) {
                    galatStruk = e;
                }
                return id;
            }

            @Override protected void done() {
                btnSimpan.setEnabled(true);
                try {
                    int id = get();
                    pesanan.clear();
                    segarkanPesanan();
                    if (struk != null) {
                        try {
                            Laporan.buka(struk);
                        } catch (java.io.IOException e) {
                            galatStruk = e;
                        }
                    }
                    if (galatStruk != null) {
                        Ui.info(PanelPenjualan.this, "Nota No. " + id + " tersimpan, tetapi struk PDF gagal dibuka:\n" + galatStruk.getMessage());
                    } else {
                        Ui.info(PanelPenjualan.this, "Nota No. " + id + " tersimpan. Struk PDF dibuka di aplikasi PDF-mu.");
                    }
                } catch (java.util.concurrent.ExecutionException e) {
                    Ui.galat(PanelPenjualan.this, e.getCause());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }

    private void muatRiwayat() {
        Ui.coba(this, () -> {
            riwayat = penjualanDao.riwayat();
            modelRiwayat.setRowCount(0);
            for (Nota n : riwayat) modelRiwayat.addRow(new Object[] {n.getId(), n.getWaktu().format(WAKTU), Angka.rupiah(n.getTotal())});
            modelIsi.setRowCount(0);
        });
    }

    private void muatIsiNota() throws Exception {
        int baris = tabelRiwayat.getSelectedRow();
        modelIsi.setRowCount(0);
        if (baris < 0 || baris >= riwayat.size()) return;
        for (BarisNota b : penjualanDao.isi(riwayat.get(baris).getId())) {
            modelIsi.addRow(new Object[] {b.getNamaMenu(), b.getJumlah(), Angka.rupiah(b.getHargaSatuan()), Angka.rupiah(b.getSubtotal())});
        }
    }

    private void cetakUlang() throws Exception {
        int baris = tabelRiwayat.getSelectedRow();
        if (baris < 0) throw new IllegalStateException("Pilih nota di tabel riwayat terlebih dahulu.");
        Laporan.buka(Laporan.struk(riwayat.get(baris).getId()));
    }
}
