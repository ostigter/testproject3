package org.example.greeter.client;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.example.greeter.api.Greeter;

@Named
@RequestScoped
public class GreeterClient {

    @EJB(lookup = "java:global/greeter-ear/greeter-ejb-1.0/GreeterBean")
    private Greeter greeter;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getGreeting() {
        return greeter.getGreeting(name);
    }
}
