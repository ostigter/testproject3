package org.ozsoft.portal.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.ozsoft.encryption.EncryptionException;
import org.ozsoft.encryption.Encryptor;
import org.ozsoft.httpclient.HttpClient;
import org.ozsoft.httpclient.HttpRequest;
import org.ozsoft.httpclient.HttpResponse;
import org.ozsoft.timer.Timer;
import org.ozsoft.timer.TimerListener;

/**
 * Portal client.
 * 
 * @author Oscar Stigter
 */
public class PortalClient implements TimerListener {
    
    /** Portal server URL. */
    private static final String SERVER_URL = "http://localhost:8080/portal/";
  
    /** Default Telnet host. */
    private static final String DEFAULT_HOST = "localhost";
    
    /** Default Telnet port. */
    private static final int DEFAULT_PORT = 23;
    
    /** Interval in ms for polling for incoming Telnet data. */
    private static final int RECEIVE_INTERVAL = 500;
    
    /** Shared key used for encryption. */
    private static final String ENCRYPTION_KEY = "F5iQ!w6#pYm&wB";
  
    /** Application title. */
    private static final String APP_TITLE = "Portal Client";

    /** Default application window width in pixels. */
    private static final int APP_WIDTH = 800;

    /** Default application window height in pixels. */
    private static final int APP_HEIGHT = 600;

    /** Font used for textfields. */
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /** Whether we are connected to the portal server. */
    private boolean isPortalConnected;
    
    /** Whether the portal server has a Telnet connection. */
    private boolean isTelnetConnected;
    
    /** HTTP client. */
    private final HttpClient httpClient;
    
    /** Timer for receiving Telnet data. */
    private final Timer receiveTimer;
    
    /** Encryptor. */
    private Encryptor encryptor;
    
    // UI components.
    private JFrame frame;
    private JTextField serverUrlText;
    private JButton serverConnectButton;
    private JTextField telnetHostText;
    private JTextField telnetPortText;
    private JButton telnetConnectButton;
    private JTextArea consoleText;
    private JTextField sendText;

    /**
     * Application's entry point.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new PortalClient();
    }
    
    /**
     * Constructs the portal client.
     */
    public PortalClient() {
        httpClient = new HttpClient();
//        httpClient.setUseProxy(true);
//        httpClient.setProxyHost("146.106.91.10");
//        httpClient.setProxyPort(8080);
//        httpClient.setProxyUsername("");
//        httpClient.setProxyPassword("");
        
        try {
            encryptor = new Encryptor();
            encryptor.setKey(ENCRYPTION_KEY);
        } catch (EncryptionException e) {
            throw new RuntimeException("ERROR: Could not initialize encryptor", e);
        }
        
        receiveTimer = new Timer(this, RECEIVE_INTERVAL);
        
        createUI();
        
        setPortalConnected(false);
    }
    
    private void createUI() {
        frame = new JFrame(APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel serverPanel = new JPanel(new GridBagLayout());
        serverPanel.setBorder(new TitledBorder("Portal server"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(serverPanel, gbc);

        JLabel label = new JLabel("Server URL:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        serverPanel.add(label, gbc);

        serverUrlText = new JTextField(String.valueOf(SERVER_URL));
        serverUrlText.setFont(FONT);
        serverUrlText.setDisabledTextColor(Color.GRAY);
        serverUrlText.setPreferredSize(new Dimension(400, 25));
        serverUrlText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                serverUrlText.selectAll();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        serverPanel.add(serverUrlText, gbc);

        serverConnectButton = new JButton("Connect");
        serverConnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverConnectDisconnect();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        serverPanel.add(serverConnectButton, gbc);

        JPanel telnetPanel = new JPanel(new GridBagLayout());
        telnetPanel.setBorder(new TitledBorder("Telnet connection"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(telnetPanel, gbc);

        label = new JLabel("Host:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        telnetPanel.add(label, gbc);

        telnetHostText = new JTextField(DEFAULT_HOST);
        telnetHostText.setFont(FONT);
        telnetHostText.setDisabledTextColor(Color.GRAY);
        telnetHostText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                telnetHostText.selectAll();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        telnetPanel.add(telnetHostText, gbc);

        label = new JLabel("Port:");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        telnetPanel.add(label, gbc);

        telnetPortText = new JTextField(String.valueOf(DEFAULT_PORT));
        telnetPortText.setFont(FONT);
        telnetPortText.setDisabledTextColor(Color.GRAY);
        telnetPortText.setPreferredSize(new Dimension(50, 25));
        telnetPortText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                telnetPortText.selectAll();
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        telnetPanel.add(telnetPortText, gbc);

        telnetConnectButton = new JButton("Connect");
        telnetConnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                telnetConnectDisconnect();
            }
        });
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        telnetPanel.add(telnetConnectButton, gbc);

        consoleText = new JTextArea();
        consoleText.setEditable(false);
        consoleText.setFont(FONT);
        consoleText.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(consoleText);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        telnetPanel.add(scrollPane, gbc);

        sendText = new JTextField();
        sendText.setFont(FONT);
        sendText.setDisabledTextColor(Color.GRAY);
        sendText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        telnetPanel.add(sendText, gbc);

        frame.setSize(APP_WIDTH, APP_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        serverConnectButton.requestFocusInWindow();
    }
    
    private void setPortalConnected(boolean isConnected) {
        this.isPortalConnected = isConnected;
        serverUrlText.setEnabled(!isConnected);
        telnetConnectButton.setEnabled(isConnected);
        if (isConnected) {
            serverConnectButton.setText("Disconnect");
        } else {
            serverConnectButton.setText("Connect");
            setTelnetConnected(false);
        }
    }
    
    private void setTelnetConnected(boolean isConnected) {
        this.isTelnetConnected = isConnected;
        telnetHostText.setEnabled(!isConnected);
        telnetPortText.setEnabled(!isConnected);
        sendText.setEditable(isConnected);
        if (isConnected) {
            telnetConnectButton.setText("Disconnect");
            consoleText.setText("");
            sendText.setText("");
            sendText.requestFocusInWindow();
            receiveTimer.start();
            appendText("\n>>> Connected.\n");
        } else {
            telnetConnectButton.setText("Connect");
            receiveTimer.stop();
        }
    }
    
    private void serverConnectDisconnect() {
        if (isPortalConnected) {
            setPortalConnected(false);
        } else {
            checkTelnetConnected();
        }
    }
    
    private void checkTelnetConnected() {
        String response = executeRequest("STATUS");
        if (response != null) {
            String[] parts = response.split(" ");
            if (parts.length == 3 && parts[0].equals("CONNECTED")) {
                telnetHostText.setText(parts[1]);
                telnetPortText.setText(parts[2]);
                setPortalConnected(true);
                setTelnetConnected(true);
            } else {
                setPortalConnected(true);
                setTelnetConnected(false);
            }
        } else {
            setPortalConnected(false);
            setTelnetConnected(false);
        }
    }
    
    private void telnetConnectDisconnect() {
        if (isTelnetConnected) {
            executeRequest("DISCONNECT");
            appendText("\n>>> Disconnected.\n");
            checkTelnetConnected();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
                public void run() {
                   executeRequest(String.format("CONNECT %s %s", telnetHostText.getText(), telnetPortText.getText()));
                   int retries = 10;
                   while (!isTelnetConnected && retries > 0) {
                       checkTelnetConnected();
                       if (!isTelnetConnected) {
                           try {
                               Thread.sleep(500L);
                           } catch (InterruptedException e) {
                               // Ignore.
                           }
                       }
                   }
                } 
            });
        }
    }
    
    private void appendText(String text) {
        consoleText.append(text);
        consoleText.setCaretPosition(consoleText.getText().length());
    }
    
    private void send() {
        if (isTelnetConnected) {
            executeRequest("SEND " + sendText.getText().trim());
            sendText.selectAll();
            sendText.requestFocusInWindow();
        }
    }

    private String executeRequest(String request) {
//        System.out.format("Request:  '%s'\n", request);
        
        String response = null;
        String url = serverUrlText.getText();
        try {
//            long startTime = System.currentTimeMillis();
            HttpRequest httpRequest = httpClient.createPostRequest(url, encryptor.encrypt(request));
            try {
                HttpResponse httpResponse = httpRequest.execute();
//                long duration = System.currentTimeMillis() - startTime;
                if (httpResponse.getStatusCode() < 400) {
                    response = encryptor.decrypt(httpResponse.getBody());
//                    if (response != null && response.length() > 0) {
//                        System.out.format("Response: '%s'\n", response);
//                    }
//                    System.out.format("Request took %s ms\n", duration);
                } else {
                    appendText(String.format("\n>>> ERROR: HTTP status code: %d\n", httpResponse.getStatusCode()));
                    setTelnetConnected(false);
                    appendText("\n>>> Disconnected.\n");
                }
            } catch (IOException e) {
                setTelnetConnected(false);
                appendText(String.format("\n>>> ERROR: Could not connect to server: %s\n", e));
            }
        } catch (EncryptionException e) {
            appendText(String.format("\n>>> ERROR: Failed to encyrpt or decrypt HTTP request: %s\n", e));
        }
        
        return response;
    }

    private void close() {
        setPortalConnected(false);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.timer.TimerListener#timerElapsed()
     */
    @Override
    public void timerElapsed() {
        if (isTelnetConnected) {
            String response = executeRequest("RECEIVE");
            if (response != null && response.length() > 0) {
                appendText(response);
            }
        }
    }
    
}
