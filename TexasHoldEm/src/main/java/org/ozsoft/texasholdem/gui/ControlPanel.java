package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener {
    
	private static final long serialVersionUID = 1L;
	
	public static final int NONE             = 0;
	public static final int CONTINUE         = 1;
	public static final int CHECK            = 2;
	public static final int CALL             = 3;
	public static final int BET              = 4;
	public static final int RAISE            = 5;
	public static final int FOLD             = 6;
	public static final int CHECK_BET_FOLD   = 7;
	public static final int CALL_RAISE_FOLD  = 8;
	public static final int CHECK_RAISE_FOLD = 9;
    
    private final MainFrame mainFrame;
    private final JButton continueButton;
    private final JButton checkButton;
    private final JButton callButton;
    private final JButton betButton;
    private final JButton raiseButton;
    private final JButton foldButton;
    
    private int action;
    
    public ControlPanel(MainFrame mainFrame) {
    	super();
        this.mainFrame = mainFrame;
        setBackground(new Color(0, 128, 0));
        setLayout(new FlowLayout());
        continueButton = createButton("Continue", 'c');
        checkButton = createButton("Check", 'c');
        callButton = createButton("Call", 'c');
        betButton = createButton("Bet", 'b');
        raiseButton = createButton("Raise", 'r');
        foldButton = createButton("Fold", 'f');
    }
    
    private JButton createButton(String label, char mnemonic) {
    	JButton button = new JButton(label);
    	button.setMnemonic(mnemonic);
    	button.setSize(100, 30);
    	button.addActionListener(this);
    	return button;
    }
    
    public void setChoices(int choice) {
        removeAll();
        switch (choice) {
        	case NONE:
        		// No buttons.
        		break;
            case CONTINUE:
                add(continueButton);
                break;
            case CHECK_BET_FOLD:
                add(checkButton);
                add(betButton);
                add(foldButton);
                break;
            case CALL_RAISE_FOLD:
                add(callButton);
                add(raiseButton);
                add(foldButton);
                break;
            case CHECK_RAISE_FOLD:
                add(checkButton);
                add(raiseButton);
                add(foldButton);
                break;
            default:
            	// Should never happen.
                System.err.println("ERROR: Invalid button choice: " + choice);
        }
//        activateButtons();
    }
    
//    private void activateButtons() {
//        for (Component component : getComponents()) {
//            component.setEnabled(true);
//        }
//    }
    
//    private void deactivateButtons() {
//        for (Component component : getComponents()) {
//            component.setEnabled(false);
//        }
//    }
    
    public int getAction() {
    	return action;
    }
    
    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
	public void actionPerformed(ActionEvent e) {
    	Object source = e.getSource();
    	if (source == continueButton) {
    		action = CONTINUE;
    	} else if (source == checkButton) {
    		action = CHECK;
    	} else if (source == callButton) {
    		action = CALL;
    	} else if (source == betButton) {
    		action = BET;
    	} else if (source == raiseButton) {
    		action = RAISE;
    	} else {
    		action = FOLD;
    	}
    	mainFrame.playerActed();
	}
    
}
