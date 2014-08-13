package org.ozsoft.projectbase.web;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.projectbase.entities.Product;
import org.ozsoft.projectbase.entities.Release;
import org.ozsoft.projectbase.repositories.ProductRepository;

@Named
@SessionScoped
public class ReleaseBean implements Serializable {

    private static final long serialVersionUID = 8246447601649350345L;

    @Inject
    private ProductRepository productRepository;

    private String title;

    private String productName;

    private Product product;

    private Release release;

    private String name;

    private Date date;

    private String description;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (product != null) {
            release = new Release();
            release.setProduct(product);
            product.getReleases().add(release);
            title = "Add Release";
            name = "";
            date = new Date();
            description = "";
            return "editRelease.xhtml";
        } else {
            return "listReleases.xhtml";
        }
    }

    public String edit() {
        if (release != null) {
            title = "Edit Release";
            name = release.getName();
            date = release.getDate();
            description = release.getDescription();
            return "editRelease.xhtml";
        } else {
            return "listReleases.xhtml";
        }
    }

    public String save() {
        release.setName(name);
        release.setDate(date);
        release.setDescription(description);
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
