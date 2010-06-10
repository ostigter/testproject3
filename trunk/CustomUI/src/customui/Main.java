package customui;

import java.awt.Font;

public class Main {

    private static final int WIDTH = 400;

    private static final int HEIGHT = 300;

    public static void main(String[] args) {
        Frame frame = new Frame("Test Application");
        frame.setSize(WIDTH, HEIGHT);

    	Panel panel = new Panel(2, 2);
        Font font = new Font("SansSerif", Font.PLAIN, 12);
        panel.addComponent(0, 0, new Label("Label 1", font));
        panel.addComponent(1, 0, new Label("Label 2", font));
        panel.addComponent(0, 1, new Label("Label 3", font));
        panel.addComponent(1, 1, new Label("Label 4", font));
        frame.setPanel(panel);
        
        frame.center();
        frame.setVisible(true);
    }

}
