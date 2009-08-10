package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.actions.Action;

/**
 * Panel representing a player at the table.
 * 
 * @author Oscar Stigter
 */
public class PlayerPanel extends JPanel {
    
	/** The serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Filled dealer button image when player is dealer. */
	private static final Icon BUTTON_PRESENT_ICON =
    		ResourceManager.getIcon("/images/button_present.png");
    
    /** Empty dealer button image when player is not dealer. */
	private static final Icon BUTTON_ABSENT_ICON =
            ResourceManager.getIcon("/images/button_absent.png");
	
	private static final Icon CARD_PLACEHOLDER_ICON =
		ResourceManager.getIcon("/images/card_placeholder.png");

	private static final Icon CARD_BACK_ICON =
			ResourceManager.getIcon("/images/card_back.png");
    
	/** The border. */
	private static final Border BORDER = new EmptyBorder(10, 10, 10, 10);
    
    /** The player. */
    private Player player;
    
    /** The label with the player's name. */
    private JLabel nameLabel;
    
    /** The label with the player's amount of cash. */
    private JLabel cashLabel;
    
    /** The label with the last action performed. */
    private JLabel actionLabel;
    
    /** The label with the player's current bet. */
    private JLabel betLabel;

    /** The label for the first hole card. */
    private JLabel card1Label;

    /** The label for the second hole card. */
    private JLabel card2Label;

    /** The label for the dealer button image. */
    private JLabel dealerButton;
    
	/**
	 * Constructor.
	 * 
	 * @param player
	 *            The player.
	 */
    public PlayerPanel(Player player) {
        this.player = player;
        
        setBorder(BORDER);
        setBackground(UIConstants.TABLE_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        nameLabel = new MyLabel();
        nameLabel.setText(player.getName());
        cashLabel = new MyLabel();
        actionLabel = new MyLabel();
        betLabel = new MyLabel();
        card1Label = new JLabel();
        card2Label = new JLabel();
        dealerButton = new JLabel();
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(dealerButton, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.insets = new Insets(1, 1, 1, 1);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        add(nameLabel, gc);
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(cashLabel, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(actionLabel, gc);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(betLabel, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(card1Label, gc);
        gc.gridx = 1;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        add(card2Label, gc);

        update();
        
        setInTurn(false);
        setDealer(false);
    }
    
    /**
     * Updates the panel with the player's status.
     */
    public void update() {
        int cash = player.getCash();
        if (cash == 0) {
            cashLabel.setText("BROKE!");
        } else {
            cashLabel.setText("$ " + player.getCash());
        }
        int bet = player.getBet();
        if (bet == 0) {
            betLabel.setText(" ");
        } else {
            betLabel.setText("$ " + player.getBet());
        }
        Action action = player.getAction();
        if (action != null) {
            actionLabel.setText(action.toString());
        } else {
            actionLabel.setText(" ");
        }
        
        Card[] cards = player.getCards();
        if (cards.length == 2) {
        	if (player instanceof HumanPlayer) {
	            card1Label.setIcon(ResourceManager.getCardImage(cards[0]));
	            card2Label.setIcon(ResourceManager.getCardImage(cards[1]));
        	} else {
	            card1Label.setIcon(CARD_BACK_ICON);
	            card2Label.setIcon(CARD_BACK_ICON);
        		
        	}
        } else {
            card1Label.setIcon(CARD_PLACEHOLDER_ICON);
            card2Label.setIcon(CARD_PLACEHOLDER_ICON);
        }
    }
    
	/**
	 * Sets whether the player is the dealer.
	 * 
	 * @param isDealer
	 *            True if the dealer, otherwise false.
	 */
    public void setDealer(boolean isDealer) {
        if (isDealer) {
            dealerButton.setIcon(BUTTON_PRESENT_ICON);
        } else {
            dealerButton.setIcon(BUTTON_ABSENT_ICON);
        }
    }
    
	/**
	 * Sets whether it's this player's turn to act.
	 * 
	 * @param inTurn
	 *            True if it's the player's turn, otherwise false.
	 */
    public void setInTurn(boolean inTurn) {
        if (inTurn) {
            nameLabel.setForeground(Color.YELLOW);
        } else {
            nameLabel.setForeground(Color.GREEN);
        }
    }
    
    /**
     * Custom label for a player panel.
     * 
     * @author Oscar Stigter
     */
    private static class MyLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public MyLabel() {
            setBorder(UIConstants.LABEL_BORDER);
            setForeground(UIConstants.TEXT_COLOR);
            setHorizontalAlignment(JLabel.HORIZONTAL);
        }
		
    } // MyLabel
    
}
