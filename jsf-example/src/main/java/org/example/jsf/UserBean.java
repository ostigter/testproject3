package org.example.jsf;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class UserBean {
    
    private String username;

    private String password;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        if (username.equals("oscar") && password.equals("appel")) {
            return "home.xhtml";
        } else {
            return "login_failed.xhtml";
        }
    }

}
