package org.ozsoft.taskman;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class CreateAccountBean implements Serializable {
    
    private static final long serialVersionUID = -1806089935280052105L;
    
    @ManagedProperty(value = "#{securityBean}")
    private SecurityBean securityBean;

    private String username;

    private String password;
    
    private String passwordAgain;
    
    public SecurityBean getSecurityBean() {
	return securityBean;
    }
    
    public void setSecurityBean(SecurityBean securityBean) {
	this.securityBean = securityBean;
    }

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
	
	securityBean.createAccount(username, password);
	
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
        	"The new account has been created successfully.", null));

	return "home.jsf";
    }
    
    public String doCancel() {
	return "welcome.jsf";
    }

}
