package id.ramenku;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import id.ramenku.spring.Konteks;
import id.ramenku.ui.Ui;
import java.awt.Color;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Main {

    private static final Preferences PILIHAN = Preferences.userNodeForPackage(Main.class);

    public static void main(String[] args) {
        Ui.pakaiTema(bacaTema());
        terapkanTema();
        new Thread(Konteks::ambil, "spring-awal").start();   // Spring disiapkan sambil halaman login tampil
        SwingUtilities.invokeLater(Main::tampilkanLogin);
    }

    /** Tema terakhir yang dipilih pengguna (disimpan per komputer); bawaannya Kedai. */
    private static Ui.Tema bacaTema() {
        try {
            return Ui.Tema.valueOf(PILIHAN.get("tema", Ui.Tema.KEDAI.name()));
        } catch (IllegalArgumentException e) {
            return Ui.Tema.KEDAI;
        }
    }

    /** Beralih antara tema Klasik dan Kedai, mengingat pilihannya. Jendela yang terbuka perlu dibuat ulang. */
    static void gantiTema() {
        Ui.pakaiTema(Ui.tema.lainnya());
        PILIHAN.put("tema", Ui.tema.name());
        terapkanTema();
    }

    /** Memasang tampilan FlatLaf sesuai tema yang aktif di {@link Ui}. */
    static void terapkanTema() {
        if (Ui.baru()) {
            FlatLaf.setGlobalExtraDefaults(Map.of(
                    "@background", "#F4EBDC",
                    "@foreground", "#2E1C14",
                    "@accentColor", "#DD8A2D",
                    "@selectionBackground", "#FBE6C0",
                    "@selectionForeground", "#2E1C14"));
        } else {
            FlatLaf.setGlobalExtraDefaults(null);
        }
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Component.focusColor", Ui.AKSEN);
        UIManager.put("Component.focusedBorderColor", Ui.AKSEN);
        if (Ui.baru()) {
            UIManager.put("Panel.background", Ui.KERTAS);
            UIManager.put("Button.background", new Color(0xFFFBF3));
            UIManager.put("Button.foreground", Ui.TINTA);
            UIManager.put("Button.borderColor", new Color(0xDCCBB0));
            UIManager.put("Button.hoverBackground", new Color(0xFFF1D6));
            UIManager.put("Button.default.background", Ui.AKSEN);
            UIManager.put("Button.default.foreground", Ui.TEKS_AKSEN);
            UIManager.put("Component.borderColor", new Color(0xDCCBB0));
            UIManager.put("TextField.background", new Color(0xFFFDF8));
            UIManager.put("PasswordField.background", new Color(0xFFFDF8));
            UIManager.put("ComboBox.background", new Color(0xFFFDF8));
            UIManager.put("Spinner.background", new Color(0xFFFDF8));
            UIManager.put("Table.background", Ui.KARTU);
            UIManager.put("Table.gridColor", Ui.GARIS);
            UIManager.put("TableHeader.background", Ui.HEADER);
            UIManager.put("TableHeader.foreground", Ui.HEADER_TEKS);
            UIManager.put("TableHeader.bottomSeparatorColor", Ui.AKSEN);
            UIManager.put("TabbedPane.underlineColor", Ui.AKSEN);
            UIManager.put("TabbedPane.selectedForeground", Ui.TINTA);
            UIManager.put("TabbedPane.hoverColor", new Color(0xFBE6C0));
            UIManager.put("ScrollBar.thumb", new Color(0xD9C6A6));
            UIManager.put("OptionPane.background", Ui.KERTAS);
        }
    }

    static void tampilkanLogin() {
        JFrame f = new JFrame("RamenKu - Login");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setContentPane(new LoginForm(p -> {
            f.dispose();
            new JendelaUtama(p).setVisible(true);
        }));
        f.pack();
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
