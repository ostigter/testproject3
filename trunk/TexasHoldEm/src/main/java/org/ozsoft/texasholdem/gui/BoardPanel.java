package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Board panel with the community cards and general information.
 *  
 * @author Oscar Stigter
 */
public class BoardPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;
	
	private static final Border LABEL_BORDER = new LineBorder(Color.BLACK, 1);
    
	private static final Border PANEL_BORDER =
    		new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10));

	/** Label with the pot. */
	private final JLabel potLabel;

	/** Labels with the community cards. */
    private final JLabel[] cardLabels;
    
    /** Label with a custom message. */
    private final JLabel messageLabel;
    
    /** The control panel. */
    private final ControlPanel controlPanel;
    
	/**
	 * Constructor.
	 * 
	 * @param mainFrame
	 *            The main frame.
	 */
    public BoardPanel(MainFrame mainFrame) {
        setBorder(PANEL_BORDER);
        setBackground(MainFrame.TABLE_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        // The pot.
        potLabel = new JLabel();
        potLabel.setBorder(LABEL_BORDER);
        potLabel.setForeground(Color.GREEN);
        potLabel.setHorizontalAlignment(JLabel.CENTER);
        gc.gridx = 2;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 0, 5, 0);
        add(potLabel, gc);

        // The five card positions.
        cardLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            cardLabels[i] = new JLabel(ResourceManager.getIcon("/images/card_placeholder.png"));
            gc.gridx = i;
            gc.gridy = 1;
            gc.gridwidth = 1;
            gc.gridheight = 1;
            gc.weightx = 0.0;
            gc.weighty = 0.0;
            gc.anchor = GridBagConstraints.CENTER;
            gc.fill = GridBagConstraints.NONE;
            gc.insets = new Insets(5, 5, 5, 5);
            add(cardLabels[i], gc);
        }
        
        // Message label.
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 5;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 0, 5, 0);
        add(messageLabel, gc);
        
        // Control panel.
        controlPanel = new ControlPanel(mainFrame);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 5;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.insets = new Insets(5, 0, 5, 0);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        add(controlPanel, gc);
        
        setPot(0);
        setMessage("");
    }
    
	/**
	 * Sets the pot.
	 * 
	 * @param pot
	 *            The pot.
	 */
    public void setPot(int pot) {
        potLabel.setText("$ " + pot);
    }
    
	/**
	 * Sets a custom message.
	 * 
	 * @param message
	 *            The message.
	 */
    public void setMessage(String message) {
        if (message.length() == 0) {
            messageLabel.setText(" ");
        } else {
            messageLabel.setText(message);
        }
    }
    
	/**
	 * Sets the allowed actions for the control panel.
	 * 
	 * @param actions
	 *            The allowed actions.
	 */
    public void setActions(int actions) {
        controlPanel.setActions(actions);
    }
    
	/**
	 * Returns the selected action from the control panel.
	 * 
	 * @return The selected action.
	 */
    public int getAction() {
    	return controlPanel.getAction();
    }
    
}
