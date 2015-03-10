package org.example.jetty.ui;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class GreetingBean implements Serializable {

    private static final long serialVersionUID = -1968484932253327213L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGreeting() {
        if (name != null && !name.isEmpty()) {
            return String.format("Hello, %s!", name);
        } else {
            return "";
        }
    }
}
