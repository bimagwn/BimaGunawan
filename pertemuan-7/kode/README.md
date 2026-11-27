# Kode Pertemuan 7: Proyek RamenKu Selesai

Versi akhir proyek RamenKu, lengkap dengan semua komponen yang diminta pada Pertemuan 7: **NetBeans, JasperReports, MySQL, dan Hibernate**. Proyek ini lulus 21 tes otomatis (`mvn test`).

| Komponen | Berkas di folder ini |
|---|---|
| **NetBeans** | proyek Maven: [`pom.xml`](pom.xml) dan folder [`src/`](src). Buka lewat File, Open Project, pilih folder ini, lalu Run. |
| **JasperReports** | desain [`stok_bahan.jrxml`](src/main/resources/laporan/stok_bahan.jrxml) dan [`struk.jrxml`](src/main/resources/laporan/struk.jrxml), pembuat laporan [`Laporan.java`](src/main/java/id/ramenku/laporan/Laporan.java) (PDF, Word, Excel) |
| **MySQL** | database `ramenku_db`: [`ramenku_db.sql`](database/ramenku_db.sql) (skema dan data contoh), [`ramenku_db_lengkap.sql`](database/ramenku_db_lengkap.sql) (ekspor lengkap struktur dan isi), [`diagram-relasi.png`](database/diagram-relasi.png) |
| **Hibernate** | konfigurasi [`Konfigurasi.java`](src/main/java/id/ramenku/spring/Konfigurasi.java) (Spring mengelola SessionFactory Hibernate), entitas dan DAO di [`data/`](src/main/java/id/ramenku/data) |

## Isi aplikasi

- Login dengan sandi ber-hash SHA-256.
- Menu dan Resep, Stok Bahan (penanda stok menipis), dan Penjualan (kasir dan riwayat nota).
- Laporan stok bahan dan struk penjualan yang dapat diunduh sebagai PDF, Word, dan Excel.

## Menjalankan

1. Nyalakan MySQL di XAMPP.
2. Impor `database/ramenku_db.sql` (atau `ramenku_db_lengkap.sql` untuk data lengkap) lewat phpMyAdmin tab Import.
3. Klik ganda `bangun.bat`, lalu `jalankan.bat`, atau jalankan dari NetBeans.

Kode ini sama dengan proyek di akar repo (versi terbaru).

[Kembali ke Pertemuan 7](../README.md)
