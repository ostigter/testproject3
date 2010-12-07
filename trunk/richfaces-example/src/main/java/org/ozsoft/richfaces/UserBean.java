package org.ozsoft.richfaces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "user")
@RequestScoped
public class UserBean {
    
    private String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGreeting() {
        return String.format("Hello, %s!", name);
    }

}
