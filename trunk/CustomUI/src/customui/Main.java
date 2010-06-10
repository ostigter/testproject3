package customui;

import java.awt.Font;

public class Main {

    private static final int WIDTH = 400;

    private static final int HEIGHT = 300;

    public static void main(String[] args) {
        Frame frame = new Frame("Test Application");
        frame.setSize(WIDTH, HEIGHT);

    	Panel panel = new Panel(2, 1);
        Font font = new Font("Arial", Font.PLAIN, 12);
        Label label1 = new Label("Label 1", font);
        Label label2 = new Label("Label 2", font);
        panel.addComponent(0, 0, label1);
        panel.addComponent(1, 0, label2);
        frame.setPanel(panel);
        
        frame.center();
        frame.setVisible(true);
    }

}
