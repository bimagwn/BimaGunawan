# Output Pertemuan 4: Hibernate (ORM)

[Kembali ke Pertemuan 4](../README.md) | [README utama](../../README.md) | [Kode](../kode) | [LP](../LP/jawaban.md) | [TP](../TP/jawaban.md)

Hibernate bekerja di belakang layar, jadi tampilan aplikasi sama seperti Pertemuan 3. Buktinya ada pada dependency, SQL yang dibuat otomatis (gambar kelima), tabel MySQL, hasil build, dan hasil tes.

**Dependency di pom.xml: mysql-connector-j 8.4.0 (driver MySQL) dan hibernate-core 6.6.59.Final (Hibernate)**

![Dependency di pom.xml: mysql-connector-j 8.4.0 (driver MySQL) dan hibernate-core 6.6.59.Final (Hibernate)](01-dependency-pom.png)

**Hasil Clean and Build di NetBeans: Hibernate ikut masuk ke aplikasi dan 20 tes lulus**

![Hasil Clean and Build di NetBeans: Hibernate ikut masuk ke aplikasi dan 20 tes lulus](02-hasil-build-netbeans.png)

**MySQL (phpMyAdmin): database ramenku_db berisi 6 tabel yang dipetakan oleh entitas Hibernate**

![MySQL (phpMyAdmin): database ramenku_db berisi 6 tabel yang dipetakan oleh entitas Hibernate](03-mysql-phpmyadmin-daftar-tabel.png)

**MySQL (phpMyAdmin): isi tabel bahan, yang dibaca dan diubah oleh Hibernate**

![MySQL (phpMyAdmin): isi tabel bahan, yang dibaca dan diubah oleh Hibernate](04-mysql-phpmyadmin-isi-bahan.png)

**SQL yang dibuat Hibernate sendiri di konsol: tidak ada SQL yang ditulis manual. Simpan penjualan menjadi insert nota, insert isi nota, dan update stok dalam satu transaksi**

![SQL yang dibuat Hibernate sendiri di konsol: tidak ada SQL yang ditulis manual. Simpan penjualan menjadi insert nota, insert isi nota, dan update stok dalam satu transaksi](05-sql-dibuat-hibernate.png)

**Fitur Stok Bahan berjalan dengan Hibernate**

![Fitur Stok Bahan berjalan dengan Hibernate](06-stok-bahan-hibernate.png)

**Fitur Menu dan Resep berjalan dengan Hibernate**

![Fitur Menu dan Resep berjalan dengan Hibernate](07-menu-dan-resep-hibernate.png)

**Fitur Penjualan: kasir**

![Fitur Penjualan: kasir](08-penjualan-kasir-hibernate.png)

**Riwayat nota penjualan (relasi Nota ke BarisNota)**

![Riwayat nota penjualan (relasi Nota ke BarisNota)](09-riwayat-nota-hibernate.png)

**Laporan stok bahan (PDF, JasperReports memakai koneksi dari Hibernate)**

![Laporan stok bahan (PDF, JasperReports memakai koneksi dari Hibernate)](10-laporan-stok-pdf.png)

**Struk penjualan (PDF, JasperReports)**

![Struk penjualan (PDF, JasperReports)](11-struk-pdf.png)

**Hasil 20 tes (OOP, angka, dan Hibernate ke MySQL)**

![Hasil 20 tes (OOP, angka, dan Hibernate ke MySQL)](12-hasil-tes.png)
