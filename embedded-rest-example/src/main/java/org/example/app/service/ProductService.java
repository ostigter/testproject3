package org.example.app.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.example.app.domain.Product;

public class ProductService {

    private final Map<Long, Product> products;

    public ProductService() {
        products = new HashMap<Long, Product>();
        initProducts();
    }

    public Collection<Product> getProducts() {
        return products.values();
    }

    private void initProducts() {
        products.put(1L, new Product(1L, "Hammer", 10.00));
        products.put(2L, new Product(2L, "Box of 1000 nails", 5.00));
        products.put(3L, new Product(3L, "Screw driver", 2.00));
    }
}
