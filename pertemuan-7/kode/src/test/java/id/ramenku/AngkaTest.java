package id.ramenku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AngkaTest {

    @Test
    void titikRibuanDanKomaDesimalDibacaBenar() {
        assertEquals(new BigDecimal("5000"), Angka.baca("5.000"));
        assertEquals(new BigDecimal("2.5"), Angka.baca("2,5"));
        assertEquals(new BigDecimal("1250.75"), Angka.baca("1.250,75"));
        assertEquals(new BigDecimal("28000"), Angka.baca("Rp 28.000"));
        assertEquals(new BigDecimal("12"), Angka.baca("12"));
    }

    @Test
    void teksBukanAngkaDitolak() {
        assertThrows(NumberFormatException.class, () -> Angka.baca(""));
        assertThrows(NumberFormatException.class, () -> Angka.baca("abc"));
    }

    @Test
    void angkaDitulisGayaIndonesia() {
        assertEquals("5.000", Angka.tulis(new BigDecimal("5000")));
        assertEquals("2,5", Angka.tulis(new BigDecimal("2.50")));
        assertEquals("Rp 28.000", Angka.rupiah(new BigDecimal("28000")));
    }
}
