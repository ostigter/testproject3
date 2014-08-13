package org.ozsoft.projectbase.web;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.projectbase.entities.Product;
import org.ozsoft.projectbase.repositories.ProductRepository;

@Named
@RequestScoped
public class ProductBean implements Serializable {

    private static final long serialVersionUID = 4728584552025654369L;

    @Inject
    private ProductRepository productRepository;

    private String title;

    private Product product;

    private String code;

    private String name;

    private String description;

    public String getTitle() {
        return title;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public String add() {
        title = "Add Product";
        product = null;
        code = "";
        name = "";
        description = "";
        return "editProduct.xhtml";
    }

    public String edit() {
        title = "Edit Product";
        code = product.getCode();
        name = product.getName();
        description = product.getDescription();
        return "editProduct.xhtml";
    }

    public String save() {
        if (product == null) {
            product = new Product();
        }
        product.setCode(code);
        product.setName(name);
        product.setDescription(description);
        productRepository.store(product);
        return "listProducts.xhtml";
    }

    public String delete() {
        if (product != null) {
            productRepository.delete(product);
        }
        return "listProducts.xhtml";
    }

    public String cancel() {
        return "listProducts.xhtml";
    }
}
