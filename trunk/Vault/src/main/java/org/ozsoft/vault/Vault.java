package org.ozsoft.vault;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.ozsoft.encryption.EncryptionException;
import org.ozsoft.encryption.Encryptor;

public class Vault {
    
    private static final String APPLICATION_TITLE = "Vault";
    
    private static final int WIDTH = 400;

    private static final int HEIGHT = 300;

//    private static final File DATA_FILE = new File("vault.dat");
    
    private JFrame frame;
    
    private JTextArea textArea;
    
    private NewPasswordDialog passwordDialog;
    
    private Encryptor encryptor;
    
    private boolean isKeySet = false;

    public static void main(String[] args) {
        new Vault();
    }
    
    public Vault() {
        initUI();
        passwordDialog = new NewPasswordDialog(frame);
        try {
            encryptor = new Encryptor();
            load();
        } catch (EncryptionException e) {
            System.err.println("ERROR: Could not create encryptor: " + e);
        }
    }
    
    private void initUI() {
        frame = new JFrame(APPLICATION_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        frame.getContentPane().setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(textArea));
        
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void close() {
        save();
        frame.dispose();
    }
    
    private void load() {
    }
    
    private void save() {
        if (!isKeySet) {
            if (passwordDialog.showDialog()) {
                String password = passwordDialog.getPassword();
                System.out.format("Password: '%s'\n", password);
            }
        }
    }

}
