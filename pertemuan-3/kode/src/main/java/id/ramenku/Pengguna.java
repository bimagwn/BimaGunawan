package id.ramenku;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Pengguna yang masuk; pemeriksaan login dilakukan lewat JDBC ke tabel pengguna. */
public final class Pengguna {

    private final String username;
    private final String nama;

    private Pengguna(String username, String nama) {
        this.username = username;
        this.nama = nama;
    }

    public String getUsername() { return username; }
    public String getNama() { return nama; }

    /** Mengembalikan pengguna bila username dan sandi cocok, atau null bila salah. */
    public static Pengguna masuk(String username, char[] sandi) throws SQLException {
        String sql = "SELECT username, nama FROM pengguna WHERE username = ? AND sandi = ?";
        try (Connection c = Koneksi.buka(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, sha256(sandi));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new Pengguna(rs.getString("username"), rs.getString("nama")) : null;
            }
        }
    }

    /** SHA-256 heksadesimal huruf kecil, sama dengan SHA2(..., 256) di MySQL. */
    static String sha256(char[] sandi) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(new String(sandi).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
