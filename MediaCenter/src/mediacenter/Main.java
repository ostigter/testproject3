package mediacenter;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;


public class Main extends JFrame {
    
    
    private static final long serialVersionUID = -1L;
	
	
	public Main() {
	    super("Media Center");
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
        
        Label titleLabel = new Label("Media Center", 500, 125);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(titleLabel, gc);
        
        Button button = new Button("_", 50, 50);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                minimize();
            }
        });
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTHEAST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 0, 0, 0);
        getContentPane().add(button, gc);
        
        button = new Button("X", 50, 50);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                exit();
            }
        });
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 0, 0, 10);
        getContentPane().add(button, gc);
        
        button = new Button("Video", 300, 75);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
            }
        });
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.SOUTH;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        getContentPane().add(button, gc);
        
        button = new Button("Music", 300, 75);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
            }
        });
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        getContentPane().add(button, gc);
        
        button = new Button("Pictures", 300, 75);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
            }
        });
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        getContentPane().add(button, gc);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setSize(600, 400);
//        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void minimize() {
        setExtendedState(getExtendedState() | JFrame.ICONIFIED);
    }


	private void exit() {
		dispose();
	}


}
