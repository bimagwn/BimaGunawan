package id.ramenku.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Ringkasan satu nota penjualan. */
public class Nota {

    private final int id;
    private final LocalDateTime waktu;
    private final BigDecimal total;

    public Nota(int id, LocalDateTime waktu, BigDecimal total) {
        this.id = id;
        this.waktu = waktu;
        this.total = total;
    }

    public int getId() { return id; }
    public LocalDateTime getWaktu() { return waktu; }
    public BigDecimal getTotal() { return total; }
}
