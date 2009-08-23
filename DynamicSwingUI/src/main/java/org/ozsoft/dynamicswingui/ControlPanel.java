package org.ozsoft.dynamicswingui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ControlPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private final JButton button1;
	private final JButton button2;
	
	public ControlPanel() {
		setBorder(new TitledBorder("Control panel"));
		setLayout(new FlowLayout());
		button1 = new JButton("One");
		button1.addActionListener(this);
		button2 = new JButton("Two");
		button2.addActionListener(this);
	}
	
	public void showButtons(int count) {
		removeAll();
		if (count > 0) {
			add(button1);
		}
		if (count > 1) {
			add(button2);
		}
		// ---------------------------------------------
		// Both validate() and repaint() must be called!
		// ---------------------------------------------
		validate();
		repaint();
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == button1) {
			System.out.println("Button One clicked");
		} else if (source == button2) {
			System.out.println("Button Two clicked");
		} else {
			// Do nothing.
		}
	}
	
}
