package org.ozsoft.projectbase.web;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ozsoft.projectbase.entities.Product;
import org.ozsoft.projectbase.entities.Release;
import org.ozsoft.projectbase.repositories.ProductRepository;

@ManagedBean
@SessionScoped
public class ReleaseBean implements Serializable {

    private static final long serialVersionUID = 8246447601649350345L;

    @EJB
    private ProductRepository productRepository;

    private String title;

    private String productName;

    private Product product;

    private Release release;

    private String name;

    public String getTitle() {
        return title;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String product) {
        this.productName = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public List<Release> getReleases() {
        List<Release> releases = null;
        if (productName != null && productName.length() > 0) {
            product = productRepository.retrieveByName(productName);
            if (product != null) {
                releases = product.getReleases();
            }
        }
        return releases;
    }

    public String add() {
        if (productName != null && productName.length() > 0) {
            title = "Add Release";
            name = "";
            return "editRelease.xhtml";
        } else {
            System.out.println("*** Project not set");
            return "listReleases.xhtml";
        }
    }

    public String edit() {
        if (release != null) {
            title = "Edit Release";
            name = release.getName();
            return "editRelease.xhtml";
        } else {
            System.out.println("*** Release not set");
            return "listReleases.xhtml";
        }
    }

    public String save() {
        if (release == null) {
            release = new Release();
        }
        release.setName(name);
        product = productRepository.retrieveByName(productName);
        release.setProduct(product);
        product.getReleases().add(release);
        productRepository.store(product);
        return "listReleases.xhtml";
    }

    public String delete() {
        if (release != null) {
            product.getReleases().remove(release);
            productRepository.store(product);
        }
        return "listReleases.xhtml";
    }

    public String cancel() {
        return "listReleases.xhtml";
    }

}
