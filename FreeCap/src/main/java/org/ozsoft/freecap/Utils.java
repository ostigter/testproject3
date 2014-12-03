package org.ozsoft.freecap;

public class Utils {

    private Utils() {
        // Empty implementation.
    }

    public static String money(double amount) {
        String text = String.format("$%,.2f", amount);
        // if (text.endsWith(".00")) {
        // text = text.substring(0, text.indexOf('.'));
        // }
        return text;
    }
}
