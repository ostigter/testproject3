package trmi.test.local;

import java.rmi.registry.LocateRegistry;

import trmi.RemoteSecurityManager;

/**
 * Test driver for a client/server example with transparant RMI in a single
 * process (JVM).
 * 
 * @author Oscar Stigter
 */
public class Main {

    private static final int REGISTRY_PORT = 1099;  // RMI Default

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            LocateRegistry.createRegistry(REGISTRY_PORT);

            RemoteSecurityManager.setup();

            HelloServer server = new HelloServerImpl();
            server.start();

            HelloClient client = new HelloClient();
            client.getGreeting();

            server.stop();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
