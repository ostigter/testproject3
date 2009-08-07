package cards.poker.texasholdem.gui;

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

public class BoardPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;

	private static final Border LABEL_BORDER = new LineBorder(Color.BLACK, 1);
    
    private static final Border PANEL_BORDER =
    		new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(10, 10, 10, 10));
    
    private final JLabel potLabel;
    
    private final JLabel[] cardLabels;
    
    private final JLabel messageLabel;
    
    private final ControlPanel controlPanel;
    
    public BoardPanel(MainFrame mainFrame) {
        super();
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
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.insets = new Insets(10, 0, 5, 0);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(potLabel, gc);

        // The five card positions.
        cardLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            cardLabels[i] = new JLabel(ResourceManager.getIcon("/images/card_placeholder.png"));
            gc.gridx = i;
            gc.gridy = 1;
            gc.gridwidth = 1;
            gc.gridheight = 1;
            gc.weightx = 1.0;
            gc.weighty = 1.0;
            gc.insets = new Insets(5, 5, 5, 5);
            gc.anchor = GridBagConstraints.CENTER;
            gc.fill = GridBagConstraints.NONE;
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
        gc.weighty = 1.0;
        gc.insets = new Insets(10, 0, 5, 0);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
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
    
    public void setPot(int pot) {
        potLabel.setText("$ " + pot);
    }
    
    public void setMessage(String message) {
        if (message.length() == 0) {
            messageLabel.setText(" ");
        } else {
            messageLabel.setText(message);
        }
    }
    
    public void setChoices(int choices) {
        controlPanel.setChoices(choices);
    }
    
}
