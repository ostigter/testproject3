package org.ozsoft.freecap.market;

import java.util.ArrayList;
import java.util.List;

public class Market {

    public double price;

    private List<Order> buyQueue;

    private List<Order> sellQueue;

    public Market() {
        buyQueue = new ArrayList<Order>();
        sellQueue = new ArrayList<Order>();
    }

    public void sell(int volume, double minPrice) {
        Order order = null;
        for (Order o : buyQueue) {
            if (o.price >= minPrice) {
                order = o;
                break;
            }
        }
        if (order != null) {
            // Found matching buy order.
            price = Math.max(minPrice, order.price);
            if (volume > order.volume) {
                volume = order.volume;
            }
            order.volume -= volume;
            if (order.volume == 0) {
                buyQueue.remove(order);
            }
            System.out.format("Selling %d for $%.2f\n", volume, price);
        } else {
            // Place new order on queue.
            order = new Order();
            order.volume = volume;
            order.price = minPrice;
            sellQueue.add(order);
            System.out.format("Adding sell order for %d x $%.2f\n", order.volume, minPrice);
        }
    }

    public void buy(int volume, double maxPrice) {
        Order order = null;
        for (Order o : sellQueue) {
            if (o.price <= maxPrice) {
                order = o;
                break;
            }
        }
        if (order != null) {
            // Found matching sell order.
            price = Math.max(maxPrice, order.price);
            if (volume > order.volume) {
                volume = order.volume;
            }
            order.volume -= volume;
            if (order.volume == 0) {
                sellQueue.remove(order);
            }
            System.out.format("Buying %d for $%.2f\n", volume, price);
        } else {
            // Place new order on queue.
            order = new Order();
            order.volume = volume;
            order.price = maxPrice;
            buyQueue.add(order);
            System.out.format("Adding buy order for %d x $%.2f\n", volume, maxPrice);
        }
    }

}
