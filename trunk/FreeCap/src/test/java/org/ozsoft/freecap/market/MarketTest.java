package org.ozsoft.freecap.market;

import org.junit.Test;

public class MarketTest {

    @Test
    public void test() {
        Market market = new Market();
        market.sell(100, 9.00);
        market.buy(50, 10.00);
    }
}
