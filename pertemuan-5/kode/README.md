# Kode Pertemuan 5: Spring Java

Versi RamenKu pada Pertemuan 5. **Spring 6.2.19** mengelola Hibernate lewat lapisan DAO, dengan alur NetBeans, Spring (DAO), Hibernate, dan MySQL.

- `Konfigurasi` (paket `id.ramenku.spring`) menyiapkan koneksi MySQL (HikariCP), `SessionFactory` Hibernate, dan pengelola transaksi.
- DAO (`BahanDao`, `MenuDao`, `PenjualanDao`, `PenggunaDao`) bertanda `@Repository`; objeknya dibuat dan disuntikkan oleh Spring (Dependency Injection).
- Transaksi memakai `@Transactional` (transaksi deklaratif), tanpa kode commit dan rollback manual.
- `PenerjemahGalat` adalah aspek AOP yang menerjemahkan galat database menjadi pesan yang mudah dipahami.
- Tampilan, login, dan laporan JasperReports sama seperti pertemuan sebelumnya.

## Menjalankan
1. Nyalakan MySQL di XAMPP dan impor `database/ramenku_db.sql`.
2. Klik ganda `bangun.bat`, lalu `jalankan.bat`.

Kode ini sama dengan proyek di akar repo (versi terbaru).

[Kembali ke Pertemuan 5](../README.md)
