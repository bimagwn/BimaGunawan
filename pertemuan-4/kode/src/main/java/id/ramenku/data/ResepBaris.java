package id.ramenku.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * Satu baris resep: berapa banyak sebuah bahan dipakai untuk satu porsi menu.
 * Entitas Hibernate untuk tabel resep, dengan relasi banyak-ke-satu ke menu dan bahan.
 */
@Entity
@Table(name = "resep")
public class ResepBaris {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "bahan_id")
    private Bahan bahan;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal jumlah;

    protected ResepBaris() {}

    public ResepBaris(Menu menu, Bahan bahan, BigDecimal jumlah) {
        this.menu = menu;
        this.bahan = bahan;
        setJumlah(jumlah);
    }

    public int getId() { return id; }
    public int getBahanId() { return bahan.getId(); }
    public String getBahanNama() { return bahan.getNama(); }
    public String getSatuan() { return bahan.getSatuan(); }
    public BigDecimal getJumlah() { return jumlah; }

    public void setJumlah(BigDecimal jumlah) {
        if (jumlah == null || jumlah.signum() <= 0) throw new IllegalArgumentException("Jumlah bahan harus lebih dari 0.");
        this.jumlah = jumlah;
    }
}
