package customui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

    private static final int WIDTH = 400;

    private static final int HEIGHT = 300;

    public static void main(String[] args) {
        JFrame app = new JFrame("Test Application");
        app.setUndecorated(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setLayout(new BorderLayout());

        Frame frame = new Frame();
        frame.setSize(WIDTH, HEIGHT);
        frame.doLayout();
        app.add(frame, BorderLayout.CENTER);

        app.setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));
        app.pack();
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }

}
