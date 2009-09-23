package customui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main {

    private static final int WIDTH = 800;

    private static final int HEIGHT = 450;

    public static void main(String[] args) {
        JFrame app = new JFrame("Test Application");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setLayout(new BorderLayout());

        Frame frame = new Frame();
        frame.setSize(WIDTH, HEIGHT);
        frame.doLayout();
        app.add(frame, BorderLayout.CENTER);

        app.setSize(WIDTH, HEIGHT);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }

}
