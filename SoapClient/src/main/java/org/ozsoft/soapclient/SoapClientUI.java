package org.ozsoft.soapclient;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class SoapClientUI {
	
	private static final File HISTORY_FILE = new File("history.dat");
	
	private static final int MAX_HISTORY_ENTRIES = 5;
	
	private static final Font CODE_FONT = new Font("Monospaced", Font.PLAIN, 14);
	
	private JFrame frame;
	
	private DefaultComboBoxModel endpoints;
	
	private DefaultComboBoxModel requests;
	
	private JTextArea requestText;

	private JTextArea responseText;

	public static void main(String[] args) {
		new SoapClientUI();
	}

	public SoapClientUI() {
		createUI();
		loadHistory();
	}

	private void createUI() {
		frame = new JFrame("SOAP Message Sender");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				saveHistory();
			}
		});
		frame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JPanel endpointPanel = new JPanel(new GridBagLayout());
		endpointPanel.setBorder(new TitledBorder("Endpoint"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		frame.getContentPane().add(endpointPanel, gbc);

		endpoints = new DefaultComboBoxModel();

		JComboBox endpointComboBox = new JComboBox(endpoints);
		endpointComboBox.setEditable(true);
		endpointComboBox.setFont(CODE_FONT);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		endpointPanel.add(endpointComboBox, gbc);

		JPanel requestPanel = new JPanel(new GridBagLayout());
		requestPanel.setBorder(new TitledBorder("Request"));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		frame.getContentPane().add(requestPanel, gbc);

		requests = new DefaultComboBoxModel();

		final JComboBox requestComboBox = new JComboBox(requests);
		requestComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = (String) requestComboBox.getSelectedItem();
				if (path != null) {
					setRequestFile(path);
				}
			}
		});
		requestComboBox.setFont(CODE_FONT);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		requestPanel.add(requestComboBox, gbc);

		JButton browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browseRequestFile();
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
		requestPanel.add(browseButton, gbc);

		requestText = new JTextArea();
		requestText.setFont(CODE_FONT);
		JScrollPane scrollPane = new JScrollPane(requestText);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		requestPanel.add(scrollPane, gbc);

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		frame.getContentPane().add(sendButton, gbc);
		
		JPanel responsePanel = new JPanel(new GridBagLayout());
		responsePanel.setBorder(new TitledBorder("Response"));
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		frame.getContentPane().add(responsePanel, gbc);

		responseText = new JTextArea();
		responseText.setEditable(false);
		responseText.setFont(CODE_FONT);
		scrollPane = new JScrollPane(responseText);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		responsePanel.add(scrollPane, gbc);

		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void loadHistory() {
		if (HISTORY_FILE.isFile()) {
			endpoints.removeAllElements();
			requests.removeAllElements();
			try {
				DataInputStream dis = new DataInputStream(new FileInputStream(HISTORY_FILE));
				// Read endpoints.
				int count = dis.readInt();
				for (int i = 0; i < count; i++) {
					String endpoint = dis.readUTF();
					endpoints.addElement(endpoint);
				}
				// Read request files.
				count = dis.readInt();
				for (int i = 0; i < count; i++) {
					String path = dis.readUTF();
					requests.addElement(path);
				}
				dis.close();
			} catch (IOException e) {
				System.err.println("ERROR: Could not read history file: " + e);
			}
		}
	}

	private void saveHistory() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(HISTORY_FILE));
			// Save endpoints.
			int count = endpoints.getSize();
			dos.writeInt(count);
			for (int i = 0; i < count; i++) {
				String endpoint = (String) endpoints.getElementAt(i);
				dos.writeUTF(endpoint);
			}
			// Save request files.
			count = requests.getSize();
			dos.writeInt(count);
			for (int i = 0; i < count; i++) {
				String path = (String) requests.getElementAt(i);
				dos.writeUTF(path);
			}
			dos.close();
		} catch (IOException e) {
			System.err.println("ERROR: Could not write history file: " + e);
		}
	}
	
	private void browseRequestFile() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            setRequestFile(path);
		}
	}
	
	private String getTextFromFile(String path) {
		String text = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			br.close();
			text = sb.toString();
		} catch (IOException e) {
			System.err.println("ERROR: Could not read file: " + path);
		}
		return text;
	}
	
	private void setRequestFile(String path) {
        String text = getTextFromFile(path);
        if (text != null) {
        	// Add request file to history.
            requests.removeElement(path);
            requests.insertElementAt(path, 0);
			if (requests.getSize() > MAX_HISTORY_ENTRIES) {
				requests.removeElementAt(MAX_HISTORY_ENTRIES);
			}
            requests.setSelectedItem(path);
            requestText.setText(text);
        }
	}
	
	private void sendMessage() {
		String endpoint = (String) endpoints.getSelectedItem();
		if (endpoint == null || endpoint.length() == 0) {
			JOptionPane.showMessageDialog(frame, "Please enter a web service endpoint URL.");
		} else {
			// Add endpoint URL to history.
			endpoints.removeElement(endpoint);
			endpoints.insertElementAt(endpoint, 0);
			if (endpoints.getSize() > MAX_HISTORY_ENTRIES) {
				endpoints.removeElementAt(MAX_HISTORY_ENTRIES);
			}
			endpoints.setSelectedItem(endpoint);
		}
	}
	
}
