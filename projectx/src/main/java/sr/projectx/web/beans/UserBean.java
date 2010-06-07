package sr.projectx.web.beans;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import sr.projectx.entities.User;
import sr.projectx.services.LogService;
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

    /** Log. */
    private static final Logger LOG = Logger.getLogger(UserBean.class);

	/** Log service. */
	@ManagedProperty(value = "#{logService}")
	private LogService logService;

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
    
    /** New password. */
    private String newPassword;

    /** Email. */
    private String email;

	/**
	 * Constructor.
	 */
	public UserBean() {
		// Empty implementation.
	}
	
	/**
	 * Sets the Log service.
	 * 
	 * @param ogService
	 *            The Log service.
	 */
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	/**
	 * Sets the User service.
	 * 
	 * @param userService
	 *            The User service.
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

    /**
     * Logs in a user.
     * 
     * @return The navigation result.
     */
    public String doLogIn() {
		String action = null;
		String hashedPassword = DigestUtils.shaHex(password);
		if (userService.checkCredentials(username, hashedPassword)) {
			user = userService.retrieve(username);
			user.updateLastLogin();
			userService.update(user);
			logAccess(user);
			getSession(true).setAttribute("username", username);
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

    /**
     * Logs out the current user.
     * 
     * @return The navigation result.
     */
	public String doLogOut() {
//		LOG.debug(String.format("Logged out user '%s'", username));
		clearUser();
		return "home.jsf";
	}

    /**
     * Creates a new user account (Create Account page).
     * 
     * @return The navigation result.
     */
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
    
    /**
     * Saves updates to the user account (Edit Account page).
     * 
     * @return The navigation result.
     */
    public String doSave() {
        String action = null;
        FacesContext fc = FacesContext.getCurrentInstance();
        String hashedPassword = DigestUtils.shaHex(password);
        if (newPassword.equals(passwordAgain)) {
            if (userService.checkCredentials(username, hashedPassword)) {
                user.setPassword(DigestUtils.shaHex(newPassword));
                try {
                    userService.update(user);
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "The password has been changed.", null));
                    logService.info("Password updated for user '%s' (%s)", username, user.getEmail());
                } catch (PersistenceException e) {
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "An error occurred while updating the password.", null));
                }
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Incorrect old password.", null));
            }
        } else {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Both new passwords are not identical.", null));
        }
        return action;
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
	
	private void logAccess(User user) {
		String address = null;
		String hostname = null;
		InetAddress remoteAddress = getRemoteAddress();
		if (remoteAddress != null) {
			address = remoteAddress.getHostAddress();
			hostname = remoteAddress.getCanonicalHostName();
		}
		logService.logAccess(user, address, hostname);
	}
	
	private static HttpSession getSession(boolean create) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (HttpSession) facesContext.getExternalContext().getSession(create);
	}
	
	private static InetAddress getRemoteAddress() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String remoteHost = request.getRemoteHost();
		InetAddress address = null;
		try {
			address = InetAddress.getByName(remoteHost);
		} catch (UnknownHostException e) {
			LOG.error(String.format("Could not resolve IP address and/or hostname for remote host '%s' ", remoteHost), e);
		}
		return address;
	}

}
