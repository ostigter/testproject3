package org.example.javaee.webapp.model;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.example.javaee.webapp.services.GreeterService;

@Named("greeterModel")
@RequestScoped
public class GreeterModel implements Serializable {

    private static final long serialVersionUID = 5297412712784379330L;

    @Inject
    private GreeterService greeter;

    private String greeting;

    public void setName(String name) {
        greeting = greeter.getGreeting(name);
    }

    public String getGreeting() {
        return greeting;
    }
}
