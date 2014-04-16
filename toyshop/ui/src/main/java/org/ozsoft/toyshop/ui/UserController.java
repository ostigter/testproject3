package org.ozsoft.toyshop.ui;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.toyshop.api.User;
import org.ozsoft.toyshop.api.UserService;

@Named
@RequestScoped
public class UserController {

    @Inject
    private UserService userService;

    public String getGreeting() {
        User user = userService.createUser("jsmith", "secret");
        System.out.println("### user = " + user);
        return "Hello World!";
    }

}
