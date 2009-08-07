package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;
	
	public static final int CHOICE_CONTINUE         = 0;
    public static final int CHOICE_CHECK_BET_FOLD   = 1;
    public static final int CHOICE_CALL_RAISE_FOLD  = 2;
    
    public static final int ACTION_CONTINUE         = 0;
    public static final int ACTION_CHECK            = 1;
    public static final int ACTION_CALL             = 2;
    public static final int ACTION_BET              = 3;
    public static final int ACTION_RAISE            = 4;
    public static final int ACTION_FOLD             = 5;
    
    private static final JButton continueButton = new JButton("Continue");
    private static final JButton checkButton    = new JButton("Check");
    private static final JButton callButton     = new JButton("Call");
    private static final JButton betButton      = new JButton("Bet");
    private static final JButton raiseButton    = new JButton("Raise");
    private static final JButton foldButton     = new JButton("Fold");
    
//    private static final GridBagConstraints gc = new GridBagConstraints();
    
    private final MainFrame mainFrame;
    
    public ControlPanel(MainFrame mainFrame) {
    	super();
        this.mainFrame = mainFrame;
        setBackground(new Color(0, 128, 0));
        setLayout(new GridBagLayout());
        continueButton.setMnemonic('c');
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                continueClicked();
            }
        });
        checkButton.setMnemonic('c');
        callButton.setMnemonic('c');
        betButton.setMnemonic('b');
        raiseButton.setMnemonic('r');
        foldButton.setMnemonic('f');
    }
    
    public void setChoices(int choice) {
        removeAll();
        switch (choice) {
            case CHOICE_CONTINUE:
                add(continueButton);
                break;
            case CHOICE_CHECK_BET_FOLD:
                add(checkButton);
                add(betButton);
                add(foldButton);
                break;
            case CHOICE_CALL_RAISE_FOLD:
                add(callButton);
                add(raiseButton);
                add(foldButton);
                break;
            default:
                System.err.println("ERROR: Invalid button choice: " + choice);
        }
        activateButtons();
    }
    
    private void activateButtons() {
        for (Component component : getComponents()) {
            component.setEnabled(true);
        }
    }
    
//    private void deactivateButtons() {
//        for (Component component : getComponents()) {
//            component.setEnabled(false);
//        }
//    }
    
    private void continueClicked() {
        mainFrame.playerActed();
    }
    
}
