package id.ramenku.data;

import id.ramenku.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Akses tabel bahan lewat JDBC (fitur Stok Bahan). */
public class BahanDao {

    public List<Bahan> semua() throws SQLException {
        String sql = "SELECT id, nama, satuan, stok, stok_minimal FROM bahan ORDER BY nama";
        List<Bahan> hasil = new ArrayList<>();
        try (Connection c = Koneksi.buka(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                hasil.add(new Bahan(rs.getInt("id"), rs.getString("nama"), rs.getString("satuan"),
                        rs.getBigDecimal("stok"), rs.getBigDecimal("stok_minimal")));
            }
        }
        return hasil;
    }

    /** Menambah bahan baru dan mengembalikan id-nya. */
    public int tambah(Bahan b) throws SQLException {
        String sql = "INSERT INTO bahan (nama, satuan, stok, stok_minimal) VALUES (?, ?, ?, ?)";
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getNama());
            ps.setString(2, b.getSatuan());
            ps.setBigDecimal(3, b.getStok());
            ps.setBigDecimal(4, b.getStokMinimal());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) {
                return k.next() ? k.getInt(1) : 0;
            }
        } catch (SQLException e) {
            Galat.periksa(e, "Bahan");
            throw e;
        }
    }

    public void ubah(Bahan b) throws SQLException {
        String sql = "UPDATE bahan SET nama = ?, satuan = ?, stok = ?, stok_minimal = ? WHERE id = ?";
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getNama());
            ps.setString(2, b.getSatuan());
            ps.setBigDecimal(3, b.getStok());
            ps.setBigDecimal(4, b.getStokMinimal());
            ps.setInt(5, b.getId());
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Bahan tidak ditemukan, mungkin sudah dihapus.");
        } catch (SQLException e) {
            Galat.periksa(e, "Bahan");
            throw e;
        }
    }

    public void hapus(int id) throws SQLException {
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement("DELETE FROM bahan WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            Galat.periksa(e, "Bahan");
            throw e;
        }
    }
}
