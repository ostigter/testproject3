package org.ozsoft.stockviewer;

import java.math.BigDecimal;
import java.util.Calendar;

public class DividendTransaction extends Transaction {

    private final BigDecimal dividendPerShare;

    public DividendTransaction(Calendar date, Stock stock, BigDecimal dividendPerShare) {
        super(date, stock);
        this.dividendPerShare = dividendPerShare;
    }

    public BigDecimal getDividendPerShare() {
        return dividendPerShare;
    }

    @Override
    public String toString() {
        return String.format("Dividend %s @ %s", getStock().getSymbol(), dividendPerShare);
    }
}
