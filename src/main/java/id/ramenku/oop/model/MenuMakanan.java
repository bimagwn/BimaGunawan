package id.ramenku.oop.model;

/**
 * Class turunan (subclass) dari Produk untuk menu makanan.
 *
 * Konsep yang ditunjukkan di sini:
 *  - INHERITANCE: kata kunci "extends" membuat MenuMakanan mewarisi attribute dan method Produk
 *  - Pemakaian "super" untuk memanggil constructor dan method milik superclass
 *  - POLYMORPHISM (overriding): deskripsi() dan hitungHarga(int) ditulis ulang
 */
public class MenuMakanan extends Produk {

    private static final double BIAYA_PER_LEVEL_PEDAS = 500;

    private int tingkatPedas; // private: attribute tambahan khusus makanan

    public MenuMakanan(String nama, double harga, int tingkatPedas) {
        super(nama, harga);        // memanggil constructor Produk
        this.kategori = "Makanan"; // attribute protected milik Produk dapat diakses langsung
        setTingkatPedas(tingkatPedas);
    }

    public int getTingkatPedas() {
        return tingkatPedas;
    }

    public void setTingkatPedas(int tingkatPedas) {
        if (tingkatPedas < 0 || tingkatPedas > 5) {
            throw new IllegalArgumentException("Tingkat pedas harus antara 0 dan 5");
        }
        this.tingkatPedas = tingkatPedas;
    }

    @Override
    public String deskripsi() {
        return super.deskripsi() + ", pedas level " + tingkatPedas;
    }

    @Override
    public double hitungHarga(int jumlah) {
        // harga dasar dari Produk, ditambah biaya level pedas tiap porsi
        return super.hitungHarga(jumlah) + BIAYA_PER_LEVEL_PEDAS * tingkatPedas * jumlah;
    }
}
