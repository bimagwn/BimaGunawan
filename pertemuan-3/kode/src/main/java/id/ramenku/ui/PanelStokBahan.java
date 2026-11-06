package id.ramenku.ui;

import id.ramenku.Angka;
import id.ramenku.data.Bahan;
import id.ramenku.data.BahanDao;
import id.ramenku.laporan.Laporan;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Fitur Stok Bahan: tabel persediaan, tambah/ubah/hapus lewat JDBC, dan laporan PDF. */
public class PanelStokBahan extends JPanel implements Halaman {

    private final BahanDao dao = new BahanDao();
    private final DefaultTableModel model = Ui.model("Nama Bahan", "Satuan", "Stok", "Stok Minimal", "Status");
    private final JTable tabel = Ui.tabel(model);
    private List<Bahan> data = new ArrayList<>();

    private final JTextField txtNama = Ui.isian();
    private final JTextField txtSatuan = Ui.isian();
    private final JTextField txtStok = Ui.isian();
    private final JTextField txtMinimal = Ui.isian();
    private final JLabel ringkas = Ui.anak(" ");
    private final JButton btnLaporan = Ui.utama("Cetak Laporan PDF");

    public PanelStokBahan() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Ui.KERTAS);
        setBorder(BorderFactory.createEmptyBorder(8, 24, 24, 24));

        JPanel atas = new JPanel(new BorderLayout());
        atas.setOpaque(false);
        JPanel judul = new JPanel(new BorderLayout());
        judul.setOpaque(false);
        judul.add(Ui.judul("Stok Bahan"), BorderLayout.NORTH);
        judul.add(ringkas, BorderLayout.SOUTH);
        atas.add(judul, BorderLayout.WEST);
        atas.add(btnLaporan, BorderLayout.EAST);
        add(atas, BorderLayout.NORTH);

        tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object nilai, boolean pilih, boolean fokus, int baris, int kolom) {
                super.getTableCellRendererComponent(t, nilai, pilih, false, baris, kolom);
                int i = t.convertRowIndexToModel(baris);
                boolean menipis = i < data.size() && data.get(i).isMenipis();
                setHorizontalAlignment(kolom == 2 || kolom == 3 ? SwingConstants.RIGHT : kolom == 4 ? SwingConstants.CENTER : SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setForeground(Ui.TINTA);
                setFont(t.getFont());
                if (kolom == 4 && menipis) {
                    setForeground(Ui.VERMILION);
                    setFont(t.getFont().deriveFont(Font.BOLD));
                }
                return this;
            }
        });
        add(Ui.gulir(tabel), BorderLayout.CENTER);
        add(panelForm(), BorderLayout.EAST);

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariBaris();
        });
        btnLaporan.addActionListener(e -> cetakLaporan());
    }

    private JPanel panelForm() {
        JPanel kartu = Ui.kartu(new BorderLayout(0, 8));
        kartu.setPreferredSize(new Dimension(290, 0));
        JLabel judul = new JLabel("Data Bahan");
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 15f));
        judul.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        kartu.add(judul, BorderLayout.NORTH);

        JPanel f = Ui.form();
        Ui.baris(f, "NAMA BAHAN", txtNama);
        Ui.baris(f, "SATUAN (gram, ml, butir, ...)", txtSatuan);
        Ui.baris(f, "STOK", txtStok);
        Ui.baris(f, "STOK MINIMAL (batas menipis)", txtMinimal);
        kartu.add(f, BorderLayout.CENTER);

        JButton tambah = Ui.utama("Tambah");
        JButton ubah = Ui.biasa("Ubah");
        JButton hapus = Ui.biasa("Hapus");
        JButton kosong = Ui.biasa("Kosongkan");
        tambah.addActionListener(e -> Ui.coba(this, this::tambah));
        ubah.addActionListener(e -> Ui.coba(this, this::ubah));
        hapus.addActionListener(e -> Ui.coba(this, this::hapus));
        kosong.addActionListener(e -> kosongkan());
        JPanel tombol = new JPanel(new GridLayout(2, 2, 8, 8));
        tombol.setOpaque(false);
        tombol.add(tambah);
        tombol.add(ubah);
        tombol.add(hapus);
        tombol.add(kosong);
        kartu.add(tombol, BorderLayout.SOUTH);
        return kartu;
    }

    @Override public void muat() {
        Ui.coba(this, () -> {
            data = dao.semua();
            model.setRowCount(0);
            int menipis = 0;
            for (Bahan b : data) {
                if (b.isMenipis()) menipis++;
                model.addRow(new Object[] {b.getNama(), b.getSatuan(), Angka.tulis(b.getStok()), Angka.tulis(b.getStokMinimal()),
                        b.isMenipis() ? "MENIPIS" : "AMAN"});
            }
            ringkas.setText(data.size() + " bahan, " + menipis + " menipis");
        });
    }

    private Bahan dipilih() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) throw new IllegalStateException("Pilih bahan di tabel terlebih dahulu.");
        return data.get(tabel.convertRowIndexToModel(baris));
    }

    private Bahan dariForm(int id) {
        return new Bahan(id, txtNama.getText(), txtSatuan.getText(),
                Ui.angka(txtStok.getText(), "Stok"), Ui.angka(txtMinimal.getText(), "Stok minimal"));
    }

    private void isiFormDariBaris() {
        int baris = tabel.getSelectedRow();
        if (baris < 0 || baris >= data.size()) return;
        Bahan b = data.get(tabel.convertRowIndexToModel(baris));
        txtNama.setText(b.getNama());
        txtSatuan.setText(b.getSatuan());
        txtStok.setText(Angka.tulis(b.getStok()));
        txtMinimal.setText(Angka.tulis(b.getStokMinimal()));
    }

    private void kosongkan() {
        tabel.clearSelection();
        txtNama.setText("");
        txtSatuan.setText("");
        txtStok.setText("");
        txtMinimal.setText("");
        txtNama.requestFocusInWindow();
    }

    private void tambah() throws Exception {
        Bahan b = dariForm(0);
        dao.tambah(b);
        muat();
        kosongkan();
        Ui.info(this, "Bahan \"" + b.getNama() + "\" berhasil ditambahkan.");
    }

    private void ubah() throws Exception {
        Bahan lama = dipilih();
        Bahan baru = dariForm(lama.getId());
        dao.ubah(baru);
        muat();
        Ui.info(this, "Bahan \"" + baru.getNama() + "\" berhasil diubah.");
    }

    private void hapus() throws Exception {
        Bahan b = dipilih();
        if (!Ui.tanya(this, "Hapus bahan \"" + b.getNama() + "\"?")) return;
        dao.hapus(b.getId());
        muat();
        kosongkan();
    }

    private void cetakLaporan() {
        btnLaporan.setEnabled(false);
        new SwingWorker<Path, Void>() {
            @Override protected Path doInBackground() throws Exception {
                return Laporan.stokBahan();
            }

            @Override protected void done() {
                btnLaporan.setEnabled(true);
                Ui.coba(PanelStokBahan.this, () -> {
                    Path pdf;
                    try {
                        pdf = get();
                    } catch (java.util.concurrent.ExecutionException e) {
                        throw e.getCause() instanceof Exception ? (Exception) e.getCause() : e;
                    }
                    Laporan.buka(pdf);
                });
            }
        }.execute();
    }
}
