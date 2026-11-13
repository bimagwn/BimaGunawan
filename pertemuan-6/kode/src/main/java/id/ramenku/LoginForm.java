package id.ramenku;

import id.ramenku.data.PenggunaDao;
import id.ramenku.spring.Konteks;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/** Halaman login: panel merek di kiri, form USERNAME dan PASSWORD di kanan. */
public class LoginForm extends JPanel {

    private static final Color TINTA = PanelMerek.TINTA;
    private static final Color VERMILION = PanelMerek.VERMILION;
    private static final Color KERTAS = PanelMerek.KERTAS;
    private static final Color ABU = new Color(0x6B7080);

    private final JLabel jLabel1 = new JLabel("Selamat datang");
    private final JLabel jLabel2 = new JLabel("USERNAME");
    private final JLabel jLabel3 = new JLabel("PASSWORD");
    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();
    private final JButton jButton1 = new JButton("MASUK");
    private final JLabel pesan = new JLabel(" ");
    private final Consumer<Pengguna> saatBerhasil;

    public LoginForm(Consumer<Pengguna> saatBerhasil) {
        this.saatBerhasil = saatBerhasil;
        setLayout(new BorderLayout());
        add(new PanelMerek(), BorderLayout.WEST);
        add(panelForm(), BorderLayout.CENTER);
        setPreferredSize(new Dimension(760, 440));

        jButton1.addActionListener(e -> jButton1ActionPerformed());
        txtPassword.addActionListener(e -> jButton1ActionPerformed());
        txtUsername.addActionListener(e -> txtPassword.requestFocusInWindow());
    }

    private JPanel panelForm() {
        JPanel luar = new JPanel(new GridBagLayout());
        luar.setBackground(KERTAS);

        JPanel f = new JPanel();
        f.setOpaque(false);
        f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));
        f.setPreferredSize(new Dimension(300, 340));

        jLabel1.setFont(new Font("Georgia", Font.BOLD, 28));
        jLabel1.setForeground(TINTA);
        JLabel sub = new JLabel("Masuk untuk mengelola kedaimu.");
        sub.setForeground(ABU);

        for (JLabel l : new JLabel[] {jLabel2, jLabel3}) {
            l.setFont(l.getFont().deriveFont(Font.BOLD, 11f));
            l.setForeground(ABU);
        }
        txtUsername.putClientProperty("JTextField.placeholderText", "Masukkan username");
        txtPassword.putClientProperty("JTextField.placeholderText", "Masukkan password");
        txtPassword.putClientProperty("JPasswordField.showRevealButton", true);
        for (JTextField t : new JTextField[] {txtUsername, txtPassword}) {
            t.setPreferredSize(new Dimension(300, 40));
            t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            t.putClientProperty("JComponent.minimumWidth", 0);
        }

        jButton1.setBackground(VERMILION);
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(jButton1.getFont().deriveFont(Font.BOLD, 13f));
        jButton1.setPreferredSize(new Dimension(300, 42));
        jButton1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        jButton1.setFocusPainted(false);

        pesan.setForeground(VERMILION);
        pesan.setFont(pesan.getFont().deriveFont(12f));

        JComponent[] urut = {jLabel1, sub, jLabel2, txtUsername, jLabel3, txtPassword, pesan, jButton1};
        for (JComponent c : urut) c.setAlignmentX(LEFT_ALIGNMENT);
        f.add(jLabel1);
        f.add(Box.createVerticalStrut(4));
        f.add(sub);
        f.add(Box.createVerticalStrut(26));
        f.add(jLabel2);
        f.add(Box.createVerticalStrut(6));
        f.add(txtUsername);
        f.add(Box.createVerticalStrut(14));
        f.add(jLabel3);
        f.add(Box.createVerticalStrut(6));
        f.add(txtPassword);
        f.add(Box.createVerticalStrut(8));
        f.add(pesan);
        f.add(Box.createVerticalStrut(8));
        f.add(jButton1);
        luar.add(f);
        return luar;
    }

    private void jButton1ActionPerformed() {
        String user = txtUsername.getText().trim();
        char[] sandi = txtPassword.getPassword();
        if (user.isEmpty() || sandi.length == 0) {
            pesan.setText("Username dan password wajib diisi.");
            Arrays.fill(sandi, '\0');
            return;
        }
        pesan.setText(" ");
        jButton1.setEnabled(false);
        jButton1.setText("MEMERIKSA...");
        new SwingWorker<Pengguna, Void>() {
            @Override protected Pengguna doInBackground() throws SQLException {
                try {
                    return Konteks.bean(PenggunaDao.class).masuk(user, sandi);
                } finally {
                    Arrays.fill(sandi, '\0');
                }
            }

            @Override protected void done() {
                jButton1.setEnabled(true);
                jButton1.setText("MASUK");
                try {
                    Pengguna p = get();
                    if (p == null) {
                        pesan.setText("Username atau password salah.");
                        txtPassword.setText("");
                        txtPassword.requestFocusInWindow();
                    } else {
                        saatBerhasil.accept(p);
                    }
                } catch (Exception ex) {
                    Throwable sebab = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Tidak dapat terhubung ke database.\nPastikan MySQL di XAMPP sudah menyala.\n\n" + sebab.getMessage(),
                            "Koneksi gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
