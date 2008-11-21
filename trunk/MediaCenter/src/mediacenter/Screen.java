package mediacenter;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;


public abstract class Screen extends JPanel {


    private static final long serialVersionUID = 1L;
    
    protected final Application application;
    
    protected final GridBagConstraints gc = new GridBagConstraints();
    
    
    public Screen(Application application) {
        this.application = application;
        
        setBackground(Constants.BACKGROUND);
        setLayout(new GridBagLayout());
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Right mouse button for exiting the current screen. 
                    Screen.this.application.back();
                }
            }
        });
        
        createUI();
    }
    
    
    protected abstract void createUI();


}
