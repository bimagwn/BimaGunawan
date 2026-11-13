package id.ramenku.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Pintu masuk ke Spring: membuat konteks aplikasi (IoC container) satu kali, lalu memberikan
 * bean yang diminta, misalnya Konteks.bean(BahanDao.class).
 */
public final class Konteks {

    private static AnnotationConfigApplicationContext konteks;

    // disimpan di atribut agar pengaturan level log tidak hilang
    private static final Logger[] LOG = {
        Logger.getLogger("org.hibernate"),
        Logger.getLogger("org.hibernate.orm.deprecation"),
        Logger.getLogger("org.hibernate.engine.jdbc.spi.SqlExceptionHelper"),
        Logger.getLogger("org.springframework"),
        Logger.getLogger("com.zaxxer.hikari")
    };

    static {
        LOG[0].setLevel(Level.WARNING);
        LOG[1].setLevel(Level.SEVERE);
        LOG[2].setLevel(Level.OFF);         // galat SQL sudah diterjemahkan menjadi pesan yang mudah dipahami
        LOG[3].setLevel(Level.WARNING);
        LOG[4].setLevel(Level.WARNING);
    }

    private Konteks() {}

    /** Pekerjaan yang membutuhkan koneksi JDBC mentah (dipakai JasperReports). */
    @FunctionalInterface
    public interface KerjaKoneksi<T> {
        T jalan(Connection koneksi) throws Exception;
    }

    public static synchronized AnnotationConfigApplicationContext ambil() {
        if (konteks == null) konteks = new AnnotationConfigApplicationContext(Konfigurasi.class);
        return konteks;
    }

    public static <T> T bean(Class<T> jenis) {
        return ambil().getBean(jenis);
    }

    /** Menutup Spring beserta kolam koneksinya; dipakai tes setelah database uji dibuat ulang. */
    public static synchronized void tutup() {
        if (konteks != null) {
            konteks.close();
            konteks = null;
        }
    }

    /** Menjalankan pekerjaan dengan koneksi JDBC yang dipinjam dari kolam koneksi Spring. */
    public static <T> T denganKoneksi(KerjaKoneksi<T> kerja) throws Exception {
        try (Connection c = bean(DataSource.class).getConnection()) {
            return kerja.jalan(c);
        }
    }

    /** Memeriksa koneksi ke database; melempar galat bila MySQL belum menyala. */
    public static void cekKoneksi() {
        try {
            denganKoneksi(c -> {
                try (Statement s = c.createStatement()) {
                    return s.execute("select 1");
                }
            });
        } catch (RuntimeException e) {
            throw e;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
