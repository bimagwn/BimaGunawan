package id.ramenku.spring;

import java.sql.SQLException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;

/**
 * Contoh AOP (Aspect Oriented Programming). Menerjemahkan galat database menjadi pesan yang mudah
 * dipahami dan mengembalikan pesan aturan bisnis apa adanya. Aturannya ditulis satu kali di sini,
 * lalu Spring memasangnya otomatis ke semua method DAO, jadi kode DAO tidak perlu memuat try-catch
 * yang berulang.
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)      // dijalankan paling luar, supaya galat saat commit ikut tertangkap
public class PenerjemahGalat {

    private static final int DUPLIKAT = 1062;
    private static final int MASIH_DIPAKAI = 1451;

    @Around("execution(public * id.ramenku.data.*Dao.*(..))")
    public Object terjemah(ProceedingJoinPoint jp) throws Throwable {
        try {
            return jp.proceed();
        } catch (RuntimeException e) {
            // aturan bisnis (misalnya stok tidak cukup) dibungkus Spring; kembalikan pesan aslinya
            if (e instanceof InvalidDataAccessApiUsageException && e.getCause() instanceof RuntimeException asli
                    && (asli instanceof IllegalStateException || asli instanceof IllegalArgumentException)) {
                throw asli;
            }
            int kode = kodeSql(e);
            if (kode == DUPLIKAT) {
                throw new IllegalStateException(objek(jp) + " dengan nama itu sudah ada.");
            }
            if (kode == MASIH_DIPAKAI) {
                PesanBilaTerpakai khusus = ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(PesanBilaTerpakai.class);
                throw new IllegalStateException(khusus != null ? khusus.value()
                        : objek(jp) + " tidak dapat dihapus karena masih dipakai data lain.");
            }
            throw e;
        }
    }

    /** Nama objek dari nama DAO-nya, misalnya BahanDao menjadi "Bahan". */
    private static String objek(ProceedingJoinPoint jp) {
        String nama = jp.getTarget().getClass().getSimpleName();
        return nama.endsWith("Dao") ? nama.substring(0, nama.length() - 3) : nama;
    }

    /** Mencari kode galat MySQL pada rantai penyebab galat. */
    private static int kodeSql(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof SQLException s) return s.getErrorCode();
        }
        return 0;
    }
}
