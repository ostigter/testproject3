package mediacenter;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;


public class MainScreen extends Screen {
    
    
    private static final long serialVersionUID = 1L;
    
    
    public MainScreen(Application application) {
        super(application);
    }
    
    
    protected void createUI() {
        Label label = new Label("Media Center", 0.30, 0.10);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 10, 10);
        add(label, gc);
        
        Button button = new Button("_", 0.05, 0.05);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.minimize();
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
        gc.insets = new Insets(10, 0, 0, 0);
        add(button, gc);
        
        button = new Button("X", 0.05, 0.05);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.exit();
            }
        });
        gc.gridx = 2;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 0, 0, 10);
        add(button, gc);
        
        label = new Label("Main", 0.30, 0.10);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 10, 10);
        add(label, gc);
        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        
        button = new Button("TV Series", 0.20, 0.10);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.setScreen(Constants.TV_SERIES);
            }
        });
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        buttonPanel.add(button, gc);
        
        button = new Button("Movies", 0.20, 0.10);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.setScreen(Constants.MOVIES);
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
        buttonPanel.add(button, gc);
        
        button = new Button("Music", 0.20, 0.10);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.setScreen(Constants.MUSIC);
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
        buttonPanel.add(button, gc);
        
        button = new Button("Pictures", 0.20, 0.10);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.setScreen(Constants.PICTURES);
            }
        });
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        buttonPanel.add(button, gc);
        
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 3;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        add(buttonPanel, gc);
        
    }

}
