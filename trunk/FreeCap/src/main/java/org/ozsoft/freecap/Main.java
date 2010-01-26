package org.ozsoft.freecap;

public class Main {

    public static void main(String[] args) {
	Market market = new Market();
	market.sell(100, 1.00);
	market.buy ( 10, 1.25);
//	market.buy ( 10, 1.00);
//	market.buy ( 10, 1.25);
//	market.buy (100, 1.25);
    }

}
