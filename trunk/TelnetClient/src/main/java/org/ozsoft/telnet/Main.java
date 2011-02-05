package org.ozsoft.telnet;

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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * Telnet client application with a Swing GUI.
 * 
 * @author Oscar Stigter
 */
public class Main implements TelnetListener {

    private static final String TITLE = "Telnet client";

    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 23;

    private static final int APP_WIDTH = 800;

    private static final int APP_HEIGHT = 600;

    private static final String NEWLINE = "\r\n";

    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    private boolean isConnected = false;
    
    private final TelnetClient telnetClient;

    private JFrame frame;

    private JTextField hostText;

    private JTextField portText;

    private JButton connectDisconnectButton;

    private JTextArea consoleText;

    private JTextField sendText;

    public static void main(String[] args) {
        new Main();
    }
    
    public Main() {
        telnetClient = new TelnetClient();
        telnetClient.addTelnetListener(this);
        createUI();
        setConnected(false);
    }

    @Override
    public void connected() {
        appendText("### Connected ###\n");
        setConnected(true);
    }

    @Override
    public void disconnected() {
        appendText("\n### Disconnected ###\n");
        setConnected(false);
    }

    @Override
    public void textReceived(String text) {
        appendText(text);
    }

    @Override
    public void ansiCodeReceived(String code) {
//        System.out.format("*** ANSI code: '%s'\n", code);
    }
    
    @Override
    public void telnetExceptionCaught(Throwable t) {
        appendText(String.format("### Error occurred: '%s' ###\n", t.getMessage()));
    }

    private void createUI() {
        frame = new JFrame(TITLE);
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
        serverPanel.setBorder(new TitledBorder("Server"));
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

        JLabel label = new JLabel("Host:");
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

        hostText = new JTextField(String.valueOf(DEFAULT_HOST));
        hostText.setPreferredSize(new Dimension(200, 25));
        hostText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                hostText.selectAll();
            }
        });
        hostText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectDisconnect();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        serverPanel.add(hostText, gbc);

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
        serverPanel.add(label, gbc);

        portText = new JTextField(String.valueOf(DEFAULT_PORT));
        portText.setPreferredSize(new Dimension(75, 25));
        portText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                portText.selectAll();
            }
        });
        portText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectDisconnect();
                }
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
        serverPanel.add(portText, gbc);

        connectDisconnectButton = new JButton("Connect");
        connectDisconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectDisconnect();
            }
        });
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        serverPanel.add(connectDisconnectButton, gbc);

        JPanel consolePanel = new JPanel(new GridBagLayout());
        consolePanel.setBorder(new TitledBorder("Console"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(consolePanel, gbc);

        consoleText = new JTextArea();
        consoleText.setEditable(false);
        consoleText.setFont(FONT);
        consoleText.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(consoleText);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        consolePanel.add(scrollPane, gbc);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new TitledBorder("Input"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(inputPanel, gbc);

        sendText = new JTextField();
        sendText.setFont(FONT);
        sendText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(sendText, gbc);

        frame.setSize(APP_WIDTH, APP_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
        hostText.setEditable(!isConnected);
        portText.setEditable(!isConnected);
        sendText.setEditable(isConnected);
        if (isConnected) {
            consoleText.setText("");
            connectDisconnectButton.setText("Disconnect");
            sendText.setText("");
            sendText.requestFocusInWindow();
        } else {
            sendText.setText("");
            connectDisconnectButton.setText("Connect");
            hostText.selectAll();
            hostText.requestFocusInWindow();
        }
    }

    private void connectDisconnect() {
        if (!isConnected) {
            String host = hostText.getText();
            if (host.isEmpty()) {
                // TODO Empty host
                return;
            }

            String portString = portText.getText();
            if (portString.isEmpty()) {
                // TODO Empty port
                return;
            }

            try {
                int port = Integer.parseInt(portString);
                if (port >= 0 && port <= 65535) {
                    telnetClient.connect(host, port);
                } else {
                    // TODO Invalid port
                    return;
                }
            } catch (NumberFormatException e) {
                // TODO Invalid port
            }
        } else {
            disconnect();
        }
    }

    private void disconnect() {
        telnetClient.disconnect();
        setConnected(false);
    }

    private void send() {
        String line = sendText.getText() + NEWLINE;
        telnetClient.sendText(line);
        appendText(line);
        sendText.selectAll();
    }

    private void appendText(String line) {
        consoleText.append(line);
        consoleText.setCaretPosition(consoleText.getText().length());
    }

    private void close() {
        disconnect();
    }

}
