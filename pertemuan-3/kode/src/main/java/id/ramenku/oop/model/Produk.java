package id.ramenku.oop.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class induk (superclass) untuk semua produk yang dijual kedai RamenKu.
 *
 * Konsep yang ditunjukkan di sini:
 *  - Class, object, attribute, dan method
 *  - ENCAPSULATION: attribute dibuat private dan diakses lewat getter dan setter
 *  - Hak akses: private, protected, default (tanpa modifier), dan public
 *  - POLYMORPHISM (overloading): method hitungHarga() punya tiga versi dengan parameter berbeda
 */
public class Produk {

    // ===================== ATTRIBUTE =====================
    private static int urutan = 0;      // private: penghitung milik class, dipakai semua objek
    private final String kode;          // private: kode otomatis, tidak dapat diubah dari luar
    private String nama;                // private: hanya dapat diubah lewat setNama()
    private double harga;               // private: hanya dapat diubah lewat setHarga()
    protected String kategori = "Umum"; // protected: dapat diakses subclass dan class sepaket

    // ===================== CONSTRUCTOR =====================
    public Produk(String nama, double harga) {
        urutan++;
        this.kode = String.format("PRD-%03d", urutan);
        setNama(nama);      // lewat setter supaya aturan validasi ikut berjalan
        setHarga(harga);
    }

    // ============ GETTER dan SETTER (ENCAPSULATION) ============
    /** Getter: membaca nilai attribute private. */
    public String getNama() {
        return nama;
    }

    /** Setter: mengisi attribute private, sekaligus menjaga agar nilainya tetap wajar. */
    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0");
        }
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    /** Hak akses default (tanpa modifier): hanya dapat dipakai class dalam package yang sama. */
    String kodeProduk() {
        return kode;
    }

    // ===================== METHOD =====================
    /** Keterangan singkat produk. Method ini akan di-override oleh class turunan. */
    public String deskripsi() {
        return nama + " (" + kategori + ") - " + rupiah(harga);
    }

    /** Harga untuk sejumlah porsi. Method ini juga di-override oleh class turunan. */
    public double hitungHarga(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0");
        }
        return harga * jumlah;
    }

    // ============ POLYMORPHISM: OVERLOADING ============
    // Nama method sama (hitungHarga), tetapi daftar parameternya berbeda.

    /** Versi 2: ditambah diskon dalam persen. */
    public double hitungHarga(int jumlah, double diskonPersen) {
        if (diskonPersen < 0 || diskonPersen > 100) {
            throw new IllegalArgumentException("Diskon harus antara 0 dan 100 persen");
        }
        return hitungHarga(jumlah) * (1 - diskonPersen / 100);
    }

    /** Versi 3: ditambah kode promo. */
    public double hitungHarga(int jumlah, String kodePromo) {
        double diskon = 0;
        if ("HEMAT10".equalsIgnoreCase(kodePromo)) {
            diskon = 10;
        } else if ("RAMEN20".equalsIgnoreCase(kodePromo)) {
            diskon = 20;
        }
        return hitungHarga(jumlah, diskon);
    }

    /** Format angka menjadi rupiah, misalnya Rp 25.000. */
    public static String rupiah(double nilai) {
        DecimalFormatSymbols simbol = new DecimalFormatSymbols(new Locale("id", "ID"));
        return "Rp " + new DecimalFormat("#,##0", simbol).format(nilai);
    }
}
