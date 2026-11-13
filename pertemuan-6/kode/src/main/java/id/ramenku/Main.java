package id.ramenku;

import com.formdev.flatlaf.FlatLightLaf;
import id.ramenku.spring.Konteks;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("Component.focusColor", PanelMerek.VERMILION);
        UIManager.put("Component.focusedBorderColor", PanelMerek.VERMILION);
        new Thread(Konteks::ambil, "spring-awal").start();   // Spring disiapkan sambil halaman login tampil
        SwingUtilities.invokeLater(Main::tampilkanLogin);
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
