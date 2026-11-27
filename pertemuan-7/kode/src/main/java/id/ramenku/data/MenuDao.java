package id.ramenku.data;

import id.ramenku.spring.PesanBilaTerpakai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** DAO menu dan resep (fitur Menu dan Resep), dikelola Spring. */
@Repository
public class MenuDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public List<Menu> semua() {
        return em.createQuery("from Menu order by nama", Menu.class).getResultList();
    }

    @Transactional
    public int tambah(Menu m) {
        em.persist(m);
        return m.getId();
    }

    @Transactional
    public void ubah(Menu m) {
        Menu asli = em.find(Menu.class, m.getId());
        if (asli == null) throw new IllegalStateException("Menu tidak ditemukan, mungkin sudah dihapus.");
        asli.setNama(m.getNama());
        asli.setHarga(m.getHarga());
    }

    /** Menghapus menu beserta resepnya. Menu yang sudah pernah terjual tidak bisa dihapus. */
    @Transactional
    @PesanBilaTerpakai("Menu ini sudah pernah terjual, jadi tidak dapat dihapus.")
    public void hapus(int id) {
        Menu m = em.find(Menu.class, id);
        if (m != null) em.remove(m);
    }

    @Transactional(readOnly = true)
    public List<ResepBaris> resep(int menuId) {
        return em.createQuery(
                        "select r from ResepBaris r join fetch r.bahan b where r.menu.id = :m order by b.nama", ResepBaris.class)
                .setParameter("m", menuId)
                .getResultList();
    }

    /** Menambah bahan ke resep; bila bahan itu sudah ada di resep, jumlahnya diganti. */
    @Transactional
    public void simpanResep(int menuId, int bahanId, BigDecimal jumlah) {
        if (jumlah == null || jumlah.signum() <= 0) throw new IllegalArgumentException("Jumlah bahan harus lebih dari 0.");
        List<ResepBaris> ada = em.createQuery(
                        "from ResepBaris r where r.menu.id = :m and r.bahan.id = :b", ResepBaris.class)
                .setParameter("m", menuId)
                .setParameter("b", bahanId)
                .getResultList();
        if (!ada.isEmpty()) {
            ada.get(0).setJumlah(jumlah);
        } else {
            em.persist(new ResepBaris(em.getReference(Menu.class, menuId), em.getReference(Bahan.class, bahanId), jumlah));
        }
    }

    @Transactional
    public void hapusResep(int resepId) {
        ResepBaris r = em.find(ResepBaris.class, resepId);
        if (r != null) em.remove(r);
    }
}
