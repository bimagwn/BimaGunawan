import java.nio.file.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.*;
import net.sf.jasperreports.export.*;

/**
 * Alat uji laporan: java -cp ramenku.jar Ekspor.java <berkas.jrxml> <folder-keluar> [param=nilai ...]
 * Membaca desain langsung dari disk (tanpa build ulang), mengisi dari MySQL, mengekspor PDF + Word + Excel.
 */
public class Ekspor {
    public static void main(String[] a) throws Exception {
        Path desain = Path.of(a[0]);
        Path keluar = Path.of(a[1]);
        Files.createDirectories(keluar);
        Locale id = new Locale("id", "ID");
        Map<String, Object> param = new HashMap<>();
        param.put(JRParameter.REPORT_LOCALE, id);
        param.put("TANGGAL", LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", id)));
        Map<String, String> mentah = new LinkedHashMap<>();
        // parameter berbentuk nama=nilai; cmd.exe memecah "nama=nilai" menjadi dua argumen, jadi dua bentuk diterima
        for (int i = 2; i < a.length; i++) {
            String k, s;
            if (a[i].contains("=")) {
                String[] kv = a[i].split("=", 2);
                k = kv[0];
                s = kv[1];
            } else {
                k = a[i];
                s = a[++i];
            }
            mentah.put(k, s);
        }
        String url = System.getProperty("db.url", "jdbc:mysql://localhost:3306/ramenku_db?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
        net.sf.jasperreports.engine.DefaultJasperReportsContext.getInstance()
                .setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        JasperReport laporan = JasperCompileManager.compileReport(desain.toString());
        // ubah nilai teks ke tipe yang dideklarasikan desain (Integer, BigDecimal, dst.)
        for (JRParameter p : laporan.getParameters()) {
            String s = mentah.get(p.getName());
            if (s == null || p.isSystemDefined()) continue;
            Class<?> t = p.getValueClass();
            Object v = s;
            if (t == Integer.class) v = Integer.valueOf(s);
            else if (t == Long.class) v = Long.valueOf(s);
            else if (t == Double.class) v = Double.valueOf(s);
            else if (t == java.math.BigDecimal.class) v = new java.math.BigDecimal(s);
            param.put(p.getName(), v);
        }
        try (Connection c =DriverManager.getConnection(url, "root", "")) {
            JasperPrint cetak = JasperFillManager.fillReport(laporan, param, c);
            String dasar = desain.getFileName().toString().replace(".jrxml", "");
            Path pdf = keluar.resolve(dasar + ".pdf");
            JasperExportManager.exportReportToPdfFile(cetak, pdf.toString());
            Path docx = keluar.resolve(dasar + ".docx");
            JRDocxExporter w = new JRDocxExporter();
            w.setExporterInput(new SimpleExporterInput(cetak));
            w.setExporterOutput(new SimpleOutputStreamExporterOutput(docx.toFile()));
            w.exportReport();
            Path xlsx = keluar.resolve(dasar + ".xlsx");
            JRXlsxExporter x = new JRXlsxExporter();
            x.setExporterInput(new SimpleExporterInput(cetak));
            x.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsx.toFile()));
            SimpleXlsxReportConfiguration cfg = new SimpleXlsxReportConfiguration();
            cfg.setOnePagePerSheet(false);
            cfg.setDetectCellType(true);
            cfg.setRemoveEmptySpaceBetweenRows(true);
            cfg.setWhitePageBackground(false);
            x.setConfiguration(cfg);
            x.exportReport();
            System.out.println("Halaman: " + cetak.getPages().size());
            for (Path p : List.of(pdf, docx, xlsx)) System.out.println("OK " + p.toAbsolutePath() + "  (" + Files.size(p) + " B)");
        }
    }
}
