# TP 5: Spring Java

Mata praktikum Rekayasa Perangkat Lunak 2, Pertemuan 5. Jawaban oleh Bima Gunawan (NPM 50423276, 4IA24).

## Soal

![Soal TP 5](soal.png)

## Jawaban

**1. Jelaskan apa itu Spring Framework dan mengapa Spring disebut framework yang _lightweight_!**
Spring adalah _application framework_ untuk aplikasi Java (JEE) yang dipakai membangun aplikasi _enterprise_. Spring disebut _lightweight_ (ringan) karena hanya mengelola objek lewat Dependency Injection dan IoC, sehingga kode infrastruktur berkurang dan tidak membebani aplikasi.

**2. Bagaimana fitur JDBC Exception Handling pada Spring memudahkan developer dalam menangani error?**
Spring menyediakan lapisan abstrak JDBC dengan _exception_ yang berbentuk hierarki, sehingga error database dapat ditangani sesuai jenisnya tanpa mengurus kode galat yang rumit dari JDBC.

**3. Jelaskan peran Spring Web MVC dan Spring Context dalam arsitektur Spring!**
**Spring Web MVC** menyediakan implementasi pola _Model-View-Controller_ untuk aplikasi web. **Spring Context** dibangun di atas paket _beans_ dan menambah dukungan sumber pesan, pola _Observer_, serta akses sumber daya yang konsisten.

**4. Jelaskan pengertian Hibernate dan fungsinya dalam _object/relational persistence_!**
Hibernate adalah _tools_ berkinerja tinggi untuk _object/relational persistence_ dan _query_. Hibernate memetakan class Java ke tabel basis data (dan tipe data Java ke tipe data SQL), sehingga objek dapat disimpan dan dibaca tanpa SQL dan JDBC manual.

[Kembali ke Pertemuan 5](../README.md)
