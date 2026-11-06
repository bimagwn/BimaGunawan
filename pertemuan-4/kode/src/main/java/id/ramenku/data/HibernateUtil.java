package id.ramenku.data;

import id.ramenku.Koneksi;
import id.ramenku.Pengguna;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Pusat pengaturan Hibernate: membuat SessionFactory (satu kali), menjalankan pekerjaan di dalam
 * Session atau transaksi, dan menerjemahkan galat database menjadi pesan yang mudah dipahami.
 */
public final class HibernateUtil {

    private static final int DUPLIKAT = 1062;
    private static final int MASIH_DIPAKAI = 1451;

    private static SessionFactory pabrik;

    // disimpan di atribut agar pengaturan level log tidak hilang
    private static final Logger[] LOG = {
        Logger.getLogger("org.hibernate"),
        Logger.getLogger("org.hibernate.orm.connections.pooling"),
        Logger.getLogger("org.hibernate.engine.jdbc.spi.SqlExceptionHelper"),
        Logger.getLogger("org.hibernate.orm.deprecation")
    };

    static {
        LOG[0].setLevel(Level.WARNING);
        LOG[1].setLevel(Level.SEVERE);      // peringatan kolam koneksi bawaan tidak perlu ditampilkan
        LOG[2].setLevel(Level.OFF);         // galat SQL sudah diterjemahkan menjadi pesan yang mudah dipahami
        LOG[3].setLevel(Level.SEVERE);      // dialek memang sengaja ditulis karena metadata JDBC tidak dibaca
    }

    private HibernateUtil() {}

    /** Pekerjaan yang dijalankan di dalam sebuah Session. */
    @FunctionalInterface
    public interface Kerja<T> {
        T jalan(Session session);
    }

    /** Pekerjaan yang membutuhkan koneksi JDBC mentah (dipakai JasperReports). */
    @FunctionalInterface
    public interface KerjaKoneksi<T> {
        T jalan(Connection koneksi) throws Exception;
    }

    public static synchronized SessionFactory pabrik() {
        if (pabrik == null) pabrik = bangun();
        return pabrik;
    }

    private static SessionFactory bangun() {
        Properties p = Koneksi.pengaturan();
        Configuration c = new Configuration();
        c.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        c.setProperty("hibernate.connection.url", p.getProperty("url"));
        c.setProperty("hibernate.connection.username", p.getProperty("user"));
        c.setProperty("hibernate.connection.password", p.getProperty("password"));
        c.setProperty("hibernate.connection.pool_size", "5");
        // XAMPP memakai MariaDB; dialek dapat diganti lewat koneksi.properties (kunci dialect)
        c.setProperty("hibernate.dialect", p.getProperty("dialect", "org.hibernate.dialect.MariaDBDialect"));
        c.setProperty("hibernate.boot.allow_jdbc_metadata_access", "false");
        c.setProperty("hibernate.hbm2ddl.auto", "none");                       // tabel dibuat dari database/ramenku_db.sql
        c.setProperty("hibernate.show_sql", System.getProperty("ramenku.sql", "false"));
        c.addAnnotatedClass(Pengguna.class);
        c.addAnnotatedClass(Bahan.class);
        c.addAnnotatedClass(Menu.class);
        c.addAnnotatedClass(ResepBaris.class);
        c.addAnnotatedClass(Nota.class);
        c.addAnnotatedClass(BarisNota.class);
        return c.buildSessionFactory();
    }

    /** Menutup SessionFactory; dipanggil tes setelah database uji dibuat ulang. */
    public static synchronized void tutup() {
        if (pabrik != null) {
            pabrik.close();
            pabrik = null;
        }
    }

    /** Menjalankan pekerjaan baca (tanpa transaksi). */
    public static <T> T baca(Kerja<T> kerja) {
        try (Session s = pabrik().openSession()) {
            return kerja.jalan(s);
        } catch (RuntimeException e) {
            throw terjemah(e, "Data", null);
        }
    }

    public static <T> T transaksi(String objek, Kerja<T> kerja) {
        return transaksi(objek, null, kerja);
    }

    /**
     * Menjalankan pekerjaan dalam satu transaksi: semua perubahan disimpan (commit) bila berhasil
     * dan dibatalkan semua (rollback) bila gagal.
     */
    public static <T> T transaksi(String objek, String pesanTerpakai, Kerja<T> kerja) {
        try (Session s = pabrik().openSession()) {
            Transaction tx = s.beginTransaction();
            try {
                T hasil = kerja.jalan(s);
                tx.commit();
                return hasil;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (RuntimeException e) {
            throw terjemah(e, objek, pesanTerpakai);
        }
    }

    /** Menjalankan pekerjaan dengan koneksi JDBC yang dipinjam dari Hibernate. */
    public static <T> T denganKoneksi(KerjaKoneksi<T> kerja) throws Exception {
        try (Session s = pabrik().openSession()) {
            return s.doReturningWork(c -> {
                try {
                    return kerja.jalan(c);
                } catch (SQLException | RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
        }
    }

    /** Memeriksa koneksi ke database; melempar galat bila MySQL belum menyala. */
    public static void cekKoneksi() {
        baca(s -> s.createNativeQuery("select 1", Integer.class).getSingleResult());
    }

    private static RuntimeException terjemah(RuntimeException e, String objek, String pesanTerpakai) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof ConstraintViolationException cv) {
                if (cv.getErrorCode() == DUPLIKAT) {
                    return new IllegalStateException(objek + " dengan nama itu sudah ada.");
                }
                if (cv.getErrorCode() == MASIH_DIPAKAI) {
                    return new IllegalStateException(pesanTerpakai != null ? pesanTerpakai
                            : objek + " tidak dapat dihapus karena masih dipakai data lain.");
                }
            }
        }
        return e;
    }
}
