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
import javax.swing.border.LineBorder;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.actions.Action;



public class PlayerPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;

	private static final Color TABLE_COLOR = new Color(0, 128, 0);
    
    private static final Icon BUTTON_PRESENT_ICON =
    		ResourceManager.getIcon("/images/button_present.png");
    
    private static final Icon BUTTON_ABSENT_ICON =
            ResourceManager.getIcon("/images/button_absent.png");
    
    private static final Border LABEL_BORDER = new LineBorder(Color.BLACK, 1);
    
    private static final Border NORMAL_BORDER = new EmptyBorder(10, 10, 10, 10);
    
//    private static final Border SELECTED_BORDER =
//    		new CompoundBorder(new LineBorder(Color.YELLOW, 2), new EmptyBorder(10, 10, 10, 10));
    
    private static final GridBagConstraints gc = new GridBagConstraints();
    
    private Player player;
    private JLabel nameLabel;
    private JLabel cashLabel;
    private JLabel actionLabel;
    private JLabel betLabel;
    private JLabel card1Label;
    private JLabel card2Label;
    private JLabel dealerButton;
    
    public PlayerPanel(Player player) {
        super();
        
        this.player = player;
        
        setBorder(NORMAL_BORDER);
        setBackground(TABLE_COLOR);
        setLayout(new GridBagLayout());
        
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
    
    public void update() {
        int cash = player.getCash();
        if (cash == 0) {
            cashLabel.setText("Broke!");
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
            card1Label.setIcon(ResourceManager.getCardImage(cards[0]));
            card2Label.setIcon(ResourceManager.getCardImage(cards[1]));
        } else {
            card1Label.setIcon(ResourceManager.getIcon("/images/card_placeholder.png"));
            card2Label.setIcon(ResourceManager.getIcon("/images/card_placeholder.png"));
        }
    }
    
    public void setInTurn(boolean inTurn) {
        if (inTurn) {
            nameLabel.setForeground(Color.YELLOW);
        } else {
            nameLabel.setForeground(Color.GREEN);
        }
    }
    
    public void setDealer(boolean isDealer) {
        if (isDealer) {
            dealerButton.setIcon(BUTTON_PRESENT_ICON);
        } else {
            dealerButton.setIcon(BUTTON_ABSENT_ICON);
        }
    }
    
    private static class MyLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		public MyLabel() {
            setBorder(LABEL_BORDER);
            setForeground(Color.GREEN);
            setHorizontalAlignment(JLabel.HORIZONTAL);
        }
		
    } // MyLabel
    
}
