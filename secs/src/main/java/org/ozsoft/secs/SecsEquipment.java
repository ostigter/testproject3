package org.ozsoft.secs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
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

/**
 * SECS/GEM equipment. <br />
 * <br />
 * 
 * This is the core class of the SECS/GEM library.
 * 
 * @author Oscar Stigter
 */
public class SecsEquipment {

    private static final int MIN_PORT = 1025;

    private static final int MAX_PORT = 65535;

    private static final long POLL_INTERVAL = 10L;

    private static final int BUFFER_SIZE = 8192;

    private static final Logger LOG = Logger.getLogger(SecsEquipment.class);

    private final Map<Integer, MessageHandler> messageHandlers;

    private final Set<SecsEquipmentListener> listeners;

    private String host = SecsConstants.DEFAULT_HOST;

    private int port = SecsConstants.DEFAULT_PORT;

    private boolean isActive = SecsConstants.IS_ACTIVE;

    private boolean isEnabled;

    private ConnectionState connectionState;

    private CommunicationState communicationState;

    private ControlState controlState;

    private Thread connectionThread;
    
    private Socket socket;
    
    public SecsEquipment() {
        listeners  = new HashSet<SecsEquipmentListener>();
        messageHandlers = new HashMap<Integer, MessageHandler>();
        
        addDefaultMessageHandlers();
        
        isEnabled = false;
        setConnectionState(ConnectionState.NOT_CONNECTED);
        setCommunicationState(CommunicationState.NOT_ENABLED);
        setControlState(ControlState.EQUIPMENT_OFFLINE);
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
        LOG.info("Connection Mode set to " + activeState);
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        if (this.connectionState != connectionState) {
            this.connectionState = connectionState;
            for (SecsEquipmentListener listener : listeners) {
                listener.connectionStateChanged(connectionState);
            }
            LOG.info("Connection State set to " + connectionState);
        }
    }

    public CommunicationState getCommunicationState() {
        return communicationState;
    }

    public void setCommunicationState(CommunicationState communicationState) {
        if (this.communicationState != communicationState) {
            this.communicationState = communicationState;
            LOG.info("Communication State set to " + communicationState);
            for (SecsEquipmentListener listener : listeners) {
                listener.communicationStateChanged(communicationState);
            }
        }
    }

    public ControlState getControlState() {
        return controlState;
    }

    public void setControlState(ControlState controlState) {
        if (this.controlState != controlState) {
            this.controlState = controlState;
            LOG.info("Control State set to " + controlState);
            for (SecsEquipmentListener listener : listeners) {
                listener.controlStateChanged(controlState);
            }
        }
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
    
    public void addListener(SecsEquipmentListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(SecsEquipmentListener listener) {
        listeners.remove(listener);
    }
    
    public void sendMessage(Message message) throws SecsException {
        if (communicationState != CommunicationState.COMMUNICATING) {
            throw new SecsException("Communication State not COMMUNICATING");
        }
        
        try {
            sendMessage(message, socket.getOutputStream());
        } catch (IOException e) {
            // Internal error (should never happen).
            String msg = "Could not send message: " + e.getMessage(); 
            LOG.error(msg);
            throw new SecsException(msg, e);
        }
    }
    
    private void sendMessage(Message message, OutputStream os) throws IOException {
        LOG.trace(String.format("Send message %s", message));
        os.write(message.toByteArray());
        os.flush();
        for (SecsEquipmentListener listener : listeners) {
            listener.messageSent(message);
        }
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
        if (connectionState != ConnectionState.NOT_CONNECTED) {
            U2 sessionId = new U2(1);
            U4 systemBytes = new U4(99999L);
            
            Message message = new ControlMessage(sessionId, 0x00, 0x00, PType.SECS_II, SType.SEPARATE, systemBytes);
            try {
                OutputStream os = socket.getOutputStream();
                os.write(message.toByteArray());
                os.flush();
            } catch (IOException e) {
                // Internal error (should never happen).
                LOG.error("Could not send SEPARATE message", e);
            }
        }

        isEnabled = false;
        while (communicationState != CommunicationState.NOT_COMMUNICATING) {
            sleep(POLL_INTERVAL);
        }
        
        setCommunicationState(CommunicationState.NOT_ENABLED);
    }

    private void addDefaultMessageHandlers() {
        addMessageHandler(new S1F1(this)); // Are You There (R)
        addMessageHandler(new S1F13(this)); // Establish Communication Request
                                            // (CR)
        addMessageHandler(new S1F14(this)); // Establish Communication
                                            // Acknowledge (CRA)
        addMessageHandler(new S1F15(this)); // Request OFF-LINE (ROFL)
        addMessageHandler(new S1F17(this)); // Request ON-LINE (RONL)
        addMessageHandler(new S2F25(this)); // Request Loopback Diagnostic
                                            // Request (LDR)
    }

    private void handleConnection() {
        String clientHost = socket.getInetAddress().getHostName();
        LOG.info(String.format("Connected with host '%s'", clientHost));
        setConnectionState(ConnectionState.NOT_SELECTED);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            byte[] buf = new byte[BUFFER_SIZE];
            while (isEnabled && getConnectionState() != ConnectionState.NOT_CONNECTED) {
                if (isActive && connectionState == ConnectionState.NOT_SELECTED) {
                    // Not selected; send SELECT_REQ.
                    // FIXME: Use unique session ID and system bytes.
                    U2 sessionId = new U2(1);
                    U4 systemBytes = new U4(1L);
                    Message message = new ControlMessage(sessionId, 0x00, 0x00, PType.SECS_II, SType.SELECT_REQ, systemBytes);
                    sendMessage(message, os);
                    sleep(100L);
                } else if (isActive && connectionState == ConnectionState.SELECTED && communicationState == CommunicationState.NOT_COMMUNICATING) {
                    // Not communicating; send S1F13.
                    // FIXME: Use sequentially, generated session ID and system bytes.
                    U2 sessionId = new U2(1);
                    U4 systemBytes = new U4(3L);
                    L text = new L();
                    // FIXME: Use actual model name and software revision.
                    text.addItem(new A(SecsConstants.DEFAULT_MDLN));
                    text.addItem(new A(SecsConstants.DEFAULT_SOFTREV));
                    Message message = new DataMessage(sessionId, 1, 13, PType.SECS_II, SType.DATA, systemBytes, text);
                    sendMessage(message, os);
                    sleep(100L);
                }
                if (is.available() > 0) {
                    int length = is.read(buf);
                    try {
                        Message requestMessage = MessageParser.parse(buf, length);
                        LOG.trace(String.format("Received message: %s", requestMessage));
                        Message replyMessage = handleMessage(requestMessage);
                        if (replyMessage != null) {
                            sendMessage(replyMessage, os);
                        }
                    } catch (SecsException e) {
                        LOG.error("Received invalid SECS message: " + e.getMessage());
                    }
                } else {
                    sleep(POLL_INTERVAL);
                }
            }
        } catch (Exception e) {
            // Internal error (should never happen).
            LOG.error("Internal error while handling connection: " + e.getMessage());
        }

        // Disconnect.
        IOUtils.closeQuietly(os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(socket);
        disconnect();
    }

    private Message handleMessage(Message requestMessage) throws SecsException {
        U2 sessionId = requestMessage.getSessionId();
        SType sType = requestMessage.getSType();
        U4 systemBytes = requestMessage.getSystemBytes();

        Message replyMessage = null;

        if (requestMessage instanceof ControlMessage) {
            // HSMS control message.
            switch (sType) {
                case SELECT_REQ:
                    // Always accept SELECT_REQ.
                    int headerByte3 = -1;
                    if (getConnectionState() == ConnectionState.NOT_SELECTED) {
                        headerByte3 = 0x00; // SelectStatus: Communication Established
                        setConnectionState(ConnectionState.SELECTED);
                    } else {
                        headerByte3 = 0x01; // SelectStatus: Communication Already Active
                    }
                    replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, PType.SECS_II, SType.SELECT_RSP, systemBytes);
                    break;

                case SELECT_RSP:
                    // Always accept SELECT_RSP; no action required.
                    int selectStatus = requestMessage.getHeaderByte3();
                    if (selectStatus == 0x00) { // SelectStatus: Communication Established
                        LOG.debug("Received SELECT_RSP message with SelectStatus: Communication Established");
                        setConnectionState(ConnectionState.SELECTED);
                    } else if (selectStatus == 0x01) { // SelectStatus: Communication Already Active
                        LOG.debug("Received SELECT_RSP message with SelectStatus: Communication Already Active");
                        setConnectionState(ConnectionState.SELECTED);
                    } else if (selectStatus == 0x02) { // SelectStatus: Connection Not Ready
                        LOG.warn("Received SELECT_RSP message with SelectStatus: Connection Not Ready -- Communication failed");
                        setConnectionState(ConnectionState.NOT_SELECTED);
                    } else if (selectStatus == 0x03) { // SelectStatus: Connect Exhaust
                        LOG.warn("Received SELECT_RSP message with SelectStatus: Connect Exhaust -- Communication failed");
                        setConnectionState(ConnectionState.NOT_SELECTED);
                    } else {
                        LOG.warn("Received SELECT_RSP message with invalid SelectStatus: " + selectStatus);
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
                        LOG.debug("Received DESELECT_RSP message with DeselectStatus: Success");
                        setConnectionState(ConnectionState.NOT_SELECTED);
                    } else {
                        LOG.debug("Received DESELECT_RSP message with DeselectStatus: Failed");
                    }
                    break;

                case SEPARATE:
                    // Immediately disconnect on SEPARATE message.
                    LOG.debug("Received SEPARATE message");
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
                    LOG.warn("Received REJECT message");
                    break;

                default:
                    LOG.error("Unsupported control message type: " + sType);
            }
        } else {
            // Data message (standard GEM or custom).
            DataMessage dataMessage = (DataMessage) requestMessage;
            int stream = dataMessage.getStream();
            int function = dataMessage.getFunction();

            if (function == 0) {
                // Received FxS0 (ABORT) message; nothing to do.
            } else {
                // Find handler for specific data message.
                MessageHandler handler = messageHandlers.get(stream * 256 + function);
                if (handler != null) {
                    LOG.trace("Handle data message " + handler);
                    replyMessage = handler.handle(dataMessage);
                } else {
                    // Unsupported message; send ABORT.
                    LOG.warn(String.format("Unsupported data message (%s) -- ABORT", dataMessage.getType()));
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

    /**
     * Thread to establish a TCP/IP connection to another equipment (ACTIVE
     * connection mode).
     * 
     * @author Oscar Stigter
     */
    private class ActiveConnectionThread extends Thread {

        @Override
        public void run() {
            while (isEnabled) {
                if (getCommunicationState() == CommunicationState.NOT_COMMUNICATING) {
                    LOG.debug(String.format("Connecting to equipment '%s' on port %d", host, port));
                    try {
                        socket = new Socket(host, port);
                        handleConnection();
                    } catch (IOException e) {
                        LOG.debug(String.format("Failed to connect to equipment '%s' on port %d", host, port));
                        SecsEquipment.sleep(SecsConstants.DEFAULT_T5_TIMEOUT);
                    }
                } else {
                    SecsEquipment.sleep(POLL_INTERVAL);
                }
            }
        }
    }

    /**
     * Thread to listen for incoming TCP/IP connections from another equipment
     * (PASSIVE connection mode).
     * 
     * @author Oscar Stigter
     */
    private class PassiveConnectionThread extends Thread {

        private static final int SOCKET_TIMEOUT = 100;

        @Override
        public void run() {
            LOG.info(String.format("Listening for incoming connections on port %d", port));
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(SOCKET_TIMEOUT);
                while (isEnabled) {
                    if (getCommunicationState() == CommunicationState.NOT_COMMUNICATING) {
                        try {
                            socket = serverSocket.accept();
                            handleConnection();
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
            } finally {
                IOUtils.closeQuietly(serverSocket);
            }
        }
    }

}
