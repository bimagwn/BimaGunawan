package id.ramenku;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Membaca dan menampilkan angka gaya Indonesia: titik untuk ribuan, koma untuk desimal. */
public final class Angka {

    private static final DecimalFormatSymbols SIMBOL = new DecimalFormatSymbols(new Locale("id", "ID"));

    private Angka() {}

    /** "5.000" menjadi 5000, "2,5" menjadi 2.5, "Rp 28.000" menjadi 28000. */
    public static BigDecimal baca(String teks) {
        String s = teks == null ? "" : teks.trim().replace("Rp", "").replace("rp", "").replace(" ", "");
        if (s.isEmpty()) throw new NumberFormatException("kosong");
        if (s.contains(",")) {
            s = s.replace(".", "").replace(',', '.');
        } else if (s.matches("\\d{1,3}(\\.\\d{3})+")) {
            s = s.replace(".", "");
        }
        return new BigDecimal(s);
    }

    /** 5000 menjadi "5.000", 2.5 menjadi "2,5". */
    public static String tulis(BigDecimal nilai) {
        return new DecimalFormat("#,##0.##", SIMBOL).format(nilai);
    }

    /** 28000 menjadi "Rp 28.000". */
    public static String rupiah(BigDecimal nilai) {
        return "Rp " + tulis(nilai);
    }
}
