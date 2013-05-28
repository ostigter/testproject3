package org.ozsoft.secs;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

public class SecsClient {
    
    private static final Logger LOG = Logger.getLogger(SecsClient.class);
    
    private final String host;
    
    private final int port;
    
    private Socket socket;
    
    public SecsClient(String host) {
        this(host, SecsConstants.DEFAULT_PORT);
    }
    
    public SecsClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void connect() {
        LOG.debug("Connecting...");
        try {
            socket = new Socket(host, port);
            LOG.info(String.format("Connected to server '%s' on port %d", host, port));
        } catch (IOException e) {
            LOG.error(String.format("Could not connect to server '%s' on port %d", host, port));
        }
    }
    
    public void disconnect() {
        if (socket == null) {
            throw new IllegalStateException("Not connected");
        }
        
        try {
            socket.close();
            LOG.info("Disconnected from server");
        } catch (IOException e) {
            LOG.error("Could not properly disconnect from server: " + e.getMessage());
        } finally {
            socket = null;
        }
    }

}
