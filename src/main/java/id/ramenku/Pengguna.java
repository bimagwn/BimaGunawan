package id.ramenku;

import id.ramenku.data.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Pengguna yang masuk. Entitas Hibernate untuk tabel pengguna; login diperiksa lewat HQL. */
@Entity
@Table(name = "pengguna")
public class Pengguna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Column(nullable = false, length = 64)
    private String sandi;

    @Column(nullable = false, length = 60)
    private String nama;

    protected Pengguna() {}

    public String getUsername() { return username; }
    public String getNama() { return nama; }

    /** Mengembalikan pengguna bila username dan sandi cocok, atau null bila salah. */
    public static Pengguna masuk(String username, char[] sandi) {
        String hash = sha256(sandi);
        return HibernateUtil.baca(s -> s.createQuery(
                        "from Pengguna where username = :u and sandi = :s", Pengguna.class)
                .setParameter("u", username)
                .setParameter("s", hash)
                .uniqueResult());
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
