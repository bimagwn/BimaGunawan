# 🗓️ Pertemuan 5: Spring Java

Jumat, 13 November 2026

Ringkasan: Spring Framework (AOP, Dependency Injection, fitur dan arsitektur Spring) yang mengelola Hibernate lewat DAO ke MySQL. Materi, LP, TP, dan kode sudah ada, laporan akhir menyusul.

| Bagian | Isi |
|---|---|
| Materi (PPT) | [PPT_Pertemuan5_Spring.pdf](materi/PPT_Pertemuan5_Spring.pdf) |
| LP 5 | [jawaban.md](LP/jawaban.md) |
| TP 5 | [jawaban.md](TP/jawaban.md) |
| Kode | [kode/](kode) |
| Laporan Akhir (LA 5) | [LA/](LA) (belum ada) |
| Output | [output/](output) (belum ada) |

## Spring di RamenKu

Alur tugas praktikum: **NetBeans, Spring (DAO), Hibernate, MySQL**. Spring sekarang yang membuat dan mengatur objek Hibernate, sehingga kode untuk membuka dan menutup sesi tidak ditulis lagi.

| Bagian | Peran |
|---|---|
| `Konfigurasi` | menyiapkan koneksi MySQL (HikariCP), `SessionFactory` Hibernate, dan pengelola transaksi |
| DAO `@Repository` | `BahanDao`, `MenuDao`, `PenjualanDao`, `PenggunaDao`; objeknya dibuat dan disuntikkan oleh Spring |
| `@Transactional` | transaksi deklaratif: commit bila berhasil, rollback bila terjadi galat |
| `PenerjemahGalat` | aspek AOP yang mengubah galat database menjadi pesan yang mudah dipahami |
| `Konteks` | pintu masuk ke Spring: `Konteks.bean(BahanDao.class)` |

[Kembali ke README utama](../README.md)
