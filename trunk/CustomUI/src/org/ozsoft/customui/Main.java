package org.ozsoft.customui;

public class Main {

    private static final int WIDTH = 400;

    private static final int HEIGHT = 150;

    public static void main(String[] args) {
        Frame frame = new Frame("Test Application");
        frame.setSize(WIDTH, HEIGHT);

//        Panel seriesPanel = new Panel(1, 3);
//        seriesPanel.addComponent(0, 0, new Label("Breaking Bad"));
//        seriesPanel.addComponent(0, 1, new Label("CSI: New York"));
//        seriesPanel.addComponent(0, 2, new Label("Dog Whisperer, the"));
//
//        Panel moviesPanel = new Panel(1, 3);
//        moviesPanel.addComponent(0, 0, new Label("Ratatouille"));
//        moviesPanel.addComponent(0, 1, new Label("Up in the air"));
//        moviesPanel.addComponent(0, 2, new Label("Sleuth"));
//
//        TabPane tabPane = new TabPane();
//        tabPane.addTab("Series", seriesPanel);
//        tabPane.addTab("Movies", moviesPanel);
//        tabPane.setSelectedIndex(0);
//
//        frame.setContent(tabPane);
        
        Label label = new Label("This is a label");
        frame.setContent(label);
        
        frame.center();
        frame.setVisible(true);
    }

}
