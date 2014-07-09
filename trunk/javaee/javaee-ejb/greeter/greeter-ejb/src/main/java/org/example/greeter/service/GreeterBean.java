package org.example.greeter.service;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.example.greeter.api.Greeter;

@Stateless
@Remote(Greeter.class)
public class GreeterBean implements Greeter {

    @Override
    public String getGreeting(String name) {
        // if (StringUtils.isEmpty(name)) {
        if (name == null || name.isEmpty()) {
            return "Hello!";
        } else {
            return String.format("Hello, %s!", name);
        }
    }
}
