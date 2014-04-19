package org.example.javaee.webapp;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("greeter")
@RequestScoped
public class GreeterController implements Serializable {

    private static final long serialVersionUID = 5297412712784379330L;

    @Inject
    private Greeter greeter;

    private String greeting;

    public void setName(String name) {
        greeting = greeter.getGreeting(name);
    }

    public String getGreeting() {
        return greeting;
    }

}
