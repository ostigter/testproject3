package org.ozsoft.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Console application to monitor an HTTP connection over a period of time.
 * 
 * @author Oscar Stigter
 */
public class ConnectionChecker {

    /** The test URL. */
    private static final String URL = "http://www.google.nl/";

    /** Polling interval in miliseconds. */
    private static final long INTERVAL = 10000L;

    /** Connection timeout in miliseconds. */
    private static final int TIMEOUT = 3000;

    /** The log. */
    private static final Logger LOG = Logger.getLogger(ConnectionChecker.class);

    /** The connection states. */
    private enum ConnectionState {
        CONNECTED,
        DISCONNECTED,
    };

    /** The current connection state. */
    private ConnectionState state;

    /**
     * Application entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        new ConnectionChecker().run();
    }

    /**
     * Constructor.
     */
    public void run() {
        HttpURLConnection con = null;
        try {
            URL url = new URL(URL);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(TIMEOUT);
        } catch (IOException e ) {
            System.err.println("ERROR: Invalid URL: " + URL);
            System.exit(1);
        }
        
        while (true) {
            try {
                con.connect();
                con.disconnect();
                if (state != ConnectionState.CONNECTED) {
                    state = ConnectionState.CONNECTED;
                    LOG.info("Connected");
                }
            } catch (IOException e) {
                if (state != ConnectionState.DISCONNECTED) {
                    state = ConnectionState.DISCONNECTED;
                    LOG.info("Disconnected");
                }
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
    }

}
