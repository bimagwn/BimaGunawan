# Output Pertemuan 5: Spring Java

[Kembali ke Pertemuan 5](../README.md) | [README utama](../../README.md) | [Kode](../kode) | [LP](../LP/jawaban.md) | [TP](../TP/jawaban.md)

Spring bekerja di belakang layar, jadi tampilan aplikasi sama seperti pertemuan sebelumnya. Buktinya ada pada dependency, hasil build, objek yang dikelola Spring (gambar ketiga), dan tabel MySQL.

**Dependency Spring di pom.xml: spring-context dan spring-orm (6.2.19), aspectjweaver untuk AOP, dan HikariCP untuk kolam koneksi MySQL**

![Dependency Spring di pom.xml: spring-context dan spring-orm (6.2.19), aspectjweaver untuk AOP, dan HikariCP untuk kolam koneksi MySQL](01-dependency-pom.png)

**Hasil Clean and Build di NetBeans: Spring ikut masuk ke aplikasi dan 20 tes lulus (BUILD SUCCESS)**

![Hasil Clean and Build di NetBeans: Spring ikut masuk ke aplikasi dan 20 tes lulus (BUILD SUCCESS)](02-hasil-build-netbeans.png)

**Objek yang dikelola Spring: DAO, SessionFactory Hibernate, dan pengelola transaksi. Query bahan dibuat Hibernate sendiri tanpa SQL manual, dan transaksinya diatur @Transactional**

![Objek yang dikelola Spring: DAO, SessionFactory Hibernate, dan pengelola transaksi. Query bahan dibuat Hibernate sendiri tanpa SQL manual, dan transaksinya diatur @Transactional](03-bean-spring-dan-sql.png)

**MySQL (phpMyAdmin): database ramenku_db berisi 6 tabel yang dipetakan oleh entitas Hibernate dan dibaca lewat DAO Spring**

![MySQL (phpMyAdmin): database ramenku_db berisi 6 tabel yang dipetakan oleh entitas Hibernate dan dibaca lewat DAO Spring](04-mysql-phpmyadmin-daftar-tabel.png)

**MySQL (phpMyAdmin): isi tabel bahan**

![MySQL (phpMyAdmin): isi tabel bahan](05-mysql-phpmyadmin-isi-bahan.png)

**Halaman login RamenKu: pemeriksaan pengguna dilakukan PenggunaDao milik Spring**

![Halaman login RamenKu: pemeriksaan pengguna dilakukan PenggunaDao milik Spring](06-halaman-login.png)

**Beranda RamenKu setelah login: status Database terhubung dicek lewat Spring**

![Beranda RamenKu setelah login: status Database terhubung dicek lewat Spring](07-beranda.png)

**Fitur Stok Bahan berjalan dengan BahanDao yang dikelola Spring**

![Fitur Stok Bahan berjalan dengan BahanDao yang dikelola Spring](08-stok-bahan-spring.png)

**Fitur Menu dan Resep berjalan dengan MenuDao yang dikelola Spring**

![Fitur Menu dan Resep berjalan dengan MenuDao yang dikelola Spring](09-menu-dan-resep-spring.png)

**Fitur Penjualan (kasir) berjalan dengan PenjualanDao, transaksi diatur @Transactional**

![Fitur Penjualan (kasir) berjalan dengan PenjualanDao, transaksi diatur @Transactional](10-penjualan-kasir-spring.png)
