package org.ozsoft.taskman;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
    
    private static final long serialVersionUID = -8153487303544698528L;

    private String username;

    private String password;
    
    private String passwordAgain;
    
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

    public String getPasswordAgain() {
	return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
	this.passwordAgain = passwordAgain;
    }

    public String doCreateAccount() {
	FacesContext fc = FacesContext.getCurrentInstance();
	
	if (!password.equals(passwordAgain)) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
        	"Both passwords are not identical.", null));
            return null;
	}
	
	createAccount(username, password);
	
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
        	"The new account has been created successfully.", null));

	return "home.jsf";
    }
    
    public String doLogIn() {
	// Check credentials.
        if (!(username.equals("oscar") && password.equals("appel"))) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
        	"Invalid username/password combination", null));
            clearUser();
            return null;
        }

        // Logged in successfully.
        return "home.xhtml";
    }
    
    public String doLogOut() {
	clearUser();
	return "home.jsf";
    }
    
    private void createAccount(String username, String password) {
	this.username = username;
	this.password = password;
    }
    
    private void clearUser() {
	username = null;
	password = null;
    }
    
}
