package org.example.app.rest;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.example.app.domain.Product;
import org.example.app.service.ProductService;

@Path("/products")
public class ProductResource {

    private final ProductService productService;

    public ProductResource() {
        productService = new ProductService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Product> test() {
        return productService.getProducts();
    }
}
