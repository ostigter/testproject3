package mediacenter;


import java.awt.GridBagConstraints;
import java.awt.Insets;


public class MoviesScreen extends Screen {
    
    
    private static final long serialVersionUID = 1L;
    
    
    public MoviesScreen(Application application) {
        super(application);
    }
    
    
    protected void createUI() {
        Button button = new Button("_", 50, 50);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.minimize();
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
        add(button, gc);
        
        button = new Button("X", 50, 50);
        button.addButtonListener(new ButtonListener() {
            public void buttonClicked(Button button) {
                application.exit();
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
        add(button, gc);
        
        Label label = new Label("Media Center", 460, 125);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 10, 10);
        add(label, gc);
        
        label = new Label("Movies", 150, 75);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 10, 10);
        add(label, gc);

        label = new Label("(Not implemented yet)", 450, 75);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        add(label, gc);
    }

}
