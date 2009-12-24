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

    public String doLogIn() {
	// Check credentials.
        if (!username.equals("oscar") && password.equals("appel")) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
        	"Invalid username/password combination", null));
            clearUser();
            return null;
        }

        // Logged in successfully.
        return "welcome.xhtml";
    }
    
    public String doLogOut() {
	clearUser();
	return "login.jsf";
    }
    
    public void createAccount(String username, String password) {
	this.username = username;
	this.password = password;
    }
    
    private void clearUser() {
	username = null;
	password = null;
    }
    
}
