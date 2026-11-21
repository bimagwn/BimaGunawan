# 🗓️ Pertemuan 6: JasperReports (PDF, Word, dan Excel)

Jumat, 20 November 2026

Ringkasan: laporan JasperReports pada RamenKu kini dapat diunduh dalam tiga format, yaitu PDF, Word, dan Excel, dari satu desain laporan yang sama. Materi, kode, output, LP, dan TP sudah ada, Laporan Akhir menyusul.

| Bagian | Isi |
|---|---|
| Materi (PPT) | [PPT_Pertemuan6_JasperReports.pdf](materi/PPT_Pertemuan6_JasperReports.pdf) |
| LP 6 | [jawaban.md](LP/jawaban.md) |
| TP 6 | [jawaban.md](TP/jawaban.md) |
| Kode | [kode/](kode) |
| Laporan Akhir (LA 6) | [LA/](LA) (belum ada) |
| Makalah | [makalah/](makalah) (template makalah laboratorium, Word dan PDF) |
| Output | [output/](output) |
| Proses di Jaspersoft Studio | [output/studio](output/studio) (menyambungkan database, Preview, dan ekspor langkah demi langkah) |
| Proses dan hasil | [output/proses](output/proses) (urutan pengerjaan laporan beserta keluarannya) |
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
