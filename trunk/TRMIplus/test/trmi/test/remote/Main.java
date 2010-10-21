package trmi.test.remote;

import java.rmi.registry.LocateRegistry;

import org.apache.log4j.Logger;

import trmi.RemoteSecurityManager;
import trmi.test.util.LogClient;

/**
 * Test driver for a client/server example with transparant RMI in a single
 * process (JVM).
 * 
 * @author Oscar Stigter
 */
public class Main {

    private static final Logger logger = LogClient.getLogger(Main.class);

    private static final int REGISTRY_PORT = 1099; // RMI default

    public static void main(String[] args) throws Exception {
        logger.info("Started.");

        LocateRegistry.getRegistry(REGISTRY_PORT);
        RemoteSecurityManager.setup();

        logger.info("Finished.");
    }

}
