package id.ramenku.data;

import java.util.List;

/** Akses data bahan lewat Hibernate (fitur Stok Bahan): tidak ada SQL yang ditulis manual. */
public class BahanDao {

    public List<Bahan> semua() {
        return HibernateUtil.baca(s -> s.createQuery("from Bahan order by nama", Bahan.class).list());
    }

    /** Menambah bahan baru dan mengembalikan id-nya. */
    public int tambah(Bahan b) {
        HibernateUtil.transaksi("Bahan", s -> {
            s.persist(b);
            return null;
        });
        return b.getId();
    }

    public void ubah(Bahan b) {
        HibernateUtil.transaksi("Bahan", s -> {
            Bahan asli = s.get(Bahan.class, b.getId());
            if (asli == null) throw new IllegalStateException("Bahan tidak ditemukan, mungkin sudah dihapus.");
            asli.setNama(b.getNama());          // perubahan terdeteksi otomatis (dirty checking)
            asli.setSatuan(b.getSatuan());
            asli.setStok(b.getStok());
            asli.setStokMinimal(b.getStokMinimal());
            return null;
        });
    }

    public void hapus(int id) {
        HibernateUtil.transaksi("Bahan", s -> {
            Bahan b = s.get(Bahan.class, id);
            if (b != null) s.remove(b);
            return null;
        });
    }
}
