package org.ozsoft.projectbase.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.ozsoft.projectbase.entities.User;
import org.ozsoft.projectbase.services.UserService;

/**
 * JSF backing bean for user management.
 * 
 * @author Oscar Stigter
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = -7138507297431899458L;

    @Inject
    private UserService userService;

    private String username;

    private String password;

    private boolean rememberMe;

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

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String logIn() {
        User user = userService.logIn(username, password);
        if (user != null) {
            // Successfully logged in.
            return "index.xhtml";
        } else {
            // Incorrect username or password.
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect username or password.", null));
            return null;
        }
    }

    public String logOut() {
        userService.logOut();
        clearUser();
        return "index.xhtml";
    }

    private static HttpSession getSession(boolean create) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (HttpSession) facesContext.getExternalContext().getSession(create);
    }

    private void clearUser() {
        username = null;
        password = null;
        rememberMe = false;
        getSession(false).invalidate();
    }
}
