package org.ozsoft.portfoliomanager;

import java.io.IOException;

import org.ozsoft.portfoliomanager.util.HtmlGrabber;
import org.ozsoft.portfoliomanager.util.HttpPageReader;

public class Main {

    public static void main(String[] args) {
        // SwingUtilities.invokeLater(new Runnable() {
        // @Override
        // public void run() {
        // new MainFrame();
        // }
        // });

        HttpPageReader httpPageReader = new HttpPageReader();
        try {
            String content = httpPageReader.read("http://finance.yahoo.com/q?s=ORC");
            HtmlGrabber htmlGrabber = new HtmlGrabber(content);
            double price = Double.parseDouble(htmlGrabber.search("yfs_l84"));
            double changePerc = Double.parseDouble(htmlGrabber.search("yfs_p43").substring(2, 5));
            System.out.format("$ %.2f (%+.2f %%)\n", price, changePerc);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
