package id.ramenku.oop;

import id.ramenku.oop.ui.JendelaUtama;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Titik masuk aplikasi desktop RamenKu OOP. */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--demo")) {
            DemoKonsol.jalankan();
            return;
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // tetap memakai tampilan bawaan Swing
        }
        SwingUtilities.invokeLater(() -> new JendelaUtama().setVisible(true));
    }
}
