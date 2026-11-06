package id.ramenku.data;

import java.math.BigDecimal;

/** Satu baris isi nota yang sudah tersimpan. */
public class BarisNota {

    private final String menu;
    private final int jumlah;
    private final BigDecimal hargaSatuan;

    public BarisNota(String menu, int jumlah, BigDecimal hargaSatuan) {
        this.menu = menu;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public String getMenu() { return menu; }
    public int getJumlah() { return jumlah; }
    public BigDecimal getHargaSatuan() { return hargaSatuan; }
    public BigDecimal getSubtotal() { return hargaSatuan.multiply(BigDecimal.valueOf(jumlah)); }
}
