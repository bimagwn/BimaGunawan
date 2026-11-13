# Kode Pertemuan 6: JasperReports (PDF, Word, dan Excel)

Versi RamenKu pada Pertemuan 6. Laporan JasperReports kini dapat diunduh dalam **tiga format**: PDF, Word (.docx), dan Excel (.xlsx), dari satu desain laporan yang sama.

- `Laporan.Format` (enum) memilih format hasil: `PDF`, `WORD`, atau `EXCEL`.
- `Laporan.stokBahan(Format)` dan `Laporan.struk(notaId, Format)` membuat laporan stok bahan dan struk penjualan sesuai format yang dipilih.
- Ekspor memakai eksportir bawaan JasperReports: `JasperExportManager` (PDF), `JRDocxExporter` (Word), dan `JRXlsxExporter` (Excel).
- Halaman **Stok Bahan** memiliki tombol **Unduh PDF**, **Unduh Word**, dan **Unduh Excel**. Tab **Riwayat Nota** memiliki tombol **Unduh Struk PDF**, **Word**, dan **Excel**.
- Desain `stok_bahan.jrxml` diberi pola angka khusus untuk Excel agar angka tampil bersih dan tetap tersimpan sebagai angka.
- Spring, Hibernate, dan MySQL sama seperti Pertemuan 5. Tes bertambah menjadi 21 (ada tes pembuatan berkas Word dan Excel).

## Menjalankan
1. Nyalakan MySQL di XAMPP dan impor `database/ramenku_db.sql`.
2. Klik ganda `bangun.bat`, lalu `jalankan.bat`.
3. Hasil unduhan tersimpan di folder `laporan` dan langsung dibuka.

Kode ini sama dengan proyek di akar repo (versi terbaru).

[Kembali ke Pertemuan 6](../README.md)
