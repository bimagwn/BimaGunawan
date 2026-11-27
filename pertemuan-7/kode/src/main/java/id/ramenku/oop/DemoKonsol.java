package id.ramenku.oop;

import id.ramenku.oop.model.MenuMakanan;
import id.ramenku.oop.model.MenuMinuman;
import id.ramenku.oop.model.Pesanan;
import id.ramenku.oop.model.Produk;

/** Demo di konsol: menjalankan semua konsep OOP dan mencetak hasilnya. Jalankan: java -jar ramenku-oop.jar --demo */
public class DemoKonsol {

    public static void jalankan() {
        System.out.println("=== 1. CLASS dan OBJECT ===");
        MenuMakanan ramen = new MenuMakanan("Ramen Pedas", 28000, 3);
        MenuMinuman esTeh = new MenuMinuman("Es Teh", 5000, true);
        System.out.println(ramen.deskripsi());
        System.out.println(esTeh.deskripsi());

        System.out.println("\n=== 2. ENCAPSULATION (getter dan setter) ===");
        System.out.println("getHarga()  -> " + Produk.rupiah(ramen.getHarga()));
        ramen.setHarga(30000);
        System.out.println("setHarga(30000) -> " + Produk.rupiah(ramen.getHarga()));
        try {
            ramen.setHarga(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("setHarga(-5) ditolak: " + e.getMessage());
        }

        System.out.println("\n=== 3. INHERITANCE (extends dan super) ===");
        System.out.println("ramen instanceof Produk  -> " + (ramen instanceof Produk));
        System.out.println("kategori (diwarisi/protected) -> " + ramen.getKategori());

        System.out.println("\n=== 4. POLYMORPHISM: OVERRIDING ===");
        Produk[] menu = {ramen, esTeh, new Produk("Kerupuk", 3000)};
        for (Produk p : menu) {
            System.out.println(p.getClass().getSimpleName() + " -> hitungHarga(2) = " + Produk.rupiah(p.hitungHarga(2)));
        }

        System.out.println("\n=== 5. POLYMORPHISM: OVERLOADING ===");
        System.out.println("hitungHarga(2)             = " + Produk.rupiah(ramen.hitungHarga(2)));
        System.out.println("hitungHarga(2, 10.0)       = " + Produk.rupiah(ramen.hitungHarga(2, 10.0)));
        System.out.println("hitungHarga(2, \"HEMAT10\")  = " + Produk.rupiah(ramen.hitungHarga(2, "HEMAT10")));

        System.out.println("\n=== 6. PESANAN (campuran objek dalam satu daftar) ===");
        Pesanan pesanan = new Pesanan();
        pesanan.tambah(ramen, 2);
        pesanan.tambah(esTeh, 2);
        for (String baris : pesanan.rincian()) {
            System.out.println(baris);
        }
        System.out.println("TOTAL = " + Produk.rupiah(pesanan.total()));
    }
}
