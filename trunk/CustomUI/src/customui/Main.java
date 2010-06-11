package customui;

import java.awt.Font;

public class Main {

    private static final int WIDTH = 400;

    private static final int HEIGHT = 300;

    public static void main(String[] args) {
        Frame frame = new Frame("Test Application");
        frame.setSize(WIDTH, HEIGHT);

    	Panel panel = new Panel(3, 3);
        Font font = new Font("SansSerif", Font.PLAIN, 36);
        panel.addComponent(0, 0, new Label("Loop", font));
        panel.addComponent(1, 0, new Label("Fuij", font));
        panel.addComponent(2, 0, new Label("Hlaalu", font));
        panel.addComponent(0, 1, new Label("Weepy", font));
        panel.addComponent(1, 1, new Label("USA", font));
        panel.addComponent(2, 2, new Label("Bottom", font));
        frame.setPanel(panel);
        
        frame.center();
        frame.setVisible(true);
    }

}
