package id.ramenku.data;

import id.ramenku.Angka;
import id.ramenku.Koneksi;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Penjualan lewat JDBC: simpan nota, kurangi stok bahan sesuai resep, dan baca riwayat. */
public class PenjualanDao {

    /**
     * Menyimpan pesanan sebagai satu nota dalam satu transaksi: nota, isi nota, dan pengurangan stok
     * berhasil semuanya atau dibatalkan semuanya. Bila stok bahan tidak cukup, nota ditolak.
     *
     * @return nomor nota yang baru
     */
    public int simpan(List<ItemPesanan> pesanan) throws SQLException {
        if (pesanan == null || pesanan.isEmpty()) throw new IllegalArgumentException("Pesanan masih kosong.");
        try (Connection c = Koneksi.buka()) {
            c.setAutoCommit(false);
            try {
                Map<Integer, BigDecimal> perlu = kebutuhanBahan(c, pesanan);
                periksaStok(c, perlu);

                BigDecimal total = BigDecimal.ZERO;
                for (ItemPesanan i : pesanan) total = total.add(i.getSubtotal());

                int notaId;
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO penjualan (total) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setBigDecimal(1, total);
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) {
                        k.next();
                        notaId = k.getInt(1);
                    }
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO penjualan_item (penjualan_id, menu_id, jumlah, harga_satuan) VALUES (?, ?, ?, ?)")) {
                    for (ItemPesanan i : pesanan) {
                        ps.setInt(1, notaId);
                        ps.setInt(2, i.getMenu().getId());
                        ps.setInt(3, i.getJumlah());
                        ps.setBigDecimal(4, i.getMenu().getHarga());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE bahan SET stok = stok - ? WHERE id = ?")) {
                    for (Map.Entry<Integer, BigDecimal> e : perlu.entrySet()) {
                        ps.setBigDecimal(1, e.getValue());
                        ps.setInt(2, e.getKey());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                c.commit();
                return notaId;
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        }
    }

    /** Total bahan yang dipakai pesanan ini: jumlah di resep dikali jumlah porsi. */
    private Map<Integer, BigDecimal> kebutuhanBahan(Connection c, List<ItemPesanan> pesanan) throws SQLException {
        Map<Integer, BigDecimal> perlu = new LinkedHashMap<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT bahan_id, jumlah FROM resep WHERE menu_id = ?")) {
            for (ItemPesanan i : pesanan) {
                ps.setInt(1, i.getMenu().getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        BigDecimal butuh = rs.getBigDecimal("jumlah").multiply(BigDecimal.valueOf(i.getJumlah()));
                        perlu.merge(rs.getInt("bahan_id"), butuh, BigDecimal::add);
                    }
                }
            }
        }
        return perlu;
    }

    /** Mengunci baris bahan lalu memastikan stoknya cukup; bila tidak, melempar pesan yang merinci. */
    private void periksaStok(Connection c, Map<Integer, BigDecimal> perlu) throws SQLException {
        List<String> kurang = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT nama, satuan, stok FROM bahan WHERE id = ? FOR UPDATE")) {
            for (Map.Entry<Integer, BigDecimal> e : perlu.entrySet()) {
                ps.setInt(1, e.getKey());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getBigDecimal("stok").compareTo(e.getValue()) < 0) {
                        kurang.add("- " + rs.getString("nama") + ": butuh " + Angka.tulis(e.getValue()) + " "
                                + rs.getString("satuan") + ", tersisa " + Angka.tulis(rs.getBigDecimal("stok")));
                    }
                }
            }
        }
        if (!kurang.isEmpty()) {
            throw new IllegalStateException("Stok bahan tidak cukup, nota ditolak:\n" + String.join("\n", kurang));
        }
    }

    public List<Nota> riwayat() throws SQLException {
        List<Nota> hasil = new ArrayList<>();
        try (Connection c = Koneksi.buka(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, waktu, total FROM penjualan ORDER BY id DESC LIMIT 200")) {
            while (rs.next()) {
                Timestamp t = rs.getTimestamp("waktu");
                hasil.add(new Nota(rs.getInt("id"), t.toLocalDateTime(), rs.getBigDecimal("total")));
            }
        }
        return hasil;
    }

    public Nota nota(int id) throws SQLException {
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement("SELECT id, waktu, total FROM penjualan WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalStateException("Nota nomor " + id + " tidak ditemukan.");
                return new Nota(rs.getInt("id"), rs.getTimestamp("waktu").toLocalDateTime(), rs.getBigDecimal("total"));
            }
        }
    }

    public List<BarisNota> isi(int notaId) throws SQLException {
        String sql = "SELECT m.nama, i.jumlah, i.harga_satuan FROM penjualan_item i "
                + "JOIN menu m ON m.id = i.menu_id WHERE i.penjualan_id = ? ORDER BY i.id";
        List<BarisNota> hasil = new ArrayList<>();
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, notaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) hasil.add(new BarisNota(rs.getString("nama"), rs.getInt("jumlah"), rs.getBigDecimal("harga_satuan")));
            }
        }
        return hasil;
    }
}
