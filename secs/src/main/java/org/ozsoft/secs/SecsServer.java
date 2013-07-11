package org.ozsoft.secs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.message.ControlMessage;
import org.ozsoft.secs.message.DataMessage;
import org.ozsoft.secs.message.Message;
import org.ozsoft.secs.message.MessageHandler;
import org.ozsoft.secs.message.MessageParser;
import org.ozsoft.secs.message.S1F1;
import org.ozsoft.secs.message.S1F13;
import org.ozsoft.secs.message.S1F15;
import org.ozsoft.secs.message.S1F17;
import org.ozsoft.secs.message.S2F25;

public class SecsServer implements Runnable {
    
    private static final int SOCKET_TIMEOUT = 100;
    
    private static final long POLL_INTERVAL = 10L;
    
    private static final int BUFFER_SIZE = 8192;
    
    private static final Logger LOG = Logger.getLogger(SecsServer.class);
    
    private final int port;
    
    private final Map<Integer, MessageHandler> messageHandlers;
    
    private ServerSocket socket;
    
    private Thread thread;
    
    private boolean isStarted;
    
    private ConnectionState connectionState;
    
    private CommunicationState communicationState;
    
    private ControlState controlState;
    
    public SecsServer() {
        this(SecsConstants.DEFAULT_PORT);
    }
    
    public SecsServer(int port) {
        this.port = port;
        isStarted = false;
        connectionState = ConnectionState.NOT_CONNECTED;
        communicationState = CommunicationState.NOT_COMMUNICATING;
        controlState = ControlState.EQUIPMENT_OFFLINE;
        messageHandlers = new HashMap<Integer, MessageHandler>();
        addDefaultMessageHandlers();
    }
    
    public static void main(String[] args) {
        new SecsServer().start();
    }
    
    public ConnectionState getConnectionState() {
        return connectionState;
    }
    
    public CommunicationState getCommunicationState() {
        return communicationState;
    }
    
    public ControlState getControlState() {
        return controlState;
    }
    
    public void addMessageHandler(MessageHandler handler) {
        int messageId = handler.getStream() * 256 + handler.getFunction();
        messageHandlers.put(messageId, handler);
    }
    
    public void removeMessageHandler(MessageHandler handler) {
        int messageId = handler.getStream() * 256 + handler.getFunction();
        messageHandlers.remove(messageId);
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
            if (communicationState == CommunicationState.NOT_COMMUNICATING) {
                try {
                    Socket clientSocket = socket.accept();
                    connectionState = ConnectionState.NOT_SELECTED;
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
        InputStream is = null;
        OutputStream os = null;
        try {
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            while (connectionState != ConnectionState.NOT_CONNECTED) {
                if (is.available() > 0) {
                    int length = is.read(buf);
                    try {
                        Message requestMessage = MessageParser.parse(buf, length);
                        LOG.debug(String.format("Received message: %s", requestMessage));
                        U2 sessionId = requestMessage.getSessionId();
                        SType sType = requestMessage.getSType();
                        U4 systemBytes = requestMessage.getSystemBytes();
                        if (requestMessage instanceof ControlMessage) {
                            switch (sType) {
                                case SELECT_REQ:
                                    // Accept SELECT_REQ if not selected, otherwise reject.
                                    byte headerByte3 = (connectionState == ConnectionState.NOT_SELECTED) ? (byte) 0x00 : (byte) 0x01; // SelectStatus: Accept or Reject
                                    Message replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, PType.SECS_II, SType.SELECT_RSP, systemBytes);
                                    LOG.debug("Reply message:    " + replyMessage);
                                    os.write(replyMessage.toByteArray());
                                    os.flush();
                                    break;
                                case DESELECT_REQ:
                                    // Always acknowledge DESELECT_REQ message and immediately disconnect.
                                    headerByte3 = 0x00; // DeselectStatus: Success
                                    replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, PType.SECS_II, SType.DESELECT_RSP, systemBytes);
                                    LOG.debug("Reply message:    " + replyMessage);
                                    os.write(replyMessage.toByteArray());
                                    os.close();
                                    is.close();
                                    clientSocket.close();
                                    disconnect();
                                    break;
                                case SEPARATE:
                                    // Always immediately disconnect on SEPARATE message.
                                    is.close();
                                    os.close();
                                    clientSocket.close();
                                    disconnect();
                                    break;
                                case LINKTEST_REQ:
                                    replyMessage = new ControlMessage(sessionId, 0x00, 0x00, PType.SECS_II, SType.LINKTEST_RSP, systemBytes);
                                    LOG.debug("Reply message:    " + replyMessage);
                                    os.write(replyMessage.toByteArray());
                                    os.flush();
                                    break;
                                case REJECT:
                                    // Nothing to do.
                                    break;
                                default:
                                    LOG.error("Unsupported control message type: " + sType);
                            }
                        } else {
                            // Data message.
                            DataMessage dataMessage = (DataMessage) requestMessage;
                            int stream = dataMessage.getStream();
                            int function = dataMessage.getFunction();

                            DataMessage replyMessage = null;
                            
                            if (function == 0) {
                                // Received ABORT message.
                                // Nothing to do.
                            } else {
                                MessageHandler handler = messageHandlers.get(stream * 256 + function);
                                if (handler != null) {
                                    LOG.debug("Handle data message " + handler);
                                    replyMessage = handler.handle(dataMessage);
                                } else {
                                    // Unsupported message; send ABORT.
                                    LOG.debug(String.format("Unsupported data message (%s) -- ABORT", dataMessage.getType()));
                                    replyMessage = new DataMessage(sessionId, stream, 0, PType.SECS_II, SType.DATA, systemBytes, null);
                                }
                            }
                            if (replyMessage != null) {
                                LOG.debug("Reply message:    " + replyMessage);
                                os.write(replyMessage.toByteArray());
                                os.flush();
                            }
                        }
                    } catch (SecsException e) {
                        LOG.error("Received invalid SECS message: " + e.getMessage());
                    }
                } else {
                    sleep(POLL_INTERVAL);
                }
            }
        } catch (IOException e) {
            LOG.error("I/O error while reading from client connection: " + e.getMessage());
        }
    }
    
    private void disconnect() {
        connectionState = ConnectionState.NOT_CONNECTED;
        communicationState = CommunicationState.NOT_COMMUNICATING;
        LOG.info("Disconnected");
    }
    
    private void addDefaultMessageHandlers() {
        addMessageHandler(new S1F1());  // Are You There (R)
        addMessageHandler(new S1F13()); // Establish Communication Request (CR)
        addMessageHandler(new S1F15()); // Request OFF-LINE (ROFL)
        addMessageHandler(new S1F17()); // Request ON-LINE (RONL)
        addMessageHandler(new S2F25()); // Request Loopback Diagnostic Request (LDR)
    }
    
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }
    
}
