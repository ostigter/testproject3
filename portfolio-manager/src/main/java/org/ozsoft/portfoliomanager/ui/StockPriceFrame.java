package org.ozsoft.portfoliomanager.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.ozsoft.portfoliomanager.domain.Stock;
import org.ozsoft.portfoliomanager.services.UpdateService;
import org.ozsoft.portfoliomanager.util.HttpPageReader;

public class StockPriceFrame extends JFrame {

    private static final long serialVersionUID = -7868161566551066062L;

    // private static final String PRICE_GRAPH_URI_COMPACT =
    // "http://chart.finance.yahoo.com/z?s=%s&t=%s&q=l&l=on&z=m&p=v";

    private static final String PRICE_GRAPH_URI = "http://chart.finance.yahoo.com/z?s=%s&t=%s&q=l&l=off&z=m&p=v";

    private final Stock stock;

    private final HttpPageReader httpPageReader;

    private final UpdateService updateService;

    private final JLabel priceLabel;

    private final ImagePanel fullHistoryGraphPanel;

    private final ImagePanel fiveYearGraphPanel;

    private final ImagePanel oneYearGraphPanel;

    private final ImagePanel sevenDaysGraphPanel;

    public static void show(Stock stock) {
        JFrame frame = new StockPriceFrame(stock);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private StockPriceFrame(Stock stock) {
        super(stock.getSymbol() + " - Stock price");

        this.stock = stock;

        httpPageReader = new HttpPageReader();

        updateService = new UpdateService();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        priceLabel = new JLabel();
        priceLabel.setFont(new Font("Proportional", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(priceLabel, gbc);

        fullHistoryGraphPanel = new ImagePanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(fullHistoryGraphPanel, gbc);

        fiveYearGraphPanel = new ImagePanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        getContentPane().add(fiveYearGraphPanel, gbc);

        oneYearGraphPanel = new ImagePanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        getContentPane().add(oneYearGraphPanel, gbc);

        sevenDaysGraphPanel = new ImagePanel();
        gbc.gridx = 1;
        gbc.gridy = 2;
        getContentPane().add(sevenDaysGraphPanel, gbc);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                update();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(updateButton, gbc);

        update();
    }

    private void update() {
        String symbol = stock.getSymbol();

        updateService.updatePrice(stock);
        priceLabel.setText(String.format("Current price: $%.2f", stock.getPrice()));

        try {
            fullHistoryGraphPanel.setImage(httpPageReader.downloadFile(String.format(PRICE_GRAPH_URI, symbol, "9y")));
            fiveYearGraphPanel.setImage(httpPageReader.downloadFile(String.format(PRICE_GRAPH_URI, symbol, "1y")));
            oneYearGraphPanel.setImage(httpPageReader.downloadFile(String.format(PRICE_GRAPH_URI, symbol, "10d")));
            sevenDaysGraphPanel.setImage(httpPageReader.downloadFile(String.format(PRICE_GRAPH_URI, symbol, "1d")));

            pack();

            repaint();
            revalidate();

        } catch (IOException e) {
            System.err.format("ERROR: Could not retrieve price graph for %s\n", stock);
            e.printStackTrace(System.err);
        }
    }
}
