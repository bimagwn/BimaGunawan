package id.ramenku.oop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Satu pesanan pelanggan yang berisi beberapa produk.
 *
 * Di sinilah POLYMORPHISM terlihat: daftar berisi objek bertipe Produk, padahal isinya
 * campuran MenuMakanan dan MenuMinuman. Saat hitungHarga() dipanggil, Java otomatis
 * memakai versi milik class asli tiap objek.
 */
public class Pesanan {

    private static class Baris {
        private final Produk produk;
        private final int jumlah;

        private Baris(Produk produk, int jumlah) {
            this.produk = produk;
            this.jumlah = jumlah;
        }
    }

    private final List<Baris> daftar = new ArrayList<>();

    public void tambah(Produk produk, int jumlah) {
        if (produk == null) {
            throw new IllegalArgumentException("Produk tidak boleh kosong");
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0");
        }
        daftar.add(new Baris(produk, jumlah));
    }

    public double total() {
        double hasil = 0;
        for (Baris b : daftar) {
            hasil += b.produk.hitungHarga(b.jumlah); // polimorfisme: versi tiap subclass dipakai
        }
        return hasil;
    }

    public List<String> rincian() {
        List<String> hasil = new ArrayList<>();
        for (Baris b : daftar) {
            hasil.add(b.jumlah + " x " + b.produk.getNama() + " [" + b.produk.kodeProduk() + "] = "
                    + Produk.rupiah(b.produk.hitungHarga(b.jumlah)));
        }
        return hasil;
    }

    public int banyakBaris() {
        return daftar.size();
    }

    public void kosongkan() {
        daftar.clear();
    }
}
