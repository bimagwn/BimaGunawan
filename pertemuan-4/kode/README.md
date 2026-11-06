# Kode Pertemuan 4: Hibernate (ORM)

Versi RamenKu pada Pertemuan 4. Akses data dipindah dari JDBC ke **Hibernate 6.6.59.Final**.

- Entitas dipetakan ke tabel lewat anotasi: `Pengguna`, `Bahan`, `Menu`, `ResepBaris`, `Nota`, `BarisNota`.
- `HibernateUtil` membuat `SessionFactory`, menjalankan transaksi (commit atau rollback), dan menerjemahkan galat database.
- DAO (`BahanDao`, `MenuDao`, `PenjualanDao`) memakai HQL dan Session, tanpa SQL manual.
- Tampilan, login, dan laporan JasperReports sama seperti Pertemuan 3.
- Dialek bawaan `MariaDBDialect` (XAMPP); dapat diganti lewat `koneksi.properties` (lihat `koneksi.properties.contoh`).

## Menjalankan
1. Nyalakan MySQL di XAMPP dan impor `database/ramenku_db.sql`.
2. Klik ganda `bangun.bat`, lalu `jalankan.bat`.

Kode ini sama dengan proyek di akar repo (versi terbaru).

[Kembali ke Pertemuan 4](../README.md)
