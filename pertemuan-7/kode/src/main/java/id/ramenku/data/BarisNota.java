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

/** Satu baris isi nota yang sudah tersimpan. Entitas Hibernate untuk tabel penjualan_item. */
@Entity
@Table(name = "penjualan_item")
public class BarisNota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "penjualan_id")
    private Nota nota;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private int jumlah;

    @Column(name = "harga_satuan", nullable = false, precision = 12, scale = 2)
    private BigDecimal hargaSatuan;

    protected BarisNota() {}

    BarisNota(Nota nota, Menu menu, int jumlah, BigDecimal hargaSatuan) {
        this.nota = nota;
        this.menu = menu;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public String getNamaMenu() { return menu.getNama(); }
    public int getJumlah() { return jumlah; }
    public BigDecimal getHargaSatuan() { return hargaSatuan; }
    public BigDecimal getSubtotal() { return hargaSatuan.multiply(BigDecimal.valueOf(jumlah)); }
}
