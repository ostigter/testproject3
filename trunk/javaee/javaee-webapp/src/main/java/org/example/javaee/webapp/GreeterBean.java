package org.example.javaee.webapp;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class GreeterBean implements Greeter {

    @Override
    public String getGreeting(String name) {
        if (name != null & name.length() > 0) {
            return String.format("Hello, %s!", name);
        } else {
            return "Hello!";
        }
    }

}
