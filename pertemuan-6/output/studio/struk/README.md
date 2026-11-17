# Proses di Jaspersoft Studio: struk penjualan

[Kembali ke proses laporan stok](../README.md) | [Output Pertemuan 6](../../README.md) | [Proses dan hasil](../../proses/README.md)

Langkah menampilkan dan mengekspor `struk.jrxml` di Jaspersoft Studio. Koneksi database memakai adapter `RamenKu` yang sama seperti pada [laporan stok](../README.md).

**1. Membuka `struk.jrxml`** lewat *File, Open File*. Tampilan Design menunjukkan bilah judul, nota, daftar menu, total, dan ucapan terima kasih.

![Desain struk di Studio](01-desain-struk.png)

**2. Tab Preview.** Struk punya tiga parameter: `NOTA_ID`, `WAKTU`, dan `TOTAL`.

![Tab Preview dan parameter](02-tab-preview-parameter.png)

**3. Mengisi parameter dan menjalankan.** `NOTA_ID` diisi `1` (nota nomor 1 di database), `WAKTU` diisi `13 November 2026, 08:55`, `TOTAL` diisi `84000`. Adapter yang dipakai `RamenKu`.

![Parameter terisi dan hasilnya](02b-parameter-terisi-dan-hasil.png)

**4. Hasil Preview.** Struk tampil dengan data nota dari database: Ramen Shoyu 3 x Rp 28.000, total Rp 84.000. Statistik mencatat 1 record dan tanpa galat.

![Hasil Preview struk](03-preview-struk.png)

## Hasil ekspor dari Studio

Diekspor dari menu ekspor yang sama seperti laporan stok, dengan nama `struk_studio`.

**PDF:** [struk_studio.pdf](struk_studio.pdf)

![Struk PDF](04-hasil-pdf.png)

**Word:** [struk_studio.docx](struk_studio.docx)

![Struk Word](05-hasil-word.png)

**Excel:** [struk_studio.xlsx](struk_studio.xlsx)

![Struk Excel](06-hasil-excel.png)
