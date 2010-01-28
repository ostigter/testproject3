package org.ozsoft.taskman.web.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.ozsoft.taskman.domain.Task;
import org.ozsoft.taskman.domain.User;
import org.ozsoft.taskman.services.UserService;

/**
 * Backing bean handling user authentication, authorization and account
 * management.
 * 
 * @author Oscar Stigter
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
	
	/** Log. */
	private static final Logger LOG = Logger.getLogger(UserBean.class);

	/** Serial version UID. */
	private static final long serialVersionUID = -8153487303544698528L;

	/** User service. */
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	/** Logged in user. */
	private User user;

	/** Username. */
	private String username;

	/** Password. */
	private String password;

	/** Password (again). */
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
		if (userService.checkCredentials(username, password)) {
			user = userService.retrieve(username);
			getSession(true).setAttribute("username", username);
//			LOG.debug(String.format("Logged in user '%s'", user.getUsername()));
			action = "list.jsf";
		} else {
			LOG.debug(String.format("Failed login attempt for user '%s'", username));
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid username/password combination", null));
			clearUser();
		}
		return action;
	}

	public String doLogOut() {
//		LOG.debug(String.format("Logged out user '%s'", username));
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

	public void editTask(Task task) {
		if (user == null) {
			throw new IllegalStateException("No logged in user");
		}
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
		getSession(false).invalidate();
	}
	
	private HttpSession getSession(boolean create) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (HttpSession) facesContext.getExternalContext().getSession(create);
	}

}
