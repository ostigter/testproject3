package com.example.calculator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Command line client for the Calculator service. <br />
 * <br />
 * 
 * Optional arguments are the server's hostname and port.
 * 
 * @author Oscar Stigter
 */
public class Client {

    /** The RMI server hostname. */
    private static final String DEFAULT_HOST = "localhost";

    /** The RMI server port. */
    private static final int DEFAULT_PORT = 1099;

    /** The Calculator service. */
    private Calculator calculator;

    /**
     * Program's main entry point.
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        // Optional first argument overrides the server's hostname.
        if (args.length >= 1) {
            host = args[0];
        }
        int port = DEFAULT_PORT;
        // Optional second argument overrides the server's port.
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println(e);
            }
        }

        new Client().run(host, port);
    }

    /**
     * Main program.
     */
    public void run(String host, int port) {
        System.out.println("Started");

        // Set a custom security manager to allow RMI and process execution
        DummySecurityManager.activate();

        try {
            System.out.format("Connecting to the RMI registry on host '%s' and port %d\n", host, port);
            Registry registry = LocateRegistry.getRegistry(host, port);

            System.out.format("Retrieving local proxy for remote service '%s'\n", Calculator.SERVICE_ID);
            calculator = (Calculator) registry.lookup(Calculator.SERVICE_ID);

            System.out.println("Executing remote call");
            int result = calculator.add(2, 3);
            System.out.println("Result: " + result);

            System.out.println("Finished");

        } catch (Exception e) {
            System.err.println("ERROR: Could not connect to server: " + e.getMessage());
        }
    }
    
}
