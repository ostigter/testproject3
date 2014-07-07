package org.example.jboss.module;

import org.junit.Assert;
import org.junit.Test;

public class HelloServiceTest {

    @Test
    public void getMessage() {
        HelloService helloService = new HelloService();
        Assert.assertEquals("Incorrect greeting message", "Hello World!", helloService.getGreeting());
    }
}
