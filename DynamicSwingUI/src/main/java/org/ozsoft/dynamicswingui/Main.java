package org.ozsoft.dynamicswingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private final JButton cleanButton;
	private final JButton show1Button;
	private final JButton show2Button;
	private final ControlPanel controlPanel;
	
	public Main() {
		super("Swing UI test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());

		controlPanel = new ControlPanel();
		
		GridBagConstraints gc = new GridBagConstraints();
		
		cleanButton = new JButton("Clean");
		cleanButton.addActionListener(this);
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.weightx = 1.0;
		gc.weighty = 1.0;
		gc.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(cleanButton, gc);
		
		show1Button = new JButton("Show 1");
		show1Button.addActionListener(this);
		gc.gridx = 1;
		gc.gridy = 0;
		getContentPane().add(show1Button, gc);
		
		show2Button = new JButton("Show 2");
		show2Button.addActionListener(this);
		gc.gridx = 2;
		gc.gridy = 0;
		getContentPane().add(show2Button, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 3;
		gc.gridheight = 1;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1.0;
		gc.weighty = 1.0;
		gc.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(controlPanel, gc);
		
		setSize(400, 200);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == cleanButton) {
			showButtons(0);
		} else if (source == show1Button) {
			showButtons(1);
		} else if (source == show2Button) {
			showButtons(2);
		} else {
			// Do nothing.
		}
	}

	private void showButtons(final int count) {
		// --------------------------------------------------------------
		// Always run dynamic UI code on Swing's Event Dispatcher Thread! 
		// --------------------------------------------------------------
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				controlPanel.showButtons(count);
			}
		});
	}

}
