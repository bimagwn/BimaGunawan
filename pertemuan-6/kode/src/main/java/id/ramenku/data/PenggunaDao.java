package id.ramenku.data;

import id.ramenku.Pengguna;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** DAO pengguna untuk halaman login, dikelola Spring. */
@Repository
public class PenggunaDao {

    @PersistenceContext
    private EntityManager em;

    /** Mengembalikan pengguna bila username dan sandi cocok, atau null bila salah. */
    @Transactional(readOnly = true)
    public Pengguna masuk(String username, char[] sandi) {
        String hash = Pengguna.sha256(sandi);
        return em.createQuery("from Pengguna where username = :u and sandi = :s", Pengguna.class)
                .setParameter("u", username)
                .setParameter("s", hash)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
