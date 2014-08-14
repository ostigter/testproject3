package org.ozsoft.projectbase.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

    public void logIn() {
        User user = userService.logIn(username, password);
        // TODO
    }

    public void logOut() {
        userService.logOut();
    }
}
