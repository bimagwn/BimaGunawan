# RamenKu OOP (ACT 2 RPL2)

Aplikasi desktop (Java Swing) kecil bertema kedai ramen untuk memperlihatkan tiga konsep OOP:
**Encapsulation**, **Inheritance**, dan **Polymorphism**.

## Peta konsep ke kode

| Konsep | Di mana | Contoh |
|---|---|---|
| Class dan object | `model/Produk.java` | `new MenuMakanan("Ramen Pedas", 28000, 3)` |
| **Encapsulation** | `model/Produk.java` | attribute `nama` dan `harga` bersifat `private`, diakses lewat `getNama()`, `setNama()`, `getHarga()`, `setHarga()`. Setter menolak harga 0 atau negatif dan nama kosong |
| Hak akses | `model/Produk.java` | `private` (nama, harga, kode), `protected` (kategori), default (`kodeProduk()`), `public` (getter, setter) |
| **Inheritance** | `model/MenuMakanan.java`, `model/MenuMinuman.java` | `class MenuMakanan extends Produk`, memakai `super(...)` dan `super.deskripsi()` |
| **Polymorphism: overriding** | `MenuMakanan`, `MenuMinuman` | `deskripsi()` dan `hitungHarga(int)` ditulis ulang dengan `@Override` |
| **Polymorphism: overloading** | `model/Produk.java` | tiga versi `hitungHarga`: `(jumlah)`, `(jumlah, diskonPersen)`, `(jumlah, kodePromo)` |
| Polimorfisme dalam satu daftar | `model/Pesanan.java` | `total()` memanggil `hitungHarga()` pada daftar `Produk` yang isinya campuran subclass |

## Cara menjalankan

**Di NetBeans (17 atau lebih baru, JDK 17 atau lebih baru):**
1. File, Open Project, pilih folder `RamenKuOOP`.
2. Klik kanan proyek, **Run** (kelas utama `id.ramenku.oop.Main`).

**Tanpa NetBeans:** klik ganda `bangun.bat` (membangun), lalu `jalankan.bat` (membuka jendela) atau `demo-konsol.bat` (hasil di konsol).

## Cara memakai jendela

1. **Buat objek:** isi nama dan harga, pilih jenis, klik *Buat objek*. Coba harga `-5` atau nama kosong: setter menolaknya (encapsulation).
2. **Daftar objek:** kolom *Class asli objek* memperlihatkan subclass tiap objek.
3. **Hitung harga:** pilih satu objek, pilih salah satu dari tiga versi `hitungHarga`, lalu klik *Hitung* (overloading dan overriding).
4. **Ubah harga (setter):** mengubah harga lewat `setHarga()` dengan pemeriksaan.
5. **Pesanan:** tambahkan beberapa objek, lalu lihat total yang dihitung secara polimorfik.
6. Panel **Catatan konsep OOP** menjelaskan otomatis konsep apa yang sedang berjalan.

## Pengujian

`mvn test` menjalankan 7 tes otomatis yang membuktikan encapsulation, inheritance, overriding, overloading, dan total pesanan.

[Kembali ke Pertemuan 2](../README.md)
