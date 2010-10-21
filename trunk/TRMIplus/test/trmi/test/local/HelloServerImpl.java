package trmi.test.local;

import trmi.Naming;

/**
 * Example implementation of a remote server with transparant RMI.
 * 
 * @author Oscar Stigter
 */
public class HelloServerImpl implements HelloServer {

    private static final String MESSAGE = "Hello World!";

    private boolean running = false;

    public void start() {
        if (!running) {
            try {
                Naming.bind(SERVER_ID, this, new Class[] { HelloServer.class });
                running = true;
                System.out.println("Server: Started.");
            } catch (Exception e) {
                System.err.println("ERROR: Could not start server: " + e);
            }
        } else {
            System.err.println("WARNING: start() called but server is already running");
        }
    }

    public void stop() {
        if (running) {
            try {
                Naming.unbind(SERVER_ID);
                running = false;
                System.out.println("Server: Stopped.");
            } catch (Exception e) {
                System.err.println("ERROR: Could not stop server: " + e);
            }
        } else {
            System.err.println("WARNING: stop() called but server not running");
        }
    }

    public String sayHello() {
        System.out.println("Server: sayHello() called.");
        return MESSAGE;
    }

}
