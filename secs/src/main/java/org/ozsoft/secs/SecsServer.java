package org.ozsoft.secs;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

public class SecsServer implements Runnable {
    
    private static final int SOCKET_TIMEOUT = 100;
    
    private static final int BUFFER_SIZE = 8192;
    
    private static final int MESSAGE_LENGTH_LENGTH = 4;
    
    private static final int HEADER_LENGTH = 10;
    
    private static final int MAX_MESSAGE_LENGTH = 256 * 1024; // 256 kB
    
    private static final int POS_SESSIONID = 0;
    
    private static final int POS_HEADERBYTE2 = 2;
    
    private static final int POS_HEADERBYTE3 = 3;
    
    private static final int POS_PTYPE = 4;
    
    private static final int POS_STYPE = 5;
    
    private static final int POS_SYSTEMBYTES = 6;
    
    private static final byte PTYPE_SECSII = 0;

    private static final Logger LOG = Logger.getLogger(SecsServer.class);
    
    private final int port;
    
    private ServerSocket socket;
    
    private Thread thread;
    
    private boolean isEnabled;
    
    private boolean isCommunicating;
    
    public SecsServer() {
        this(SecsConstants.DEFAULT_PORT);
    }
    
    public SecsServer(int port) {
        this.port = port;
        isEnabled = false;
        isCommunicating = false;
    }
    
    public static void main(String[] args) {
        new SecsServer().start();
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public boolean isCommunicating() {
        return isCommunicating;
    }
    
    public void start() {
        if (isEnabled) {
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
        if (!isEnabled) {
            throw new IllegalStateException("Server not started");
        }
        
        isEnabled = false;
        thread.interrupt();
    }
    
    @Override
    public void run() {
        if (socket == null) {
            throw new IllegalStateException("Socket is null");
        }
        
        isEnabled = true;
        LOG.info(String.format("Server started, listening on port %d", port));
        while (isEnabled) {
            if (!isCommunicating) {
                try {
                    Socket clientSocket = socket.accept();
                    handleConnection(clientSocket);
                } catch (SocketTimeoutException e) {
                    // No connections yet, wait some more.
                } catch (IOException e) {
                    LOG.error("Socket connection error: " + e.getMessage());
                    isEnabled = false;
                }
            } else {
                sleep(SOCKET_TIMEOUT);
            }
        }
        thread = null;
        LOG.info("Server stopped");
    }
    
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }
    
    private void handleConnection(Socket clientSocket) {
        String clientHost = clientSocket.getInetAddress().getHostName();
        LOG.info(String.format("Connected with host '%s'", clientHost));
        try {
            InputStream is = clientSocket.getInputStream();
            byte[] message = new byte[BUFFER_SIZE];
            byte[] lengthField = new byte[MESSAGE_LENGTH_LENGTH];
            while (isEnabled) {
                if (is.available() > 0) {
                    int read = is.read(message);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < read; i++) {
                        sb.append(String.format("%02X ", message[i]));
                    }
                    LOG.debug(read + " bytes read: " + sb);
                    
                    if (read > MESSAGE_LENGTH_LENGTH) {
                        System.arraycopy(message, 0, lengthField, 0, MESSAGE_LENGTH_LENGTH);
                        int length = SecsUtils.toU4(lengthField);
                        LOG.debug("Message length: " + length);
                        if (length < HEADER_LENGTH) {
                            LOG.error("Incomplete message");
                        } else if (length > MAX_MESSAGE_LENGTH) {
                            LOG.error("Message too large");
                        } else {
                            length -= HEADER_LENGTH;
                            PType pType = PType.parse(message[MESSAGE_LENGTH_LENGTH + POS_PTYPE]);
                            LOG.debug("PType = " + pType);
                            if (pType != PType.SECS_II) {
                                LOG.error("Unsupported message protocol (PType is not SECS-II)");
                            }
                            SType sType = SType.parse(message[MESSAGE_LENGTH_LENGTH + POS_STYPE]);
                            LOG.debug("SType = " + sType);
                            
                            short sessionId = (short) (((short) (message[MESSAGE_LENGTH_LENGTH + POS_SESSIONID] & 0xFF)) << 8 | ((short) (message[HEADER_LENGTH + POS_SESSIONID + 1] & 0xFF)));
                            LOG.debug("Session ID = " + sessionId);
                        }
                    }
                    
                    sleep(10L);
                }
            }
        } catch (IOException e) {
            LOG.error("I/O error while reading from client connection: " + e.getMessage());
        }
    }
    
}
