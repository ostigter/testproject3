package com.example.calculator;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementation of the Calculator service.
 * 
 * @author Oscar Stigter
 */
public class Server implements Calculator {

    /** The RMI server port. */
    private static final int SERVER_PORT = 1099;

    /** The RMI registry. */
    private Registry registry;
    
    /** Whether the server is running. */
    private boolean isRunning = false;

    /**
     * Program's main entry point.
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(String[] args) {
        new Server().start();
    }
    
    /**
     * Constructs the server.
     */
    public Server() {
        // Add JVM shutdown hook (to catch CTRL-C).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    /**
     * Starts the server.
     */
    public void start() {
        if (!isRunning) {
            System.out.println("Starting server...");
    
            // Override the default security manager.
            DummySecurityManager.activate();
    
            // Register the server.
            try {
                registry = LocateRegistry.createRegistry(SERVER_PORT);
                UnicastRemoteObject.exportObject(this, 0);
                registry.bind(SERVICE_ID, this);
                isRunning = true;
                System.out.println("Server started. (Press CTRL-C to shutdown.)");
            } catch (Exception e) {
                System.err.println("ERROR: Could not start server: " + e.getMessage());
            }
        }
    }

    /**
     * Do a clean shutdown of the server.
     */
    private void shutdown() {
        System.out.println("Shutting down...");
        if (isRunning) {
            stop();
        }
    }

    /**
     * Stops the server.
     */
    public void stop() {
        if (isRunning) {
            System.out.println("Stopping server...");
            if (registry != null) {
                try {
                    // Unregister the server.
                    registry.unbind(SERVICE_ID);
                    UnicastRemoteObject.unexportObject(this, true);
                    System.out.println("Server stopped");
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            isRunning = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.calculator.Calculator#add(int, int)
     */
    public int add(int i, int j) throws RemoteException {
        System.out.format("Received request: add(%d, %d)\n", i, j);
        int result = i + j;
        System.out.format("Returning result: %d\n", result);
        return result;
    }

}
