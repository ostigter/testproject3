package org.ozsoft.portfoliomanager.domain;

public enum TransactionType {

    BUY("Buy"),

    SELL("Sell"),

    DIVIDEND("Dividend"),

    ;

    private final String name;

    private TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
