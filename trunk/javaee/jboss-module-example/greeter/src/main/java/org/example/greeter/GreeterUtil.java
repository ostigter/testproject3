package org.example.greeter;

/**
 * Greeter utility class.
 * 
 * @author Oscar Stigter
 */
public class GreeterUtil {

    /**
     * Private constructor to deny instantiation.
     */
    private GreeterUtil() {
        // Empty implementation.
    }

    /**
     * Returns a greeting.
     * 
     * @return The greeting.
     */
    public static String getGreeting() {
        return "Hello World!";
    }
}
