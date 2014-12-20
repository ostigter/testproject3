package org.ozsoft.mediacenter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Main extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Configuration config;

    private final Library library;

    public Main() {
        super("Media Center");
        config = new Configuration();
        library = new Library(config);
        createUI();
        library.update();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                library.save();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Constants.FONT);
        MediaPanel seriesPanel = new ShowsMediaPanel(config, library);
        tabbedPane.add("TV Series", seriesPanel);
        MediaPanel moviesPanel = new MoviesMediaPanel(config, library);
        tabbedPane.add("Movies", moviesPanel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        getContentPane().add(tabbedPane, gbc);

        setSize(Constants.WIDTH, Constants.HEIGHT);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
}
