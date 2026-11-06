package id.ramenku.data;

import java.math.BigDecimal;

/** Bahan baku kedai. Nilai dicek di setter supaya data yang masuk database selalu wajar. */
public class Bahan {

    private final int id;
    private String nama;
    private String satuan;
    private BigDecimal stok;
    private BigDecimal stokMinimal;

    public Bahan(int id, String nama, String satuan, BigDecimal stok, BigDecimal stokMinimal) {
        this.id = id;
        setNama(nama);
        setSatuan(satuan);
        setStok(stok);
        setStokMinimal(stokMinimal);
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getSatuan() { return satuan; }
    public BigDecimal getStok() { return stok; }
    public BigDecimal getStokMinimal() { return stokMinimal; }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) throw new IllegalArgumentException("Nama bahan wajib diisi.");
        if (nama.trim().length() > 60) throw new IllegalArgumentException("Nama bahan maksimal 60 huruf.");
        this.nama = nama.trim();
    }

    public void setSatuan(String satuan) {
        if (satuan == null || satuan.trim().isEmpty()) throw new IllegalArgumentException("Satuan wajib diisi (misalnya gram, ml, butir).");
        if (satuan.trim().length() > 15) throw new IllegalArgumentException("Satuan maksimal 15 huruf.");
        this.satuan = satuan.trim();
    }

    public void setStok(BigDecimal stok) {
        if (stok == null || stok.signum() < 0) throw new IllegalArgumentException("Stok tidak boleh negatif.");
        this.stok = stok;
    }

    public void setStokMinimal(BigDecimal stokMinimal) {
        if (stokMinimal == null || stokMinimal.signum() < 0) throw new IllegalArgumentException("Stok minimal tidak boleh negatif.");
        this.stokMinimal = stokMinimal;
    }

    /** Stok dianggap menipis bila sudah sama dengan atau di bawah batas minimal. */
    public boolean isMenipis() {
        return stok.compareTo(stokMinimal) <= 0;
    }

    @Override public String toString() {
        return nama + " (" + satuan + ")";
    }
}
