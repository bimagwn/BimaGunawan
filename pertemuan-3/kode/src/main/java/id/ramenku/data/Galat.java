package id.ramenku.data;

import java.sql.SQLException;

/** Menerjemahkan galat MySQL yang umum menjadi pesan yang mudah dipahami pengguna. */
final class Galat {

    private static final int DUPLIKAT = 1062;
    private static final int MASIH_DIPAKAI = 1451;

    private Galat() {}

    /** Melempar pesan ramah untuk galat yang dikenal; galat lain dibiarkan oleh pemanggil. */
    static void periksa(SQLException e, String objek) {
        if (e.getErrorCode() == DUPLIKAT) {
            throw new IllegalStateException(objek + " dengan nama itu sudah ada.");
        }
        if (e.getErrorCode() == MASIH_DIPAKAI) {
            throw new IllegalStateException(objek + " tidak dapat dihapus karena masih dipakai data lain.");
        }
    }
}
