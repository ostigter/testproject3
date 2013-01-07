package org.ozsoft.vault;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.io.IOUtils;
import org.ozsoft.encryption.EncryptionException;
import org.ozsoft.encryption.Encryptor;

public class Main {
    
    private static final String APPLICATION_TITLE = "Vault";
    
    private static final int WIDTH = 800;

    private static final int HEIGHT = 600;

    private static final File DATA_FILE = new File("vault.dat");
    
    private static final String HEADER = "XXXXXXXXXXXXXXXXXXXX";
    
    private JFrame frame;
    
    private JTextArea textArea;
    
    private NewPasswordDialog passwordDialog;
    
    private Encryptor encryptor;
    
    private boolean isKeySet = false;
    
    private boolean isModified = false;

    public static void main(String[] args) {
        new Main();
    }
    
    public Main() {
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
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                isModified = true;
            }
            
            @Override
            public void insertUpdate(DocumentEvent arg0) {
                isModified = true;
            }
            
            @Override
            public void removeUpdate(DocumentEvent arg0) {
                isModified = true;
            }
        });
        frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(textArea));
        
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void load() {
        if (DATA_FILE.isFile()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(DATA_FILE));
                String cipherHeader = dis.readUTF();
                IOUtils.closeQuietly(dis);
                boolean passwordOK = false;
                while (!passwordOK) {
                    RequestPasswordDialog dialog = new RequestPasswordDialog(frame);
                    if (dialog.showDialog()) {
                        try {
                            encryptor.setKey(dialog.getPassword());
                            if (encryptor.encrypt(HEADER).equals(cipherHeader)) {
                                passwordOK = true;
                                isKeySet = true;
                                dis = new DataInputStream(new FileInputStream(DATA_FILE));
                                dis.readUTF(); // skip header
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                encryptor.decrypt(dis, baos);
                                textArea.setText(baos.toString());
                                IOUtils.closeQuietly(baos);
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
            } catch (IOException e) {
                System.err.println("Could not read file: " + e);
            } finally {
                IOUtils.closeQuietly(dis);
            }
        }
    }
    
    private void save() {
        if (!isKeySet) {
            if (passwordDialog.showDialog()) {
                try {
                    encryptor.setKey(passwordDialog.getPassword());
                    isKeySet = true;
                } catch (EncryptionException e) {
                    System.err.println("ERROR: Could not set encryption key: " + e);
                }
            }
        }
        
        if (isKeySet) {
            sortLines();
            DataOutputStream dos = null;
            try {
                dos = new DataOutputStream(new FileOutputStream(DATA_FILE));
                dos.writeUTF(encryptor.encrypt(HEADER));
                InputStream plainIn = IOUtils.toInputStream(textArea.getText());
                encryptor.encrypt(plainIn, dos);
                IOUtils.closeQuietly(plainIn);
            } catch (EncryptionException e) {
                System.err.println("ERROR: Could not encrypt data: " + e);
            } catch (IOException e) {
                System.err.println("ERROR: Could not write file: " + e);
            } finally {
                IOUtils.closeQuietly(dos);
            }
        }
    }
    
    private void sortLines() {
        String text = textArea.getText();
        List<String> lines = new ArrayList<String>();
        for (String line : text.split("\n")) {
            lines.add(line);
        }
        Collections.sort(lines);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append('\n');
        }
        textArea.setText(sb.toString());
    }

    private void close() {
        if (isModified) {
            save();
        }
        frame.dispose();
    }
    
}
