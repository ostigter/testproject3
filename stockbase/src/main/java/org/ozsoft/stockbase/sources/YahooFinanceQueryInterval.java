
package org.ozsoft.stockbase.sources;

/**
 * Yahoo Finance query interval.
 * 
 * @author Oscar Stigter
 */
public enum YahooFinanceQueryInterval {

    /** Daily. */
    DAILY("1d"),

    /** Weekly. */
    WEEKLY("5d"),

    /** Monthly. */
    MONTHLY("1mo");

    private final String tag;

    YahooFinanceQueryInterval(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }
}
