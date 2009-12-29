package org.ozsoft.taskman.web.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.ozsoft.taskman.domain.Task;
import org.ozsoft.taskman.domain.User;
import org.ozsoft.taskman.services.UserService;

/**
 * Backing bean handling user authentication, authorization and account management.
 * 
 * @author Oscar Stigter
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
    
    private static final long serialVersionUID = -8153487303544698528L;

    @ManagedProperty(value = "#{userService}")
    private UserService userService;
    
    private User user;
    
    private String username;

    private String password;
    
    private String passwordAgain;
    
    public UserService getUserService() {
	return userService;
    }
    
    public void setUserService(UserService userService) {
	this.userService = userService;
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
	String action = null;
	FacesContext fc = FacesContext.getCurrentInstance();
	if (password.equals(passwordAgain)) {
	    prepareUser();
	    userService.create(user);
	    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		    "The new account has been created successfully.", null));
	    action = "home.jsf";
	} else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
            	"Both passwords are not identical.", null));
	}
	return action;
    }
    
    public String doLogIn() {
	String action = null;
	user = userService.retrieve(username);
	if (user != null && user.getPassword().equals(password)) {
            action = "list.jsf";
        } else {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
        	"Invalid username/password combination", null));
            clearUser();
        }
        return action;
    }
    
    public String doLogOut() {
	clearUser();
	return "home.jsf";
    }
    
    public User getUser() {
	return user;
    }
    
    public void createTask(Task task) {
	if (user == null) {
	    throw new IllegalStateException("No logged in user");
	}
	user.addTask(task);
	userService.update(user);
    }

    private void prepareUser() {
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
    }
    
    private void clearUser() {
	user = null;
	username = null;
	password = null;
    }
    
}
