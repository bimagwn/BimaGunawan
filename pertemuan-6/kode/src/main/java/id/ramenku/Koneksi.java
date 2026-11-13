package id.ramenku;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Pengaturan koneksi ke MySQL yang dipakai Hibernate.
 * Pengaturan dibaca dari koneksi.properties (tidak ikut ke GitHub); bila berkas itu
 * tidak ada, dipakai pengaturan bawaan XAMPP: localhost:3306, user root, tanpa sandi.
 */
public final class Koneksi {

    private static final String BERKAS = "koneksi.properties";

    private Koneksi() {}

    /** Mengembalikan pengaturan url, user, dan password. */
    public static Properties pengaturan() {
        Properties p = new Properties();
        p.setProperty("url", "jdbc:mysql://localhost:3306/ramenku_db?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
        p.setProperty("user", "root");
        p.setProperty("password", "");
        try (FileInputStream in = new FileInputStream(BERKAS)) {
            p.load(in);
        } catch (IOException e) {
            // berkas tidak ada: pakai pengaturan bawaan
        }
        // -Dramenku.url=... dipakai oleh tes supaya tidak menyentuh data asli
        p.setProperty("url", System.getProperty("ramenku.url", p.getProperty("url")));
        return p;
    }
}
