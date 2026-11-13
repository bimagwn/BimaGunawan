package id.ramenku.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO bahan (fitur Stok Bahan). Objek ini dibuat dan dikelola Spring (@Repository); transaksinya
 * diatur Spring lewat @Transactional, sehingga tidak ada kode begin, commit, atau rollback di sini.
 */
@Repository
public class BahanDao {

    @PersistenceContext
    private EntityManager em;       // disuntikkan oleh Spring

    @Transactional(readOnly = true)
    public List<Bahan> semua() {
        return em.createQuery("from Bahan order by nama", Bahan.class).getResultList();
    }

    /** Menambah bahan baru dan mengembalikan id-nya. */
    @Transactional
    public int tambah(Bahan b) {
        em.persist(b);
        return b.getId();
    }

    @Transactional
    public void ubah(Bahan b) {
        Bahan asli = em.find(Bahan.class, b.getId());
        if (asli == null) throw new IllegalStateException("Bahan tidak ditemukan, mungkin sudah dihapus.");
        asli.setNama(b.getNama());          // perubahan terdeteksi otomatis (dirty checking)
        asli.setSatuan(b.getSatuan());
        asli.setStok(b.getStok());
        asli.setStokMinimal(b.getStokMinimal());
    }

    @Transactional
    public void hapus(int id) {
        Bahan b = em.find(Bahan.class, id);
        if (b != null) em.remove(b);
    }
}
