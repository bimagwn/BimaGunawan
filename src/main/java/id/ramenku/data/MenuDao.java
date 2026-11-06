package id.ramenku.data;

import java.math.BigDecimal;
import java.util.List;

/** Akses data menu dan resep lewat Hibernate (fitur Menu dan Resep). */
public class MenuDao {

    public List<Menu> semua() {
        return HibernateUtil.baca(s -> s.createQuery("from Menu order by nama", Menu.class).list());
    }

    public int tambah(Menu m) {
        HibernateUtil.transaksi("Menu", s -> {
            s.persist(m);
            return null;
        });
        return m.getId();
    }

    public void ubah(Menu m) {
        HibernateUtil.transaksi("Menu", s -> {
            Menu asli = s.get(Menu.class, m.getId());
            if (asli == null) throw new IllegalStateException("Menu tidak ditemukan, mungkin sudah dihapus.");
            asli.setNama(m.getNama());
            asli.setHarga(m.getHarga());
            return null;
        });
    }

    /** Menghapus menu beserta resepnya. Menu yang sudah pernah terjual tidak bisa dihapus. */
    public void hapus(int id) {
        HibernateUtil.transaksi("Menu", "Menu ini sudah pernah terjual, jadi tidak dapat dihapus.", s -> {
            Menu m = s.get(Menu.class, id);
            if (m != null) s.remove(m);
            return null;
        });
    }

    public List<ResepBaris> resep(int menuId) {
        return HibernateUtil.baca(s -> s.createQuery(
                        "select r from ResepBaris r join fetch r.bahan b where r.menu.id = :m order by b.nama", ResepBaris.class)
                .setParameter("m", menuId)
                .list());
    }

    /** Menambah bahan ke resep; bila bahan itu sudah ada di resep, jumlahnya diganti. */
    public void simpanResep(int menuId, int bahanId, BigDecimal jumlah) {
        if (jumlah == null || jumlah.signum() <= 0) throw new IllegalArgumentException("Jumlah bahan harus lebih dari 0.");
        HibernateUtil.transaksi("Resep", s -> {
            ResepBaris ada = s.createQuery(
                            "from ResepBaris r where r.menu.id = :m and r.bahan.id = :b", ResepBaris.class)
                    .setParameter("m", menuId)
                    .setParameter("b", bahanId)
                    .uniqueResult();
            if (ada != null) {
                ada.setJumlah(jumlah);
            } else {
                s.persist(new ResepBaris(s.getReference(Menu.class, menuId), s.getReference(Bahan.class, bahanId), jumlah));
            }
            return null;
        });
    }

    public void hapusResep(int resepId) {
        HibernateUtil.transaksi("Resep", s -> {
            ResepBaris r = s.get(ResepBaris.class, resepId);
            if (r != null) s.remove(r);
            return null;
        });
    }
}
