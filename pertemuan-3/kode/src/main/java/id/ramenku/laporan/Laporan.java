package id.ramenku.laporan;

import id.ramenku.Koneksi;
import id.ramenku.data.Nota;
import id.ramenku.data.PenjualanDao;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
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

/**
 * Pembuat laporan PDF dengan JasperReports. Desain laporan ada di resources/laporan/*.jrxml,
 * datanya diambil langsung dari MySQL lewat koneksi JDBC, hasilnya disimpan di folder laporan.
 */
public final class Laporan {

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
        Map<String, Object> param = new HashMap<>();
        param.put("TANGGAL", LocalDateTime.now().format(TAMPIL));
        return buat("stok_bahan", param, "Stok_Bahan_" + LocalDateTime.now().format(NAMA_FILE) + ".pdf");
    }

    /** Membuat PDF struk untuk satu nota yang sudah tersimpan. */
    public static Path struk(int notaId) throws Exception {
        Nota nota = new PenjualanDao().nota(notaId);
        Map<String, Object> param = new HashMap<>();
        param.put("NOTA_ID", nota.getId());
        param.put("WAKTU", nota.getWaktu().format(TAMPIL));
        param.put("TOTAL", nota.getTotal());
        return buat("struk", param, "Struk_" + nota.getId() + "_" + LocalDateTime.now().format(NAMA_FILE) + ".pdf");
    }

    private static Path buat(String desain, Map<String, Object> param, String namaFile) throws Exception {
        param.put(JRParameter.REPORT_LOCALE, INDONESIA);
        try (InputStream in = Laporan.class.getResourceAsStream("/laporan/" + desain + ".jrxml");
             Connection c = Koneksi.buka()) {
            if (in == null) throw new IOException("Desain laporan " + desain + ".jrxml tidak ditemukan.");
            JasperReport laporan = JasperCompileManager.compileReport(in);
            JasperPrint cetak = JasperFillManager.fillReport(laporan, param, c);
            Files.createDirectories(FOLDER);
            Path berkas = FOLDER.resolve(namaFile).toAbsolutePath();
            JasperExportManager.exportReportToPdfFile(cetak, berkas.toString());
            return berkas;
        }
    }

    /** Membuka PDF dengan aplikasi bawaan komputer. */
    public static void buka(Path berkas) throws IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(berkas.toFile());
        }
    }
}
