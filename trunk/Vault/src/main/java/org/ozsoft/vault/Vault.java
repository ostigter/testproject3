package org.ozsoft.vault;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.ozsoft.encryption.EncryptionException;
import org.ozsoft.encryption.Encryptor;

public class Vault {
    
    private static final String APPLICATION_TITLE = "Vault";
    
    private static final int WIDTH = 400;

    private static final int HEIGHT = 300;

    private static final File DATA_FILE = new File("vault.dat");
    
    private static final String FILE_HEADER = "XXXXXXXXXXXXXXXXXXXX";
    
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
    
    private void load() {
        if (DATA_FILE.isFile()) {
            // Read encrypted file header.
            String cipherHeader = null;
            try {
                Reader reader = new BufferedReader(new FileReader(DATA_FILE));
                char[] buffer = new char[FILE_HEADER.length()];
                reader.read(buffer);
                reader.close();
                cipherHeader = new String(buffer);
                
                // Check password using file header.
                boolean passwordOK = false;
                while (!passwordOK) {
                    RequestPasswordDialog dialog = new RequestPasswordDialog(frame);
                    if (dialog.showDialog()) {
                        try {
                            encryptor.setKey(dialog.getPassword());
                            if (encryptor.encrypt(FILE_HEADER).equals(cipherHeader)) {
                                // Password matches; read encrypted text.
                                passwordOK = true;
                            } else {
                                JOptionPane.showMessageDialog(frame, "Invalid password, please try again.", "Vault", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (EncryptionException e) {
                            System.err.println("Could not decrypt file: " + e);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                System.out.println("passwordOK = " + passwordOK);
            } catch (IOException e) {
                System.err.println("Could not read file: " + e);
            }
        }
    }
    
    private void save() {
        if (!isKeySet) {
            if (passwordDialog.showDialog()) {
                String password = passwordDialog.getPassword();
                try {
                    encryptor.setKey(password);
                    try {
                        Writer writer = new BufferedWriter(new FileWriter(DATA_FILE));
                        writer.write(encryptor.encrypt(FILE_HEADER));
                        writer.write(encryptor.encrypt(textArea.getText()));
                        writer.close();
                    } catch (IOException e) {
                        System.err.println("ERROR: Could not write file: " + e);
                    }
                } catch (EncryptionException e) {
                    System.err.println("ERROR: Could not encrypt file: " + e);
                }
            }
        }
    }

    private void close() {
        save();
        frame.dispose();
    }
    
}
