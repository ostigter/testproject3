package org.example.javaee.greeter.web;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.example.javaee.greeter.domain.User;
import org.example.javaee.greeter.domain.UserDao;

@Named
@RequestScoped
public class CreateController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private UserDao userDao;

    @Produces
    @Named
    @RequestScoped
    private final User newUser = new User();

    public void create() {
        try {
            userDao.createUser(newUser);
            String message = "A new user with id " + newUser.getId() + " has been created successfully";
            facesContext.addMessage(null, new FacesMessage(message));
        } catch (Exception e) {
            String message = "An error has occured while creating the user (see log for details)";
            facesContext.addMessage(null, new FacesMessage(message));
        }
    }

}
