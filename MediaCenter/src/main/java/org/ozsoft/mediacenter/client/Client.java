package org.ozsoft.mediacenter.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.ozsoft.genie.GenieClient;
import org.ozsoft.genie.GenieException;
import org.ozsoft.mediacenter.domain.Episode;
import org.ozsoft.mediacenter.domain.Show;
import org.ozsoft.mediacenter.shared.Constants;

/**
 * The media client with a Swing UI.
 * 
 * @author Oscar Stigter
 */
public class Client {
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(Client.class);
	
	/** The confguration. */
	private final Configuration config;
	
	/** The remote service. */
	private GenieClient service;
	
	/** The TV shows. */
	private Show[] shows;
	
	// UI components.
	private JFrame frame;
	private JList showList;
	private JList episodeList;
	private DefaultListModel showListModel;
	private DefaultListModel episodeListModel;
	private JButton playButton;
	private JButton refreshButton;
	private JButton deleteButton;
	
    /**
     * Program's main entry point.
     * 
     * @param args  Command line arguments.
     */
    public static void main(String[] args) {
    	new Client();
    }
    
    /**
     * Constructor.
     */
    public Client() {
    	config = new Configuration();
    	createUI();
    	LOG.info("Started");
    	connect();
    	refresh();
    }
    
    private void createUI() {
		frame = new JFrame(Constants.TITLE);
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()  {
			public void windowClosed(WindowEvent e) {
				close();
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();

		JLabel label = new JLabel(Constants.TITLE);
		label.setFont(Constants.FONT);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(20, 20, 10, 20);
		frame.getContentPane().add(label, gbc);

		showListModel = new DefaultListModel();
		episodeListModel = new DefaultListModel();
		
		showList = new JList(showListModel);
		showList.setFont(Constants.FONT);
		showList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectShow();
			}
		});
		JScrollPane scrollPane = new JScrollPane(showList);
		scrollPane.getVerticalScrollBar().setPreferredSize(
				new Dimension(50, 600));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.4;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 20, 5, 20);
		frame.getContentPane().add(scrollPane, gbc);

		episodeList = new JList(episodeListModel);
		episodeList.setFont(Constants.FONT);
		episodeList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					playEpisode();
				} else {
					selectEpisode();
				}
			}
		});
		scrollPane = new JScrollPane(episodeList);
		scrollPane.getVerticalScrollBar().setPreferredSize(
				new Dimension(50, 600));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.6;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 20, 5, 20);
		frame.getContentPane().add(scrollPane, gbc);

		playButton = new JButton("Play");
		playButton.setFont(Constants.FONT);
		playButton.setPreferredSize(Constants.BUTTON_SIZE);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playEpisode();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 20, 20, 10);
		frame.getContentPane().add(playButton, gbc);

		refreshButton = new JButton("Refresh");
		refreshButton.setFont(Constants.FONT);
		refreshButton.setPreferredSize(Constants.BUTTON_SIZE);
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 20, 20, 10);
		frame.getContentPane().add(refreshButton, gbc);

		deleteButton = new JButton("Delete");
		deleteButton.setFont(Constants.FONT);
		deleteButton.setPreferredSize(Constants.BUTTON_SIZE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteEpisode();
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5, 10, 20, 20);
		frame.getContentPane().add(deleteButton, gbc);
		
		enableButtons(false);

		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
	
    private void connect() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		    	try {
			    	LOG.debug("Connecting to server");
			    	String host = config.getServerHost();
			    	int port = config.getServerPort();
					service = new GenieClient(host, port, Constants.SERVICE_ID);
				} catch (GenieException e) {
					String msg = "Could not connect to server";
					LOG.error(msg, e);
					JOptionPane.showMessageDialog(
							frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
					close();
		    	}
            }
        });
    }

	private void refresh() {
    	LOG.debug("Refreshing");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		showListModel.removeAllElements();
        		episodeListModel.removeAllElements();
            	try {
	    			service.call("refresh");
	    			shows = (Show[]) service.call("getShows");
	    			for (Show show : shows) {
	    				showListModel.addElement(show);
	    			}
        		} catch (GenieException e) {
        			String msg = "Could not retrieve TV shows from server";
        			LOG.error(msg, e);
        			JOptionPane.showMessageDialog(
        					frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
        			close();
            	}
            }
        });
	}

	private void selectShow() {
		int index = showList.getSelectedIndex();
		Show show = (Show) showListModel.get(index);
		episodeListModel.clear();
		for (Episode episode : show.getEpisodes()) {
			episodeListModel.addElement(episode);
		}
		enableButtons(false);
	}

	private void selectEpisode() {
		enableButtons(true);
	}

	private void playEpisode() {
		int index = episodeList.getSelectedIndex();
		Episode episode = (Episode) episodeListModel.get(index);
		String host = config.getServerHost();
		if (host.equals("localhost")) {
		    host = "127.0.0.1";
		}
//		String filePath = String.format("//%s/%s", host, episode.getFile().getPath()); 
        String filePath = episode.getFile().getPath(); 
		LOG.debug(String.format("Playing video file '%s'", filePath));
		String playerPath = config.getVideoPlayerPath();
		ProcessBuilder pb =
				new ProcessBuilder(playerPath, "/fullscreen", filePath);
		Process process = null;
		try {
			process = pb.start();
			long time = System.currentTimeMillis();
			int exitValue = process.waitFor();
			if (exitValue != 0) {
				String msg = String.format(
						"Error playing video file (exit code %d)", exitValue);
				LOG.error(msg);
				JOptionPane.showMessageDialog(
						frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}
			long duration = (System.currentTimeMillis() - time) / 1000;
			if (duration >= Constants.SEEN_DURATION) {
				episode.markAsSeen();
			}
		} catch (Exception e) {
			String msg = "Could not start video player: " + e.getMessage();
			LOG.error(msg, e);
			JOptionPane.showMessageDialog(
					frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deleteEpisode() {
//		int index = episodeList.getSelectedIndex();
//		Episode episode = (Episode) episodeListModel.get(index);
//		Show show = shows.get(episode.getShowName());
//		File file = episode.getFile();
//		if (file.delete()) {
//			episodeListModel.remove(index);
//			show.removeEpisode(episode);
//			enableButtons(false);
//		}
	}
	
	private void enableButtons(boolean isEnabled) {
		playButton.setEnabled(isEnabled);
		deleteButton.setEnabled(isEnabled);
	}

	private void close() {
		System.exit(0);
	}

}
