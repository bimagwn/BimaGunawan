package id.ramenku.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Satu nota penjualan. Entitas Hibernate untuk tabel penjualan; isi notanya (BarisNota)
 * ikut disimpan otomatis karena relasinya memakai cascade.
 */
@Entity
@Table(name = "penjualan")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDateTime waktu;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "nota", cascade = CascadeType.ALL)
    private List<BarisNota> baris = new ArrayList<>();

    protected Nota() {}

    public Nota(LocalDateTime waktu, BigDecimal total) {
        this.waktu = waktu;
        this.total = total;
    }

    public int getId() { return id; }
    public LocalDateTime getWaktu() { return waktu; }
    public BigDecimal getTotal() { return total; }

    /** Menambah satu baris ke nota ini. */
    public void tambahBaris(Menu menu, int jumlah, BigDecimal hargaSatuan) {
        baris.add(new BarisNota(this, menu, jumlah, hargaSatuan));
    }
}
