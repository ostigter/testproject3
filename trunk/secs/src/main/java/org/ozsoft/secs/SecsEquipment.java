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
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.message.ControlMessage;
import org.ozsoft.secs.message.DataMessage;
import org.ozsoft.secs.message.Message;
import org.ozsoft.secs.message.MessageHandler;
import org.ozsoft.secs.message.MessageParser;
import org.ozsoft.secs.message.S1F1;
import org.ozsoft.secs.message.S1F13;
import org.ozsoft.secs.message.S1F14;
import org.ozsoft.secs.message.S1F15;
import org.ozsoft.secs.message.S1F17;
import org.ozsoft.secs.message.S2F25;

public class SecsEquipment {
    
    private static final int MIN_PORT = 1025;
    
    private static final int MAX_PORT = 65535;

    private static final long POLL_INTERVAL = 10L;
    
    private static final int BUFFER_SIZE = 8192;
    
    private static final Logger LOG = Logger.getLogger(SecsEquipment.class);
    
    private final Map<Integer, MessageHandler> messageHandlers;
    
    private String host = SecsConstants.DEFAULT_HOST;
    
    private int port = SecsConstants.DEFAULT_PORT;
    
    private boolean isActive = SecsConstants.IS_ACTIVE;
    
    private boolean isEnabled;
    
    private ConnectionState connectionState;
    
    private CommunicationState communicationState;
    
    private ControlState controlState;
    
    private Thread connectionThread;
    
    public SecsEquipment() {
        isEnabled = false;
        setConnectionState(ConnectionState.NOT_CONNECTED);
        setCommunicationState(CommunicationState.NOT_ENABLED);
        setControlState(ControlState.EQUIPMENT_OFFLINE);
        messageHandlers = new HashMap<Integer, MessageHandler>();
        addDefaultMessageHandlers();
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) throws SecsException {
        if (host == null || host.isEmpty()) {
            throw new SecsException(String.format("Invalid host: '%s'", host));
        }
        this.host = host;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) throws SecsException {
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new SecsException("Invalid port number: " + port);
        }
        this.port = port;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        String activeState = (isActive) ? "ACTIVE" : "PASSIVE";
        LOG.debug("Connection Mode set to " + activeState);
    }
    
    public ConnectionState getConnectionState() {
        return connectionState;
    }
    
    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
        LOG.debug("Connection State set to " + connectionState);
    }
    
    public CommunicationState getCommunicationState() {
        return communicationState;
    }
    
    public void setCommunicationState(CommunicationState communicationState) {
        this.communicationState = communicationState;
        LOG.debug("Communication State set to " + communicationState);
    }
    
    public ControlState getControlState() {
        return controlState;
    }
    
    public void setControlState(ControlState controlState) {
        this.controlState = controlState;
        LOG.debug("Control State set to " + controlState);
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) throws SecsException {
        if (isEnabled) {
            // Enable equipment.
            if (!isEnabled()) {
                enable();
            } else {
                throw new SecsException("Already enabled");
            }
        } else {
            // Disable equipment.
            if (isEnabled()) {
                disable();
            } else {
                throw new SecsException("Already disabled");
            }
        }
    }
    
    public void addMessageHandler(MessageHandler handler) {
        int messageId = handler.getStream() * 256 + handler.getFunction();
        messageHandlers.put(messageId, handler);
    }
    
    public void removeMessageHandler(MessageHandler handler) {
        int messageId = handler.getStream() * 256 + handler.getFunction();
        messageHandlers.remove(messageId);
    }
    
    private void enable() {
        isEnabled = true;
        setCommunicationState(CommunicationState.NOT_COMMUNICATING);
        if (isActive) {
            // Active mode; establish HSMS connection (client).
            connectionThread = new ActiveConnectionThread();
        } else {
            // Passive mode; accept incoming HSMS connection (server).
            connectionThread = new PassiveConnectionThread();
        }
        connectionThread.start();
    }
    
    private void disable() {
        disconnect();
        setCommunicationState(CommunicationState.NOT_ENABLED);
        isEnabled = false;
    }
    
    private void addDefaultMessageHandlers() {
        addMessageHandler(new S1F1(this));  // Are You There (R)
        addMessageHandler(new S1F13(this)); // Establish Communication Request (CR)
        addMessageHandler(new S1F14(this)); // Establish Communication Acknowledge (CRA)
        addMessageHandler(new S1F15(this)); // Request OFF-LINE (ROFL)
        addMessageHandler(new S1F17(this)); // Request ON-LINE (RONL)
        addMessageHandler(new S2F25(this)); // Request Loopback Diagnostic Request (LDR)
    }
    
    private void handleConnection(Socket socket) {
        String clientHost = socket.getInetAddress().getHostName();
        LOG.info(String.format("Connected with host '%s'", clientHost));
        setConnectionState(ConnectionState.NOT_SELECTED);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            while (getConnectionState() != ConnectionState.NOT_CONNECTED) {
                if (isActive && connectionState == ConnectionState.NOT_SELECTED) {
                    // Not selected; send SELECT_REQ.
                    LOG.debug("Send SELECT_REQ message");
                    //FIXME: Use sequentially, generated session ID and system bytes.
                    U2 sessionId = new U2(1);
                    U4 systemBytes = new U4(1L);
                    Message message = new ControlMessage(sessionId, 0x00, 0x00, PType.SECS_II, SType.SELECT_REQ, systemBytes);
                    os.write(message.toByteArray());
                    os.flush();
                }
                if (isActive && connectionState == ConnectionState.SELECTED && communicationState == CommunicationState.NOT_COMMUNICATING) {
                    // Not communicating; send S1F13.
                    LOG.debug("Send S1F13 (Establish Communication Request) message");
                    //FIXME: Use sequentially, generated session ID and system bytes.
                    U2 sessionId = new U2(1);
                    U4 systemBytes = new U4(3L);
                    L text = new L();
                    //FIXME: Use actual model name and software revision.
                    text.addItem(new A(SecsConstants.DEFAULT_MDLN));
                    text.addItem(new A(SecsConstants.DEFAULT_SOFTREV));
                    Message message = new DataMessage(sessionId, 1, 13, PType.SECS_II, SType.DATA, systemBytes, text);
                    os.write(message.toByteArray());
                    os.flush();
                }
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
            socket.close();
            
        } catch (IOException e) {
            LOG.error("I/O error while reading from client connection: " + e.getMessage());
        }
    }
    
    private Message handleMessage(Message requestMessage) throws SecsException {
        U2 sessionId = requestMessage.getSessionId();
        SType sType = requestMessage.getSType();
        U4 systemBytes = requestMessage.getSystemBytes();
        
        Message replyMessage = null;
        
        if (requestMessage instanceof ControlMessage) {
            switch (sType) {
                case SELECT_REQ:
                    // Accept SELECT_REQ if not selected, otherwise reject.
                    int headerByte3 = -1;
                    if (getConnectionState() == ConnectionState.NOT_SELECTED) {
                        headerByte3 = 0x00; // SelectStatus: Accept
                        setConnectionState(ConnectionState.SELECTED);
                    } else {
                        headerByte3 = 0x01; // SelectStatus: Reject
                    }
                    replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, PType.SECS_II, SType.SELECT_RSP, systemBytes);
                    break;
                    
                case SELECT_RSP:
                    // Always accept SELECT_RSP; no action required.
                    int selectStatus = requestMessage.getHeaderByte3();
                    if (selectStatus == 0x00) { // Accept
                        LOG.debug("Received SELECT_RSP with SelectStatus: Accept");
                        setConnectionState(ConnectionState.SELECTED);
                    } else {
                        LOG.debug("Received SELECT_RSP with SelectStatus: Reject");
                    }
                    break;
                    
                case DESELECT_REQ:
                    // Acknowledge DESELECT_REQ if selected, otherwise fail.
                    if (getConnectionState() == ConnectionState.SELECTED) {
                        headerByte3 = 0x00; // DeselectStatus: Success
                        setConnectionState(ConnectionState.NOT_SELECTED);
                    } else {
                        headerByte3 = 0x01; // DeselectStatus: Failed
                    }
                    replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, PType.SECS_II, SType.DESELECT_RSP, systemBytes);
                    break;
                    
                case DESELECT_RSP:
                    int deselectStatus = requestMessage.getHeaderByte3();
                    if (deselectStatus == 0x00) { // Accept
                        LOG.debug("Received DESELECT_RSP with DeselectStatus: Accept");
                        setConnectionState(ConnectionState.NOT_SELECTED);
                    } else {
                        LOG.debug("Received DESELECT_RSP with DeselectStatus: Reject");
                    }
                    break;
                    
                case SEPARATE:
                    // Immediately disconnect on SEPARATE message.
                    disconnect();
                    break;
                    
                case LINKTEST_REQ:
                    // Send LINKTEST_RSP message.
                    replyMessage = new ControlMessage(sessionId, 0x00, 0x00, PType.SECS_II, SType.LINKTEST_RSP, systemBytes);
                    break;
                    
                case LINKTEST_RSP:
                    // Always accept LINKTEST_RSP; no action required.
                    break;
                    
                case REJECT:
                    // Always accept REJECT; no action required.
                    break;
                    
                default:
                    LOG.error("Unsupported control message type: " + sType);
            }
        } else {
            // Data message.
            DataMessage dataMessage = (DataMessage) requestMessage;
            int stream = dataMessage.getStream();
            int function = dataMessage.getFunction();
            
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
        }
        
        return replyMessage;
    }
    
    private void disconnect() {
        setCommunicationState(CommunicationState.NOT_COMMUNICATING);
        setConnectionState(ConnectionState.NOT_CONNECTED);
        connectionThread.interrupt();
        LOG.info("Disconnected");
    }
    
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }
    
    private class ActiveConnectionThread extends Thread {
        
        @Override
        public void run() {
            while (isEnabled) {
                if (getCommunicationState() == CommunicationState.NOT_COMMUNICATING) {
                    LOG.debug("Connecting...");
                    try {
                        Socket socket = new Socket(host, port);
                        handleConnection(socket);
                    } catch (IOException e) {
                        LOG.debug(String.format("Failed to connect to equipment '%s' on port %d", host, port));
                        SecsEquipment.sleep(POLL_INTERVAL);
                    }
                } else {
                    SecsEquipment.sleep(POLL_INTERVAL);
                }
            }
        }
        
    } // ActiveThread
    
    private class PassiveConnectionThread extends Thread {
        
        private static final int SOCKET_TIMEOUT = 100;
        
        @Override
        public void run() {
            LOG.debug(String.format("Listening for incoming connections on port %d", port));
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(SOCKET_TIMEOUT);
                while (isEnabled) {
                    if (getCommunicationState() == CommunicationState.NOT_COMMUNICATING) {
                        try {
                            Socket socket = serverSocket.accept();
                            handleConnection(socket);
                        } catch (SocketTimeoutException e) {
                            // No incoming connections, just continue waiting.
                        } catch (IOException e) {
                            LOG.error("Socket connection error: " + e.getMessage());
                            disconnect();
                        }
                    } else {
                        SecsEquipment.sleep(POLL_INTERVAL);
                    }
                }
            } catch (IOException e) {
                LOG.error("Could not start server: " + e.getMessage());
            }
        }
        
    } // ServerThread
    
}
