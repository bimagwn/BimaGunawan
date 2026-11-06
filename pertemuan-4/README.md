# 🗓️ Pertemuan 4: ORM Java: Hibernate

Jumat, 6 November 2026

Ringkasan: lapisan data dipindah dari JDBC ke Hibernate (entitas, HQL, dan transaksi).

| Bagian | Isi |
|---|---|
| Materi (PPT) | [PPT_Pertemuan4_ORM.pdf](materi/PPT_Pertemuan4_ORM.pdf) |
| LP 4 | [jawaban.md](LP/jawaban.md) |
| TP 4 | [jawaban.md](TP/jawaban.md) |
| Kode | [kode/](kode) |
| Laporan Akhir (LA 4) | [LA4_BimaGunawan_50423276.docx](LA/LA4_BimaGunawan_50423276.docx) |
| Output | [output/](output) |

## Hibernate di RamenKu

Akses data tidak lagi memakai JDBC langsung (`PreparedStatement` dan `ResultSet`), tetapi **Hibernate** (versi 6.6.59.Final). Class Java dipetakan ke tabel lewat anotasi, SQL dibuat otomatis oleh Hibernate, dan perubahan pada objek tersimpan otomatis.

| Entitas (class) | Tabel | Relasi |
|---|---|---|
| `Pengguna` | `pengguna` | login diperiksa dengan HQL |
| `Bahan` | `bahan` | - |
| `Menu` | `menu` | - |
| `ResepBaris` | `resep` | banyak-ke-satu ke `Menu` dan `Bahan` |
| `Nota` | `penjualan` | satu-ke-banyak ke `BarisNota` (cascade) |
| `BarisNota` | `penjualan_item` | banyak-ke-satu ke `Nota` dan `Menu` |

- `HibernateUtil` membuat `SessionFactory`, menjalankan pekerjaan di dalam transaksi (commit atau rollback), dan menerjemahkan galat database menjadi pesan yang mudah dipahami.
- Transaksi penjualan: nota, isi nota, dan pengurangan stok disimpan sekaligus; bila stok bahan tidak cukup, semuanya dibatalkan.
- JasperReports tetap dipakai untuk struk dan laporan stok. Koneksi datanya dipinjam dari Hibernate (`session.doReturningWork`).
- Dialek bawaan `MariaDBDialect` (XAMPP). Untuk MySQL asli, ubah kunci `dialect` di `koneksi.properties` (lihat `koneksi.properties.contoh`).

[Kembali ke README utama](../README.md)
