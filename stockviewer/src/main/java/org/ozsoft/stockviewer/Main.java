package org.ozsoft.stockviewer;

public class Main {

    public static void main(String[] args) {
        HttpPageReader reader = new HttpPageReader();
        // reader.setUseProxy(true);
        // reader.setProxyHost("");
        // reader.setProxyPort(8080);
        // reader.setProxyUsername("");
        // reader.setProxyPassword("");
        String[] lines = reader.read("http://download.finance.yahoo.com/d/quotes.csv?s=IVV&f=d1t1l1");
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
