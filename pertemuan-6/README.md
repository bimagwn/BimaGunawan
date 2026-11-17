# 🗓️ Pertemuan 6: JasperReports (PDF, Word, dan Excel)

Jumat, 20 November 2026

Ringkasan: laporan JasperReports pada RamenKu kini dapat diunduh dalam tiga format, yaitu PDF, Word, dan Excel, dari satu desain laporan yang sama. Kode, output, dan LP sudah ada, materi dan tugas lainnya menyusul.

| Bagian | Isi |
|---|---|
| Materi (PPT) | [materi/](materi) (belum ada) |
| LP 6 | [jawaban.md](LP/jawaban.md) |
| TP 6 | [TP/](TP) (belum ada) |
| Kode | [kode/](kode) |
| Laporan Akhir (LA 6) | [LA/](LA) (belum ada) |
| Output | [output/](output) |
| Jaspersoft Studio | [jaspersoft/](jaspersoft) (cara memasang, membuka desain, dan alat uji laporan) |

## Unduh laporan di RamenKu

Satu desain laporan JasperReports (`.jrxml`) diekspor ke tiga format memakai eksportir bawaan JasperReports.

| Format | Eksportir | Berkas |
|---|---|---|
| PDF | `JasperExportManager` | `.pdf` |
| Word | `JRDocxExporter` | `.docx` |
| Excel | `JRXlsxExporter` | `.xlsx` |

- Halaman **Stok Bahan**: tombol **Unduh PDF**, **Unduh Word**, dan **Unduh Excel** untuk laporan stok.
- Tab **Riwayat Nota** pada halaman **Penjualan**: tombol **Unduh Struk PDF**, **Word**, dan **Excel** untuk nota yang dipilih.
- Hasil tersimpan di folder `laporan` dan langsung dibuka. Contoh hasilnya ada di [output/contoh-hasil](output/contoh-hasil).

## Tampilan baru

Pada pertemuan ini tampilan aplikasi dan desain laporan juga disegarkan dengan warna bahan ramen: krim kaldu, cokelat shoyu, oranye miso, dan kuning mi. Halaman login menampilkan mangkuk ramen dilihat dari atas, dan laporan stok serta struk (PDF, Word, Excel) memakai warna yang sama. Desainnya ada di `stok_bahan.jrxml` dan `struk.jrxml`, tangkapan layarnya di [output/tampilan-baru](output/tampilan-baru).

[Kembali ke README utama](../README.md)
