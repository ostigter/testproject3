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

public class Stock implements Comparable<Stock> {

    private final String symbol;

    private String name;

    private Exchange exchange = Exchange.UNKNOWN;

    private double price;

    private double changePerc;

    private double peRatio = -1.0;

    private double targetPrice;

    private double divRate;

    private double divGrowth = -1.0;

    private int yearsDivGrowth = -1;

    private CreditRating creditRating = CreditRating.NA;

    private int starRating = -1;

    private String comment;

    private StockLevel level = StockLevel.WATCH;

    public Stock(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChangePerc() {
        return changePerc;
    }

    public void setChangePerc(double changePerc) {
        this.changePerc = changePerc;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public double getTargetPriceIndex() {
        if (targetPrice > 0.0 && price > 0.0) {
            return (targetPrice / price) * 100.0;
        } else {
            return 0.0;
        }
    }

    public double getDivRate() {
        return divRate;
    }

    public void setDivRate(double divRate) {
        this.divRate = divRate;
    }

    public double getYield() {
        if (price > 0.0) {
            double yield = (divRate / price) * 100.0;
            if (yield < 0.0) {
                yield = 0.0;
            }
            return (divRate / price) * 100.0;
        } else {
            return 0.0;
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public StockLevel getLevel() {
        return level;
    }

    public void setLevel(StockLevel level) {
        this.level = level;
    }

    public int getYearsDivGrowth() {
        return yearsDivGrowth;
    }

    public void setYearsDivGrowth(int yearsDivGrowth) {
        this.yearsDivGrowth = yearsDivGrowth;
    }

    public double getDivGrowth() {
        return divGrowth;
    }

    public void setDivGrowth(double divGrowth) {
        this.divGrowth = divGrowth;
    }

    public CreditRating getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(CreditRating creditRating) {
        this.creditRating = creditRating;
    }

    public int getStarRating() {
        return starRating;
    }

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
