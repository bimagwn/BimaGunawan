package id.ramenku.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/** Satu menu yang dijual di kedai. Entitas Hibernate untuk tabel menu. */
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 60, unique = true)
    private String nama;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal harga;

    protected Menu() {}

    public Menu(int id, String nama, BigDecimal harga) {
        this.id = id;
        setNama(nama);
        setHarga(harga);
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public BigDecimal getHarga() { return harga; }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) throw new IllegalArgumentException("Nama menu wajib diisi.");
        if (nama.trim().length() > 60) throw new IllegalArgumentException("Nama menu maksimal 60 huruf.");
        this.nama = nama.trim();
    }

    public void setHarga(BigDecimal harga) {
        if (harga == null || harga.signum() <= 0) throw new IllegalArgumentException("Harga harus lebih dari 0.");
        this.harga = harga;
    }

    @Override public String toString() {
        return nama;
    }
}
