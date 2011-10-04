package org.ozsoft.projectbase.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.ozsoft.projectbase.entities.User;
import org.ozsoft.projectbase.services.UserService;
import org.ozsoft.projectbase.services.UserServiceImpl;

@ManagedBean
@SessionScoped
public class UserBean {
    
    private final UserService userService;
    
    private String username;
    
    private String password;
    
    private String fullName;
    
    public UserBean() {
        userService = new UserServiceImpl();
    }
    
    public List<User> getUsers() {
        return userService.findAll();
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String add() {
        if (!username.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullName(fullName);
            userService.create(user);
        }
        return null;
    }

    public String getMessage() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        String path = servletContext.getRealPath("/");
        return path;
    }

}
