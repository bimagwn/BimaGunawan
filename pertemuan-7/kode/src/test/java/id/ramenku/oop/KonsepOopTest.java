package id.ramenku.oop;

import static org.junit.jupiter.api.Assertions.*;

import id.ramenku.oop.model.MenuMakanan;
import id.ramenku.oop.model.MenuMinuman;
import id.ramenku.oop.model.Pesanan;
import id.ramenku.oop.model.Produk;
import org.junit.jupiter.api.Test;

class KonsepOopTest {

    @Test
    void encapsulationSetterMenjagaNilai() {
        Produk p = new Produk("Kerupuk", 3000);
        assertEquals(3000, p.getHarga());
        p.setHarga(3500);
        assertEquals(3500, p.getHarga());
        assertThrows(IllegalArgumentException.class, () -> p.setHarga(0));
        assertThrows(IllegalArgumentException.class, () -> p.setHarga(-10));
        assertThrows(IllegalArgumentException.class, () -> p.setNama("  "));
        assertThrows(IllegalArgumentException.class, () -> new Produk(null, 1000));
        assertEquals(3500, p.getHarga());              // nilai tidak berubah setelah penolakan
        assertEquals("Kerupuk", p.getNama());
    }

    @Test
    void encapsulationAttributeTambahanSubclass() {
        MenuMakanan m = new MenuMakanan("Ramen", 25000, 2);
        assertThrows(IllegalArgumentException.class, () -> m.setTingkatPedas(6));
        assertThrows(IllegalArgumentException.class, () -> m.setTingkatPedas(-1));
        assertEquals(2, m.getTingkatPedas());
    }

    @Test
    void inheritanceSubclassMewarisiProduk() {
        MenuMakanan m = new MenuMakanan("Ramen Pedas", 28000, 3);
        MenuMinuman n = new MenuMinuman("Es Teh", 5000, true);
        assertTrue(m instanceof Produk);
        assertTrue(n instanceof Produk);
        assertEquals("Makanan", m.getKategori());      // kategori (protected) diwarisi dan diisi subclass
        assertEquals("Minuman", n.getKategori());
        assertEquals("Ramen Pedas", m.getNama());      // method getNama() diwarisi dari Produk
    }

    @Test
    void polymorphismOverridingMemakaiVersiSubclass() {
        Produk makanan = new MenuMakanan("Ramen Pedas", 25000, 3);
        Produk dingin = new MenuMinuman("Es Teh", 5000, true);
        Produk hangat = new MenuMinuman("Teh Hangat", 4000, false);
        Produk biasa = new Produk("Kerupuk", 3000);
        assertEquals(2 * 25000 + 500 * 3 * 2, makanan.hitungHarga(2));   // 53.000
        assertEquals(2 * 5000 + 1000 * 2, dingin.hitungHarga(2));         // 12.000
        assertEquals(2 * 4000, hangat.hitungHarga(2));                    // 8.000
        assertEquals(2 * 3000, biasa.hitungHarga(2));                     // 6.000
        assertTrue(makanan.deskripsi().contains("pedas level 3"));
        assertTrue(dingin.deskripsi().contains("dingin"));
        assertTrue(hangat.deskripsi().contains("hangat"));
    }

    @Test
    void polymorphismOverloadingTigaVersiHitungHarga() {
        Produk p = new MenuMakanan("Ramen Pedas", 25000, 3);                // hitungHarga(2) = 53.000
        assertEquals(53000, p.hitungHarga(2), 0.001);
        assertEquals(47700, p.hitungHarga(2, 10.0), 0.001);                 // diskon 10 persen
        assertEquals(47700, p.hitungHarga(2, "HEMAT10"), 0.001);            // kode promo
        assertEquals(42400, p.hitungHarga(2, "ramen20"), 0.001);            // tidak peka huruf besar-kecil
        assertEquals(53000, p.hitungHarga(2, "KODESALAH"), 0.001);          // kode tidak dikenal: tanpa diskon
        assertThrows(IllegalArgumentException.class, () -> p.hitungHarga(2, 101.0));
        assertThrows(IllegalArgumentException.class, () -> p.hitungHarga(2, -1.0));
        assertThrows(IllegalArgumentException.class, () -> p.hitungHarga(0));
    }

    @Test
    void pesananMemakaiVersiMasingMasingObjek() {
        Pesanan pesanan = new Pesanan();
        pesanan.tambah(new MenuMakanan("Ramen Pedas", 25000, 3), 2);   // 53.000
        pesanan.tambah(new MenuMinuman("Es Teh", 5000, true), 2);      // 12.000
        pesanan.tambah(new Produk("Kerupuk", 3000), 1);                // 3.000
        assertEquals(68000, pesanan.total(), 0.001);
        assertEquals(3, pesanan.banyakBaris());
        assertEquals(3, pesanan.rincian().size());
        assertTrue(pesanan.rincian().get(0).contains("Ramen Pedas"));
        assertThrows(IllegalArgumentException.class, () -> pesanan.tambah(null, 1));
        assertThrows(IllegalArgumentException.class, () -> pesanan.tambah(new Produk("X", 1), 0));
        pesanan.kosongkan();
        assertEquals(0, pesanan.total());
    }

    @Test
    void rupiahDanKodeProduk() {
        assertEquals("Rp 25.000", Produk.rupiah(25000));
        assertEquals("Rp 0", Produk.rupiah(0));
        Produk a = new Produk("A", 1000);
        Produk b = new Produk("B", 1000);
        Pesanan ps = new Pesanan();
        ps.tambah(a, 1);
        ps.tambah(b, 1);
        String kodeA = ps.rincian().get(0).replaceAll(".*\\[(PRD-\\d+)\\].*", "$1");
        String kodeB = ps.rincian().get(1).replaceAll(".*\\[(PRD-\\d+)\\].*", "$1");
        assertNotEquals(kodeA, kodeB);                                 // kode otomatis selalu berbeda
        assertTrue(kodeA.matches("PRD-\\d{3}"));
    }
}
