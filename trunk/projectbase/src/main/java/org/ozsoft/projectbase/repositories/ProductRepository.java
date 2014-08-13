package org.ozsoft.projectbase.repositories;

import javax.ejb.Stateless;

import org.ozsoft.projectbase.entities.Product;

@Stateless
public class ProductRepository extends Repository<Product> {

    public ProductRepository() {
        super(Product.class);
    }
}
