package org.ozsoft.httpclient;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 * Simple HTTP client application. <br />
 * <br />
 * 
 * Sends text messages to a HTTP server using the POST method. <br />
 * <br />
 * 
 * Implemented with the Apache commons-httpclient framework
 * (http://hc.apache.org/httpclient-3.x/).
 * 
 * @author nlost
 */
public class Main {
	
	private static final String TITLE = "HTTP Client";
	
	private static final String DEFAULT_URL = "http://localhost:8080/";
	
	private static final int APP_WIDTH = 800;  
	
	private static final int APP_HEIGHT = 800;  
	
	private static final Font CODE_FONT = new Font("Monospaced", Font.PLAIN, 12);
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private static final int STATUS_ERROR = 400;
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private HttpClient httpClient;
	
	private JFrame frame;
	
	private JTextField urlText;
	
	private JTextArea sendText;
	
	private JTextField countText;
	
	private JButton sendButton;
	
	private JTextArea logText;
	
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		createUI();
        httpClient = new HttpClient();
	}
	
	private void createUI() {
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel sendPanel = new JPanel(new GridBagLayout());
		sendPanel.setBorder(new TitledBorder("Send Message"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		frame.getContentPane().add(sendPanel, gbc);

		JLabel label = new JLabel("URL:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		sendPanel.add(label, gbc);

		urlText = new JTextField(String.valueOf(DEFAULT_URL));
        urlText.setFont(CODE_FONT);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		sendPanel.add(urlText, gbc);
		
        sendText = new JTextArea();
        sendText.setFont(CODE_FONT);
        sendText.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(sendText);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        sendPanel.add(scrollPane, gbc);
        
        label = new JLabel("Message count:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        sendPanel.add(label, gbc);

        countText = new JTextField("1");
        countText.setHorizontalAlignment(JTextField.RIGHT);
        countText.setPreferredSize(new Dimension(50, 25));
        countText.setFont(CODE_FONT);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        sendPanel.add(countText, gbc);
        
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		sendPanel.add(sendButton, gbc);
		
		JPanel logPanel = new JPanel(new GridBagLayout());
		logPanel.setBorder(new TitledBorder("Message Log"));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		frame.getContentPane().add(logPanel, gbc);

		logText = new JTextArea();
		logText.setEditable(false);
		logText.setFont(CODE_FONT);
		logText.setLineWrap(false);
		scrollPane = new JScrollPane(logText);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		logPanel.add(scrollPane, gbc);
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logText.setText("");
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		logPanel.add(clearButton, gbc);

		frame.setSize(APP_WIDTH, APP_HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		urlText.requestFocus();
	}
	
	private void sendMessage() {
	    final String url = urlText.getText().trim();
	    if (url.length() == 0) {
	        JOptionPane.showMessageDialog(null, "Please fill in the URL.", TITLE, JOptionPane.ERROR_MESSAGE);
	    } else {
	        int count = -1;
	        try {
	            count = Integer.parseInt(countText.getText());
	        } catch (NumberFormatException e) {
	            // Handled later, ignore for now.
	        }
	        if (count < 1) {
	            JOptionPane.showMessageDialog(null, "Invalid message count; must be 1 or greater.", TITLE, JOptionPane.ERROR_MESSAGE);
	            countText.setText("1");
	        } else {
                sendButton.setEnabled(false);
	            // Do actual send on separate thread to avoid GUI freeze.
	            final String message = sendText.getText();
	            final int count2 = count;
	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    sendMessage(url, message, count2);
                        sendButton.setEnabled(true);
	                }
	            });
	        }
	    }
	}
	
	private void sendMessage(String url, String message, int count) {
        log(String.format("Sending %d message(s) to URL '%s' with request body:\n%s", count, url, message));
        PostMethod postMethod = new PostMethod(url);
        try {
            RequestEntity requestEntity = new StringRequestEntity(message, "text/plain", "UTF-8");
            postMethod.setRequestEntity(requestEntity);
            for (int i = 1; i <= count; i++) {
                log(String.format("Sending message %d...", i));
                int statusCode = httpClient.executeMethod(postMethod);
                log("Message sent; reading response...");
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(postMethod.getResponseBodyAsStream(), postMethod.getResponseCharSet()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append(NEWLINE);
                }
                String response = sb.toString().trim();
                if (statusCode >= STATUS_ERROR) {
                    log("*** Received an error status from server!");
                }
                log("Response status code: " + statusCode);
                log("Response body:\n" + response);
            }
        } catch (Exception e) {
            log("*** ERROR: Could not send message: " + e.getMessage());
        } finally {
            postMethod.releaseConnection();
        }
	}
	
	private void log(String message) {
	    // Write message with timestamp to log.
		String timestamp = DATE_FORMAT.format(new Date());
		logText.append(String.format("%s %s", timestamp, message));
		logText.append("\n");
		
		// Force JTextArea to scroll down to last line.
		logText.setCaretPosition(logText.getText().length());
	}
	
}
