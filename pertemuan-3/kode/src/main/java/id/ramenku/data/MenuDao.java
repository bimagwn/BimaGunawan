package id.ramenku.data;

import id.ramenku.Koneksi;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Akses tabel menu dan resep lewat JDBC (fitur Menu dan Resep). */
public class MenuDao {

    public List<Menu> semua() throws SQLException {
        List<Menu> hasil = new ArrayList<>();
        try (Connection c = Koneksi.buka(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nama, harga FROM menu ORDER BY nama")) {
            while (rs.next()) hasil.add(new Menu(rs.getInt("id"), rs.getString("nama"), rs.getBigDecimal("harga")));
        }
        return hasil;
    }

    public int tambah(Menu m) throws SQLException {
        String sql = "INSERT INTO menu (nama, harga) VALUES (?, ?)";
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNama());
            ps.setBigDecimal(2, m.getHarga());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) {
                return k.next() ? k.getInt(1) : 0;
            }
        } catch (SQLException e) {
            Galat.periksa(e, "Menu");
            throw e;
        }
    }

    public void ubah(Menu m) throws SQLException {
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement("UPDATE menu SET nama = ?, harga = ? WHERE id = ?")) {
            ps.setString(1, m.getNama());
            ps.setBigDecimal(2, m.getHarga());
            ps.setInt(3, m.getId());
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Menu tidak ditemukan, mungkin sudah dihapus.");
        } catch (SQLException e) {
            Galat.periksa(e, "Menu");
            throw e;
        }
    }

    /** Menghapus menu beserta resepnya. Menu yang sudah pernah terjual tidak bisa dihapus. */
    public void hapus(int id) throws SQLException {
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement("DELETE FROM menu WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                throw new IllegalStateException("Menu ini sudah pernah terjual, jadi tidak dapat dihapus.");
            }
            throw e;
        }
    }

    public List<ResepBaris> resep(int menuId) throws SQLException {
        String sql = "SELECT r.id, r.bahan_id, b.nama, b.satuan, r.jumlah FROM resep r "
                + "JOIN bahan b ON b.id = r.bahan_id WHERE r.menu_id = ? ORDER BY b.nama";
        List<ResepBaris> hasil = new ArrayList<>();
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(new ResepBaris(rs.getInt("id"), rs.getInt("bahan_id"), rs.getString("nama"),
                            rs.getString("satuan"), rs.getBigDecimal("jumlah")));
                }
            }
        }
        return hasil;
    }

    /** Menambah bahan ke resep; bila bahan itu sudah ada di resep, jumlahnya diganti. */
    public void simpanResep(int menuId, int bahanId, BigDecimal jumlah) throws SQLException {
        if (jumlah == null || jumlah.signum() <= 0) throw new IllegalArgumentException("Jumlah bahan harus lebih dari 0.");
        String sql = "INSERT INTO resep (menu_id, bahan_id, jumlah) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE jumlah = VALUES(jumlah)";
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            ps.setInt(2, bahanId);
            ps.setBigDecimal(3, jumlah);
            ps.executeUpdate();
        }
    }

    public void hapusResep(int resepId) throws SQLException {
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement("DELETE FROM resep WHERE id = ?")) {
            ps.setInt(1, resepId);
            ps.executeUpdate();
        }
    }
}
