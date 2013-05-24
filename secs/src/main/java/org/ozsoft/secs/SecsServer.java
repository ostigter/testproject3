package org.ozsoft.secs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

public class SecsServer implements Runnable {
    
    private static final int SOCKET_TIMEOUT = 100;
    
    private static final Logger LOG = Logger.getLogger(SecsServer.class);
    
    private final int port;
    
    private ServerSocket socket;
    
    private Thread thread;
    
    private boolean isStarted;
    
    public SecsServer() {
        this(SecsConstants.DEFAULT_PORT);
    }
    
    public SecsServer(int port) {
        this.port = port;
        isStarted = false;
    }
    
    public boolean isStarted() {
        return isStarted;
    }
    
    public void start() {
        if (isStarted) {
            throw new IllegalStateException("Server already started");
        }

        LOG.debug("Starting server...");
        try {
            socket = new ServerSocket(port);
            socket.setSoTimeout(SOCKET_TIMEOUT);
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            LOG.error("Could not start server: " + e.getMessage());
        }
    }
    
    public void stop() {
        if (!isStarted) {
            throw new IllegalStateException("Server not started");
        }
        
        isStarted = false;
        thread.interrupt();
    }
    
    @Override
    public void run() {
        if (socket == null) {
            throw new IllegalStateException("Socket is null");
        }
        
        isStarted = true;
        LOG.info(String.format("Server started, listening on port %d", port));
        while (isStarted) {
            try {
                Socket clientSocket = socket.accept();
                handleConnection(clientSocket);
            } catch (SocketTimeoutException e) {
                // No connections yet, wait some more.
            } catch (IOException e) {
                LOG.error("Socket connection error: " + e.getMessage());
                isStarted = false;
            }
        }
        thread = null;
        LOG.info("Server stopped");
    }
    
    private void handleConnection(Socket clientSocket) {
        String clientHost = clientSocket.getInetAddress().getHostName();
        LOG.info(String.format("Connected with host '%s'", clientHost));
        
//        try {
//            socket.close();
//        } catch (IOException e) {
//            LOG.error("Could not close client connection: " + e.getMessage());
//        }
    }
    
}
