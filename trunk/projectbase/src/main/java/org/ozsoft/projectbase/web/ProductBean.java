package org.ozsoft.projectbase.web;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ozsoft.projectbase.entities.Product;
import org.ozsoft.projectbase.repositories.ProductRepository;

@ManagedBean
@SessionScoped
public class ProductBean implements Serializable {

    private static final long serialVersionUID = 4728584552025654369L;

    @EJB
    private ProductRepository productRepository;

    private String title;

    private Product product;

    private String code;

    private String name;

    public String getTitle() {
        return title;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product project) {
        this.product = project;
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

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public String add() {
        title = "Add Product";
        product = null;
        code = "";
        name = "";
        return "editProduct.xhtml";
    }

    public String edit() {
        title = "Edit Product";
        code = product.getCode();
        name = product.getName();
        return "editProduct.xhtml";
    }

    public String save() {
        if (product == null) {
            product = new Product();
        }
        product.setCode(code);
        product.setName(name);
        productRepository.store(product);
        return "listProducts.xhtml";
    }

    public String delete() {
        if (product != null) {
            productRepository.delete(product.getId());
        }
        return "listProducts.xhtml";
    }

    public String cancel() {
        return "listProducts.xhtml";
    }
}
