package org.ozsoft.taskman;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CreateAccountBean implements Serializable {
    
    private static final long serialVersionUID = -1806089935280052105L;

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
	if (!password.equals(passwordAgain)) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
        	"Both passwords are not identical.", null));
            return null;
	}
	return "login.jsf";
    }
    
    public String doCancel() {
	return "welcome.jsf";
    }
    
}
