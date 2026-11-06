package id.ramenku.data;

import java.math.BigDecimal;

/** Satu baris resep: berapa banyak sebuah bahan dipakai untuk satu porsi menu. */
public class ResepBaris {

    private final int id;
    private final int bahanId;
    private final String bahanNama;
    private final String satuan;
    private final BigDecimal jumlah;

    public ResepBaris(int id, int bahanId, String bahanNama, String satuan, BigDecimal jumlah) {
        this.id = id;
        this.bahanId = bahanId;
        this.bahanNama = bahanNama;
        this.satuan = satuan;
        this.jumlah = jumlah;
    }

    public int getId() { return id; }
    public int getBahanId() { return bahanId; }
    public String getBahanNama() { return bahanNama; }
    public String getSatuan() { return satuan; }
    public BigDecimal getJumlah() { return jumlah; }
}
