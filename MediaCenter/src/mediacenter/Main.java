package mediacenter;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class Main extends JFrame {
    
    
    private static final long serialVersionUID = -1L;
	
	
	public Main() {
	    createUI();
	}
	
	
	public static void main(String[] args) {
		new Main();
	}
	
	
    private void createUI() {
        setUndecorated(true);
        getContentPane().setBackground(Constants.BACKGROUND);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        
        JButton closeButton = new JButton("X");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTHEAST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        getContentPane().add(closeButton, gc);
        
        Button button = new Button(150, 50, "Video");
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                System.out.println(button + " clicked");
            }
        });
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        getContentPane().add(button, gc);
        
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }


	private void close() {
		dispose();
	}


}
