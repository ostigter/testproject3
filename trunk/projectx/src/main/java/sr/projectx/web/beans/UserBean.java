package sr.projectx.web.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import sr.projectx.entities.User;
import sr.projectx.services.UserService;

/**
 * Backing bean handling user authentication, authorization and account
 * management.
 * 
 * @author Oscar Stigter
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {
	
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
	
    /** Email. */
    private String email;

	/**
	 * Constructor.
	 */
	public UserBean() {
		// Empty implementation.
	}
	
	/**
	 * Sets the User service.
	 * 
	 * @param userService The User service.
	 */
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
	
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String doCreateAccount() {
		String action = null;
		FacesContext fc = FacesContext.getCurrentInstance();
		if (password.equals(passwordAgain)) {
	        user = new User();
	        user.setUsername(username);
	        user.setPassword(DigestUtils.shaHex(password));
	        user.setEmail(email);
	        try {
    			userService.create(user);
    			action = doLogIn();
	        } catch (Exception e) {
	            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "An error occurred while creating the user.", null));
	            clearUser();
	        }
		} else {
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Both passwords are not identical.", null));
		}
		return action;
	}

	public String doLogIn() {
		String action = null;
		String hashedPassword = DigestUtils.shaHex(password);
		if (userService.checkCredentials(username, hashedPassword)) {
			user = userService.retrieve(username);
			getSession(true).setAttribute("username", username);
//			LOG.debug(String.format("Logged in user '%s'", user.getUsername()));
			action = "home.jsf";
		} else {
//			LOG.debug(String.format("Failed login attempt for user '%s'", username));
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid username/password combination.", null));
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

	private void clearUser() {
		user = null;
		username = null;
		password = null;
		email = null;
		getSession(false).invalidate();
	}
	
	private HttpSession getSession(boolean create) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (HttpSession) facesContext.getExternalContext().getSession(create);
	}

}
