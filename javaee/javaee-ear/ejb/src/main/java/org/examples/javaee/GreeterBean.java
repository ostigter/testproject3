package org.examples.javaee;

import javax.ejb.Stateful;

@Stateful
public class GreeterBean {

    public String sayHello(String name) {
        return String.format("Hello, %s!", name);
    }

}
