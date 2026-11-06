package id.ramenku.data;

import java.math.BigDecimal;

/** Satu baris pesanan di kasir: menu dan jumlah porsinya. */
public class ItemPesanan {

    private final Menu menu;
    private int jumlah;

    public ItemPesanan(Menu menu, int jumlah) {
        this.menu = menu;
        setJumlah(jumlah);
    }

    public Menu getMenu() { return menu; }
    public int getJumlah() { return jumlah; }

    public void setJumlah(int jumlah) {
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah porsi minimal 1.");
        this.jumlah = jumlah;
    }

    public BigDecimal getSubtotal() {
        return menu.getHarga().multiply(BigDecimal.valueOf(jumlah));
    }
}
