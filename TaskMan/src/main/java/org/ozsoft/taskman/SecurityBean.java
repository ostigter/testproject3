package org.ozsoft.taskman;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SecurityBean implements Serializable {
    
    private static final long serialVersionUID = -8153487303544698528L;

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

    public boolean isLoggedIn() {
	return (username != null);
    }

    public String doLogin() {
        if (username.equals("oscar") && password.equals("appel")) {
            return "home.xhtml";
        } else {
        	FacesContext fc = FacesContext.getCurrentInstance();
        	fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
        		"Invalid username/password combination", null));
        	return null;
        }
    }
    
}
