package org.example.greeter.service;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;
import org.example.greeter.api.Greeter;

@Stateless
@Remote
public class GreeterBean implements Greeter {

    @Override
    public String getGreeting(String name) {
        if (StringUtils.isEmpty(name)) {
            return "Hello!";
        } else {
            return String.format("Hello, %s!", name);
        }
    }
}
