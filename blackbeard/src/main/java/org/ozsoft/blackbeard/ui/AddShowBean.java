package org.ozsoft.blackbeard.ui;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.ozsoft.blackbeard.services.ShowService;

@ManagedBean
@RequestScoped
public class AddShowBean implements Serializable {

    private static final long serialVersionUID = -897134941768788958L;

    @ManagedProperty(value = "#{showService}")
    private ShowService showService;

    private String name;

    public void setShowService(ShowService showService) {
        this.showService = showService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void search() {
        // TODO
    }
}
