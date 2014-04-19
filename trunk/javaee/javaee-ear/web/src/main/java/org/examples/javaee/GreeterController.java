package org.examples.javaee;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.examples.javaee.GreeterBean;

@Named("greeter")
@SessionScoped
public class GreeterController implements Serializable {

    private static final long serialVersionUID = -6536612572175529438L;

    @EJB
    private GreeterBean greeterEJB;

    private String message;

    public void setName(String name) {
        message = greeterEJB.sayHello(name);
    }

    public String getMessage() {
        return message;
    }

}
