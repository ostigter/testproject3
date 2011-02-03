package org.ozsoft.icefaces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "userBean")
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
        if (name != null && name.length() > 0) {
            return String.format("Hello, %s!", name);
        } else {
            return "";
        }
    }

}
