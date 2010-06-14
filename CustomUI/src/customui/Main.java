package customui;

import java.awt.Font;

public class Main {

    private static final int WIDTH = 600;

    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        Frame frame = new Frame("Test Application");
        frame.setSize(WIDTH, HEIGHT);

    	Panel seriesPanel = new Panel(1, 3);
        Font font = new Font("SansSerif", Font.PLAIN, 36);
        seriesPanel.addComponent(0, 0, new Label("Breaking Bad", font));
        seriesPanel.addComponent(0, 1, new Label("CSI: New York", font));
        seriesPanel.addComponent(0, 2, new Label("Dog Whisperer, the", font));
        
        Panel moviesPanel = new Panel(1, 3);
        moviesPanel.addComponent(0, 0, new Label("Ratatouille", font));
        moviesPanel.addComponent(0, 1, new Label("Up in the air", font));
        moviesPanel.addComponent(0, 2, new Label("Sleuth", font));
        
        TabPane tabPane = new TabPane();
        tabPane.addTab("Series", seriesPanel);
        tabPane.addTab("Movies", moviesPanel);
        tabPane.setSelectedIndex(0);

        frame.setContent(tabPane);
        
        frame.center();
        frame.setVisible(true);
    }

}
