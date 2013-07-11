package org.ozsoft.secs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.ozsoft.secs.message.Message;
import org.ozsoft.secs.message.MessageParser;

public class SecsServer extends SecsEquipment implements Runnable {
    
    private static final int SOCKET_TIMEOUT = 100;
    
    private static final long POLL_INTERVAL = 10L;
    
    private static final Logger LOG = Logger.getLogger(SecsServer.class);
    
    private final int port;
    
    private ServerSocket socket;
    
    private Thread thread;
    
    private boolean isStarted;
    
    public SecsServer() {
        this(SecsConstants.DEFAULT_PORT);
    }
    
    public SecsServer(int port) {
        super();
        this.port = port;
        isStarted = false;
    }
    
    public static void main(String[] args) {
        new SecsServer().start();
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
            if (getCommunicationState() == CommunicationState.NOT_COMMUNICATING) {
                try {
                    Socket clientSocket = socket.accept();
                    handleConnection(clientSocket);
                } catch (SocketTimeoutException e) {
                    // No incoming connections, just continue waiting.
                } catch (IOException e) {
                    LOG.error("Socket connection error: " + e.getMessage());
                    disconnect();
                }
            } else {
                sleep(POLL_INTERVAL);
            }
        }
        thread = null;
        LOG.info("Server stopped");
    }
    
    private void handleConnection(Socket clientSocket) {
        String clientHost = clientSocket.getInetAddress().getHostName();
        LOG.info(String.format("Connected with host '%s'", clientHost));
        setConnectionState(ConnectionState.NOT_SELECTED);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            while (getConnectionState() != ConnectionState.NOT_CONNECTED) {
                if (is.available() > 0) {
                    int length = is.read(buf);
                    try {
                        Message requestMessage = MessageParser.parse(buf, length);
                        LOG.debug(String.format("Received message: %s", requestMessage));
                        Message replyMessage = handleMessage(requestMessage);
                        if (replyMessage != null) {
                            LOG.debug("Reply message:    " + replyMessage);
                            os.write(replyMessage.toByteArray());
                            os.flush();
                        }
                    } catch (SecsException e) {
                        LOG.error("Received invalid SECS message: " + e.getMessage());
                    }
                } else {
                    sleep(POLL_INTERVAL);
                }
            }
            
            // Disconnect.
            os.close();
            is.close();
            clientSocket.close();
            
        } catch (IOException e) {
            LOG.error("I/O error while reading from client connection: " + e.getMessage());
        }
    }
    
}
