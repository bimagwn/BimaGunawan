# LP 5: Spring Java

Mata praktikum Rekayasa Perangkat Lunak 2, Pertemuan 5. Jawaban oleh Bima Gunawan (NPM 50423276, 4IA24).

## Soal

![Soal LP 5](soal.png)

## Jawaban

**1. Jelaskan pengertian Aspect Oriented Programming (AOP) dan apa tujuan utama paradigma ini dalam pengembangan aplikasi!**
AOP adalah paradigma pemrograman yang memisahkan fungsi pendukung yang dipakai di banyak bagian (_cross-cutting concern_), seperti _logging_, keamanan, dan transaksi, dari logika bisnis utama. Tujuannya agar kode lebih modular, tidak berulang, dan mudah dirawat.

**2. Mengapa suatu class sebaiknya tidak terikat langsung (_tightly coupled_) dengan class lain? Jelaskan bagaimana Dependency Injection mengatasi masalah tersebut!**
Class yang terikat langsung (misalnya membuat objek lain dengan `new`) sulit diubah, diuji, dan dipakai ulang, karena perubahan di satu class ikut merembet ke class lain. Dependency Injection mengatasinya dengan menyuntikkan objek yang dibutuhkan dari luar (lewat konstruktor atau _setter_), sehingga class hanya bergantung pada _interface_ dan hubungannya menjadi longgar.

**3. Jelaskan tugas utama Spring dalam memanage objek-objek pada aplikasi! Berikan contoh dengan SessionFactory dan Session pada Hibernate!**
Spring bertugas membuat, mengatur, dan menghubungkan objek (disebut _bean_) serta mengelola siklus hidupnya lewat _IoC container_. Contohnya, Spring membuat satu **SessionFactory** dari konfigurasi database lalu menyuntikkannya ke class DAO. Dari **SessionFactory** itu dibuka **Session** untuk setiap operasi, dan Spring yang mengatur transaksi serta penutupannya, sehingga programmer tidak perlu membuat dan menutupnya sendiri.

**4. Jelaskan lima fitur utama Spring Framework yang Anda ketahui!**
1. **IoC dan Dependency Injection**, untuk mengelola dan menyuntikkan objek.
2. **AOP**, untuk memisahkan fungsi pendukung seperti _logging_ dan keamanan.
3. **Manajemen transaksi**, agar transaksi database lebih mudah dan konsisten.
4. **Spring MVC**, untuk membangun aplikasi web dengan pola _Model-View-Controller_.
5. **Dukungan akses data**, yang terintegrasi dengan JDBC, Hibernate, dan JPA.

**5. Jelaskan tujuan penggunaan Hibernate bagi pengembang, dan bagaimana Hibernate dapat mengurangi kebutuhan pemrosesan data manual melalui SQL dan JDBC!**
Hibernate adalah ORM yang memetakan class Java ke tabel database, sehingga pengembang bekerja dengan objek dan tidak dengan SQL. Hibernate membuat perintah SQL (**INSERT**, **UPDATE**, **DELETE**, **SELECT**) secara otomatis dan mengubah hasilnya menjadi objek, sehingga kode JDBC yang panjang (membuka koneksi, `PreparedStatement`, dan `ResultSet`) tidak perlu ditulis manual.

[Kembali ke Pertemuan 5](../README.md)
