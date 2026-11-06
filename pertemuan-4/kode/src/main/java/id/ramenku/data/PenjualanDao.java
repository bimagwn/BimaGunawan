package id.ramenku.data;

import id.ramenku.Angka;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

/** Penjualan lewat Hibernate: simpan nota, kurangi stok bahan sesuai resep, dan baca riwayat. */
public class PenjualanDao {

    /**
     * Menyimpan pesanan sebagai satu nota dalam satu transaksi: nota, isi nota, dan pengurangan stok
     * berhasil semuanya atau dibatalkan semuanya. Bila stok bahan tidak cukup, nota ditolak.
     *
     * @return nomor nota yang baru
     */
    public int simpan(List<ItemPesanan> pesanan) {
        if (pesanan == null || pesanan.isEmpty()) throw new IllegalArgumentException("Pesanan masih kosong.");
        return HibernateUtil.transaksi("Nota", s -> {
            Map<Integer, BigDecimal> perlu = kebutuhanBahan(s, pesanan);
            Map<Integer, Bahan> bahan = kunciDanPeriksaStok(s, perlu);

            BigDecimal total = BigDecimal.ZERO;
            for (ItemPesanan i : pesanan) total = total.add(i.getSubtotal());

            Nota nota = new Nota(LocalDateTime.now(), total);
            for (ItemPesanan i : pesanan) {
                nota.tambahBaris(s.getReference(Menu.class, i.getMenu().getId()), i.getJumlah(), i.getMenu().getHarga());
            }
            s.persist(nota);                                   // isi nota ikut tersimpan (cascade)

            for (Map.Entry<Integer, BigDecimal> e : perlu.entrySet()) {
                Bahan b = bahan.get(e.getKey());
                b.setStok(b.getStok().subtract(e.getValue())); // stok berkurang, tersimpan otomatis
            }
            return nota.getId();
        });
    }

    /** Total bahan yang dipakai pesanan ini: jumlah di resep dikali jumlah porsi. */
    private Map<Integer, BigDecimal> kebutuhanBahan(Session s, List<ItemPesanan> pesanan) {
        Map<Integer, BigDecimal> perlu = new LinkedHashMap<>();
        for (ItemPesanan i : pesanan) {
            List<ResepBaris> resep = s.createQuery("from ResepBaris r where r.menu.id = :m", ResepBaris.class)
                    .setParameter("m", i.getMenu().getId())
                    .list();
            for (ResepBaris r : resep) {
                BigDecimal butuh = r.getJumlah().multiply(BigDecimal.valueOf(i.getJumlah()));
                perlu.merge(r.getBahanId(), butuh, BigDecimal::add);
            }
        }
        return perlu;
    }

    /** Mengunci baris bahan lalu memastikan stoknya cukup; bila tidak, melempar pesan yang merinci. */
    private Map<Integer, Bahan> kunciDanPeriksaStok(Session s, Map<Integer, BigDecimal> perlu) {
        Map<Integer, Bahan> hasil = new LinkedHashMap<>();
        List<String> kurang = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> e : perlu.entrySet()) {
            Bahan b = s.find(Bahan.class, e.getKey(), LockModeType.PESSIMISTIC_WRITE);
            hasil.put(e.getKey(), b);
            if (b.getStok().compareTo(e.getValue()) < 0) {
                kurang.add("- " + b.getNama() + ": butuh " + Angka.tulis(e.getValue()) + " "
                        + b.getSatuan() + ", tersisa " + Angka.tulis(b.getStok()));
            }
        }
        if (!kurang.isEmpty()) {
            throw new IllegalStateException("Stok bahan tidak cukup, nota ditolak:\n" + String.join("\n", kurang));
        }
        return hasil;
    }

    public List<Nota> riwayat() {
        return HibernateUtil.baca(s -> s.createQuery("from Nota order by id desc", Nota.class)
                .setMaxResults(200)
                .list());
    }

    public Nota nota(int id) {
        return HibernateUtil.baca(s -> {
            Nota n = s.get(Nota.class, id);
            if (n == null) throw new IllegalStateException("Nota nomor " + id + " tidak ditemukan.");
            return n;
        });
    }

    public List<BarisNota> isi(int notaId) {
        return HibernateUtil.baca(s -> s.createQuery(
                        "select b from BarisNota b join fetch b.menu where b.nota.id = :n order by b.id", BarisNota.class)
                .setParameter("n", notaId)
                .list());
    }
}
