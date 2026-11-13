package id.ramenku.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import id.ramenku.laporan.Laporan;
import id.ramenku.spring.Konteks;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tes DAO (Spring dan Hibernate) ke MySQL memakai database khusus ramenku_uji yang dibuat dari skrip database/ramenku_db.sql
 * dan dihapus lagi. Bila MySQL tidak menyala, tes ini dilewati.
 */
class DataTest {

    private static final String ADMIN = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";
    private static boolean siap;

    private BahanDao bahanDao;
    private MenuDao menuDao;
    private PenjualanDao penjualanDao;

    @BeforeEach
    void siapkanDatabase() throws Exception {
        try (Connection c = DriverManager.getConnection(ADMIN, "root", "")) {
            siap = true;
        } catch (SQLException e) {
            siap = false;
        }
        assumeTrue(siap, "MySQL tidak menyala, tes database dilewati");
        System.setProperty("ramenku.url", "jdbc:mysql://localhost:3306/ramenku_uji?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
        try (Connection c = DriverManager.getConnection(ADMIN, "root", ""); Statement st = c.createStatement()) {
            st.execute("DROP DATABASE IF EXISTS ramenku_uji");
            StringBuilder sb = new StringBuilder();
            for (String baris : Files.readAllLines(Path.of("database/ramenku_db.sql"))) {
                if (!baris.trim().startsWith("--")) sb.append(baris).append('\n');
            }
            for (String perintah : sb.toString().replace("ramenku_db", "ramenku_uji").split(";\\s*\\n")) {
                if (!perintah.isBlank()) st.execute(perintah);
            }
        }
        Konteks.tutup();            // Spring dibuat ulang dan tersambung ke database uji yang baru dibuat
        bahanDao = Konteks.bean(BahanDao.class);
        menuDao = Konteks.bean(MenuDao.class);
        penjualanDao = Konteks.bean(PenjualanDao.class);
    }

    @AfterAll
    static void bersihkan() throws Exception {
        Konteks.tutup();
        if (!siap) return;
        try (Connection c = DriverManager.getConnection(ADMIN, "root", ""); Statement st = c.createStatement()) {
            st.execute("DROP DATABASE IF EXISTS ramenku_uji");
        }
        System.clearProperty("ramenku.url");
    }

    private Bahan cari(String nama) throws SQLException {
        return bahanDao.semua().stream().filter(b -> b.getNama().equals(nama)).findFirst().orElseThrow();
    }

    private Menu menu(String nama) throws SQLException {
        return menuDao.semua().stream().filter(m -> m.getNama().equals(nama)).findFirst().orElseThrow();
    }

    @Test
    void dataContohTerbaca() throws Exception {
        assertEquals(5, bahanDao.semua().size());
        assertEquals(3, menuDao.semua().size());
        assertEquals(4, menuDao.resep(menu("Ramen Shoyu").getId()).size());
    }

    @Test
    void bahanBisaDitambahDiubahDanDihapus() throws Exception {
        int id = bahanDao.tambah(new Bahan(0, "Nori", "lembar", new BigDecimal("100"), new BigDecimal("20")));
        Bahan nori = cari("Nori");
        assertEquals(id, nori.getId());
        nori.setStok(new BigDecimal("15"));
        bahanDao.ubah(nori);
        assertTrue(cari("Nori").isMenipis());          // 15 <= 20
        bahanDao.hapus(id);
        assertEquals(5, bahanDao.semua().size());
    }

    @Test
    void bahanTidakValidDitolak() {
        assertThrows(IllegalArgumentException.class, () -> new Bahan(0, " ", "gram", BigDecimal.ONE, BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> new Bahan(0, "X", "gram", new BigDecimal("-1"), BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> new Menu(0, "X", BigDecimal.ZERO));
    }

    @Test
    void namaBahanKembarDitolakDenganPesanRamah() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> bahanDao.tambah(new Bahan(0, "Telur", "butir", BigDecimal.ONE, BigDecimal.ONE)));
        assertTrue(e.getMessage().contains("sudah ada"));
    }

    @Test
    void bahanYangDipakaiResepTidakBisaDihapus() throws Exception {
        int id = cari("Telur").getId();
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> bahanDao.hapus(id));
        assertTrue(e.getMessage().contains("masih dipakai"));
    }

    @Test
    void resepBisaDiubahDanBahanDiganti() throws Exception {
        Menu m = menu("Telur Ajitama");
        menuDao.simpanResep(m.getId(), cari("Daun Bawang").getId(), new BigDecimal("3"));
        assertEquals(2, menuDao.resep(m.getId()).size());
        menuDao.simpanResep(m.getId(), cari("Daun Bawang").getId(), new BigDecimal("4"));   // ganti jumlah, bukan baris baru
        assertEquals(2, menuDao.resep(m.getId()).size());
        menuDao.hapusResep(menuDao.resep(m.getId()).get(0).getId());
        assertEquals(1, menuDao.resep(m.getId()).size());
    }

    @Test
    void penjualanMengurangiStokSesuaiResep() throws Exception {
        int nota = penjualanDao.simpan(List.of(new ItemPesanan(menu("Ramen Shoyu"), 3), new ItemPesanan(menu("Telur Ajitama"), 2)));
        assertEquals(0, new BigDecimal("47").compareTo(cari("Mi Ramen").getStok()));       // 50 - 3
        assertEquals(0, new BigDecimal("4100").compareTo(cari("Kaldu Ayam").getStok()));   // 5000 - 900
        assertEquals(0, new BigDecimal("38").compareTo(cari("Telur").getStok()));          // 40 - 2
        assertEquals(0, new BigDecimal("96000").compareTo(penjualanDao.nota(nota).getTotal()));   // 3*28000 + 2*6000
        assertEquals(2, penjualanDao.isi(nota).size());
        assertEquals(1, penjualanDao.riwayat().size());
    }

    @Test
    void stokKurangMenolakNotaDanTidakMengubahApaPun() throws Exception {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> penjualanDao.simpan(List.of(new ItemPesanan(menu("Ramen Shoyu"), 2), new ItemPesanan(menu("Telur Ajitama"), 41))));
        assertTrue(e.getMessage().contains("Telur"));
        assertEquals(0, new BigDecimal("40").compareTo(cari("Telur").getStok()));
        assertEquals(0, new BigDecimal("50").compareTo(cari("Mi Ramen").getStok()));    // transaksi dibatalkan utuh
        assertTrue(penjualanDao.riwayat().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> penjualanDao.simpan(List.of()));
    }

    @Test
    void menuYangSudahTerjualTidakBisaDihapus() throws Exception {
        penjualanDao.simpan(List.of(new ItemPesanan(menu("Ramen Pedas"), 1)));
        int id = menu("Ramen Pedas").getId();
        assertThrows(IllegalStateException.class, () -> menuDao.hapus(id));
        int bebas = menu("Telur Ajitama").getId();
        menuDao.hapus(bebas);                                   // belum pernah terjual: boleh, resepnya ikut terhapus
        assertEquals(2, menuDao.semua().size());
    }

    @Test
    void laporanPdfStokDanStrukTerbentuk() throws Exception {
        Path stok = Laporan.stokBahan();
        assertPdf(stok);
        int nota = penjualanDao.simpan(List.of(new ItemPesanan(menu("Ramen Shoyu"), 2)));
        assertPdf(Laporan.struk(nota));
    }

    @Test
    void laporanWordDanExcelTerbentuk() throws Exception {
        int nota = penjualanDao.simpan(List.of(new ItemPesanan(menu("Ramen Shoyu"), 2)));
        assertOoxml(Laporan.stokBahan(Laporan.Format.WORD), "word/document.xml");
        assertOoxml(Laporan.stokBahan(Laporan.Format.EXCEL), "xl/workbook.xml");
        assertOoxml(Laporan.struk(nota, Laporan.Format.WORD), "word/document.xml");
        assertOoxml(Laporan.struk(nota, Laporan.Format.EXCEL), "xl/workbook.xml");
    }

    /** Berkas Word dan Excel (.docx dan .xlsx) adalah arsip ZIP yang berisi bagian utama dokumen. */
    private static void assertOoxml(Path berkas, String bagianUtama) throws IOException {
        assertTrue(Files.exists(berkas), "Berkas tidak terbentuk: " + berkas);
        assertTrue(Files.size(berkas) > 1500, "Berkas terlalu kecil");
        try (java.util.zip.ZipFile zip = new java.util.zip.ZipFile(berkas.toFile())) {
            assertTrue(zip.getEntry(bagianUtama) != null, berkas + " tidak memuat " + bagianUtama);
        }
    }

    private static void assertPdf(Path berkas) throws IOException {
        assertTrue(Files.exists(berkas), "PDF tidak terbentuk: " + berkas);
        byte[] isi = Files.readAllBytes(berkas);
        assertTrue(isi.length > 800, "PDF terlalu kecil");
        assertEquals("%PDF", new String(isi, 0, 4));
        assertFalse(berkas.toString().isEmpty());
    }
}
