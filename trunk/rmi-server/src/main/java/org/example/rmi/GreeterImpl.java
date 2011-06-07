package org.example.rmi;

public class GreeterImpl implements Greeter {

    public String getGreeting(String name) {
        return String.format("Hello, %s!", name);
    }

}
