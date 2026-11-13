package id.ramenku.laporan;

import id.ramenku.data.Nota;
import id.ramenku.data.PenjualanDao;
import id.ramenku.spring.Konteks;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * Pembuat laporan dengan JasperReports dalam tiga format: PDF, Word (.docx), dan Excel (.xlsx).
 * Desain laporan ada di resources/laporan/*.jrxml dan datanya diambil dari MySQL memakai koneksi
 * dari Spring. Satu desain yang sama diekspor ke ketiga format, hasilnya disimpan di folder laporan.
 */
public final class Laporan {

    /** Format berkas hasil laporan beserta ekstensinya. */
    public enum Format {
        PDF("pdf", "PDF"),
        WORD("docx", "Word"),
        EXCEL("xlsx", "Excel");

        private final String ekstensi;
        private final String nama;

        Format(String ekstensi, String nama) {
            this.ekstensi = ekstensi;
            this.nama = nama;
        }

        public String getEkstensi() {
            return ekstensi;
        }

        public String getNama() {
            return nama;
        }
    }

    private static final Path FOLDER = Path.of("laporan");
    private static final Locale INDONESIA = new Locale("id", "ID");
    private static final DateTimeFormatter TAMPIL = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", INDONESIA);
    private static final DateTimeFormatter NAMA_FILE = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    static {
        // jangan gagal bila font Arial tidak ada di komputer lain
        DefaultJasperReportsContext.getInstance().setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
    }

    private Laporan() {}

    /** Membuat PDF daftar stok bahan; bahan yang menipis diberi warna merah. */
    public static Path stokBahan() throws Exception {
        return stokBahan(Format.PDF);
    }

    /** Membuat laporan daftar stok bahan dalam format yang dipilih. */
    public static Path stokBahan(Format format) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("TANGGAL", LocalDateTime.now().format(TAMPIL));
        return buat("stok_bahan", param, "Stok_Bahan_" + LocalDateTime.now().format(NAMA_FILE), format);
    }

    /** Membuat PDF struk untuk satu nota yang sudah tersimpan. */
    public static Path struk(int notaId) throws Exception {
        return struk(notaId, Format.PDF);
    }

    /** Membuat struk untuk satu nota yang sudah tersimpan dalam format yang dipilih. */
    public static Path struk(int notaId, Format format) throws Exception {
        Nota nota = Konteks.bean(PenjualanDao.class).nota(notaId);
        Map<String, Object> param = new HashMap<>();
        param.put("NOTA_ID", nota.getId());
        param.put("WAKTU", nota.getWaktu().format(TAMPIL));
        param.put("TOTAL", nota.getTotal());
        return buat("struk", param, "Struk_" + nota.getId() + "_" + LocalDateTime.now().format(NAMA_FILE), format);
    }

    private static Path buat(String desain, Map<String, Object> param, String namaDasar, Format format) throws Exception {
        param.put(JRParameter.REPORT_LOCALE, INDONESIA);
        try (InputStream in = Laporan.class.getResourceAsStream("/laporan/" + desain + ".jrxml")) {
            if (in == null) throw new IOException("Desain laporan " + desain + ".jrxml tidak ditemukan.");
            JasperReport laporan = JasperCompileManager.compileReport(in);
            // koneksi JDBC dipinjam dari kolam koneksi Spring, lalu dipakai JasperReports untuk membaca data
            JasperPrint cetak = Konteks.denganKoneksi(c -> JasperFillManager.fillReport(laporan, param, c));
            Files.createDirectories(FOLDER);
            Path berkas = FOLDER.resolve(namaDasar + "." + format.getEkstensi()).toAbsolutePath();
            ekspor(cetak, berkas, format);
            return berkas;
        }
    }

    /** Menulis hasil laporan ke berkas sesuai format: PDF, Word, atau Excel. */
    private static void ekspor(JasperPrint cetak, Path berkas, Format format) throws Exception {
        switch (format) {
            case PDF -> JasperExportManager.exportReportToPdfFile(cetak, berkas.toString());
            case WORD -> {
                JRDocxExporter exporter = new JRDocxExporter();
                exporter.setExporterInput(new SimpleExporterInput(cetak));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(berkas.toFile()));
                exporter.exportReport();
            }
            case EXCEL -> {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(cetak));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(berkas.toFile()));
                SimpleXlsxReportConfiguration atur = new SimpleXlsxReportConfiguration();
                atur.setOnePagePerSheet(false);                 // semua halaman dalam satu lembar kerja
                atur.setDetectCellType(true);                   // angka tersimpan sebagai angka, bukan teks
                atur.setRemoveEmptySpaceBetweenRows(true);
                atur.setWhitePageBackground(false);
                exporter.setConfiguration(atur);
                exporter.exportReport();
            }
        }
    }

    /** Membuka berkas laporan dengan aplikasi bawaan komputer (PDF, Word, atau Excel). */
    public static void buka(Path berkas) throws IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(berkas.toFile());
        }
    }
}
