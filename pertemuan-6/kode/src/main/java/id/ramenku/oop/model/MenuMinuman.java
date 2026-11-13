package id.ramenku.oop.model;

/**
 * Class turunan (subclass) dari Produk untuk menu minuman.
 * Sama seperti MenuMakanan, class ini memakai extends dan melakukan overriding.
 */
public class MenuMinuman extends Produk {

    private static final double BIAYA_ES = 1000;

    private boolean dingin; // private: attribute tambahan khusus minuman

    public MenuMinuman(String nama, double harga, boolean dingin) {
        super(nama, harga);
        this.kategori = "Minuman";
        this.dingin = dingin;
    }

    public boolean isDingin() {
        return dingin;
    }

    public void setDingin(boolean dingin) {
        this.dingin = dingin;
    }

    @Override
    public String deskripsi() {
        return super.deskripsi() + (dingin ? ", dingin" : ", hangat");
    }

    @Override
    public double hitungHarga(int jumlah) {
        // minuman dingin dikenakan biaya es per gelas
        return super.hitungHarga(jumlah) + (dingin ? BIAYA_ES * jumlah : 0);
    }
}
