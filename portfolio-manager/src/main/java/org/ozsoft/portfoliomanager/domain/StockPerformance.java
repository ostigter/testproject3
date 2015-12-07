package org.ozsoft.portfoliomanager.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StockPerformance {

    private final List<ClosingPrice> prices;

    private final int duration;

    private double startPrice;

    private double endPrice;

    private double lowPrice;

    private double highPrice;

    private double change;

    private double changePerc;

    private double volatility;

    public StockPerformance(List<ClosingPrice> prices, TimeRange dateFilter) {
        this.prices = new ArrayList<ClosingPrice>();
        this.duration = dateFilter.getDuration();

        Date fromDate = dateFilter.getFromDate();
        for (ClosingPrice price : prices) {
            if (price.getDate().after(fromDate)) {
                this.prices.add(price);
            }
        }

        Collections.sort(this.prices);

        int count = this.prices.size();
        startPrice = this.prices.get(0).getValue();
        endPrice = this.prices.get(count - 1).getValue();
        lowPrice = Double.MAX_VALUE;
        highPrice = Double.MIN_VALUE;
        change = endPrice - startPrice;
        changePerc = (change / startPrice) * 100.0;
        double slope = change / count;

        for (int i = 0; i < count; i++) {
            double p = this.prices.get(i).getValue();
            if (p < lowPrice) {
                lowPrice = p;
            }
            if (p > highPrice) {
                highPrice = p;
            }
            double avg = startPrice + (i * slope);
            volatility += Math.abs(p - avg) / p * 100.0;
        }
        volatility /= count;
    }

    public List<ClosingPrice> getPrices() {
        return Collections.unmodifiableList(prices);
    }

    public double getStartPrice() {
        return startPrice;
    }

    public double getEndPrice() {
        return endPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public double getChange() {
        return change;
    }

    public double getChangePerc() {
        return changePerc;
    }

    public double getVolatility() {
        return volatility;
    }

    public double getCagr() {
        if (duration < 1) {
            return changePerc;
        } else {
            return (Math.pow(endPrice / startPrice, 1.0 / duration) - 1.0) * 100.0;
        }
    }

    public double getDiscount() {
        double discount = ((highPrice - endPrice) / (highPrice - lowPrice)) * 100.0;
        if (discount < 0.0) {
            return 0.0;
        } else {
            return discount;
        }
    }
}
