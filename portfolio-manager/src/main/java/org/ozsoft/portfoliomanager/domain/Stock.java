// This file is part of the 'portfolio-manager' (Portfolio Manager)
// project, an open source stock portfolio manager application
// written in Java.
//
// Copyright 2015 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.portfoliomanager.domain;

/**
 * Common stock, issued by a company.
 * 
 * @author Oscar Stigter
 */
public class Stock implements Comparable<Stock> {

    private final String symbol;

    private String name;

    private Exchange exchange = Exchange.UNKNOWN;

    private double price;

    private double changePerc;

    private double peRatio = -1.0;

    private double targetPrice;

    private double divRate;

    private double divGrowth = 0.0;

    private int yearsDivGrowth = -1;

    private CreditRating creditRating = CreditRating.NA;

    private int starRating = -1;

    private String comment;

    private StockLevel level = StockLevel.WATCH;

    /**
     * Constructor.
     * 
     * @param symbol
     *            Ticket symbol (e.g. "MSFT").
     * @param name
     *            Name (e.g. "Microsoft").
     */
    public Stock(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    /**
     * Returns the ticket symbol.
     * 
     * @return The ticket symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the stock exchange this stock is traded on.
     * 
     * @return The stock exchange.
     */
    public Exchange getExchange() {
        return exchange;
    }

    /**
     * Sets the stock exchange this stock is traded on.
     * 
     * @param exchange
     *            The stock exchange.
     */
    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    /**
     * Returns the current price.
     * 
     * @return The current price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the current price.
     * 
     * @param price
     *            The current price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the current price change percentage based on the previous closing price.
     * 
     * @return The price change percentage.
     */
    public double getChangePerc() {
        return changePerc;
    }

    /**
     * Sets the current price change percentage based on the previous closing price.
     * 
     * @param changePerc
     *            The price change percentage.
     */
    public void setChangePerc(double changePerc) {
        this.changePerc = changePerc;
    }

    /**
     * Returns the trailing P/E (price/earnings) ratio.
     * 
     * @return The trailing P/E ratio.
     */
    public double getPeRatio() {
        return peRatio;
    }

    /**
     * Sets the trailing P/E (price/earnings) ratio.
     * 
     * @param peRatio
     *            The P/E ratio.
     */
    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    /*
     * Returns the target price.
     * 
     * @return The target price.
     */
    public double getTargetPrice() {
        return targetPrice;
    }

    /**
     * Sets he target price.
     * 
     * @param targetPrice
     *            The target price.
     */
    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    /**
     * Returns the target price index.
     * 
     * @return The target price index.
     */
    public double getTargetPriceIndex() {
        if (targetPrice > 0.0 && price > 0.0) {
            return (targetPrice / price) * 100.0;
        } else {
            return 0.0;
        }
    }

    /**
     * Returns the current, annual dividend rate per share.
     * 
     * @return The dividend rate.
     */
    public double getDivRate() {
        return divRate;
    }

    /**
     * Sets the current, annual dividend rate per share.
     * 
     * @param divRate
     *            The dividend rate.
     */
    public void setDivRate(double divRate) {
        this.divRate = divRate;
    }

    /**
     * Returns the current dividend yield.
     * 
     * @return The current dividend yield.
     */
    public double getYield() {
        if (price > 0.0 && divRate > 0.0) {
            double yield = (divRate / price) * 100.0;
            if (yield < 0.0) {
                yield = 0.0;
            }
            return (divRate / price) * 100.0;
        } else {
            return 0.0;
        }
    }

    /**
     * Returns the comment (if set).
     * 
     * @return The comment, or {@code null} if not set.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     * 
     * @param comment
     *            The comment, or {@code null} to clear.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the 'stock level'.
     * 
     * @return The 'stock level'.
     */
    public StockLevel getLevel() {
        return level;
    }

    /**
     * Sets the 'stock level'.
     * 
     * @param level
     *            The 'stock level'.
     */
    public void setLevel(StockLevel level) {
        this.level = level;
    }

    /**
     * Returns the number of consecutive years of dividend growth.
     * 
     * @return The number of consecutive years of dividend growth.
     */
    public int getYearsDivGrowth() {
        return yearsDivGrowth;
    }

    /**
     * Sets the number of consecutive years of dividend growth.
     * 
     * @param yearsDivGrowth
     *            The number of consecutive years of dividend growth.
     */
    public void setYearsDivGrowth(int yearsDivGrowth) {
        this.yearsDivGrowth = yearsDivGrowth;
    }

    /**
     * Returns the 5-year compounded annual dividend growth rate.
     * 
     * @return the 5-year compounded annual dividend growth rate.
     */
    public double getDivGrowth() {
        return divGrowth;
    }

    /**
     * Sets the 5-year compounded annual dividend growth rate.
     * 
     * @param divGrowth
     *            The 5-year compounded annual dividend growth rate.
     */
    public void setDivGrowth(double divGrowth) {
        this.divGrowth = divGrowth;
    }

    /**
     * Returns the current credit rating.
     * 
     * @return The current credit rating.
     */
    public CreditRating getCreditRating() {
        return creditRating;
    }

    /**
     * Sets the credit rating.
     * 
     * @param creditRating
     *            The credit rating.
     */
    public void setCreditRating(CreditRating creditRating) {
        this.creditRating = creditRating;
    }

    /**
     * Returns the current Morningstar value rating.
     * 
     * @return The current Morningstar value rating.
     */
    public int getStarRating() {
        return starRating;
    }

    /**
     * Sets the current Morningstar value rating.
     * 
     * @param starRating
     *            The current Morningstar value rating.
     */
    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if ((other != null) && (other instanceof Stock)) {
            return ((Stock) other).getSymbol().equals(symbol);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, symbol);
    }

    @Override
    public int compareTo(Stock other) {
        return name.compareTo(other.getName());
    }
}
