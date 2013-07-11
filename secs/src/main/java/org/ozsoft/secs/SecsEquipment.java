package org.ozsoft.secs;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.message.ControlMessage;
import org.ozsoft.secs.message.DataMessage;
import org.ozsoft.secs.message.Message;
import org.ozsoft.secs.message.MessageHandler;
import org.ozsoft.secs.message.S1F1;
import org.ozsoft.secs.message.S1F13;
import org.ozsoft.secs.message.S1F15;
import org.ozsoft.secs.message.S1F17;
import org.ozsoft.secs.message.S2F25;

public abstract class SecsEquipment {

    protected static final int BUFFER_SIZE = 8192;
    
    private static final Logger LOG = Logger.getLogger(SecsEquipment.class);
    
    private final Map<Integer, MessageHandler> messageHandlers;
    
    private ConnectionState connectionState;
    
    private CommunicationState communicationState;
    
    private ControlState controlState;
    
    public SecsEquipment() {
        setConnectionState(ConnectionState.NOT_CONNECTED);
        setCommunicationState(CommunicationState.NOT_COMMUNICATING);
        setControlState(ControlState.EQUIPMENT_OFFLINE);
        messageHandlers = new HashMap<Integer, MessageHandler>();
        addDefaultMessageHandlers();
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
    
    public void addMessageHandler(MessageHandler handler) {
        int messageId = handler.getStream() * 256 + handler.getFunction();
        messageHandlers.put(messageId, handler);
    }
    
    public void removeMessageHandler(MessageHandler handler) {
        int messageId = handler.getStream() * 256 + handler.getFunction();
        messageHandlers.remove(messageId);
    }
    
    protected Message handleMessage(Message requestMessage) throws SecsException {
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
                    
                case SEPARATE:
                    // Immediately disconnect on SEPARATE message.
                    disconnect();
                    break;
                    
                case LINKTEST_REQ:
                    // Send LINKTEST_RSP message.
                    replyMessage = new ControlMessage(sessionId, 0x00, 0x00, PType.SECS_II, SType.LINKTEST_RSP, systemBytes);
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
    
    protected void disconnect() {
        setCommunicationState(CommunicationState.NOT_COMMUNICATING);
        setConnectionState(ConnectionState.NOT_CONNECTED);
        LOG.info("Disconnected");
    }
    
    protected static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }
    
    private void addDefaultMessageHandlers() {
        addMessageHandler(new S1F1(this));  // Are You There (R)
        addMessageHandler(new S1F13(this)); // Establish Communication Request (CR)
        addMessageHandler(new S1F15(this)); // Request OFF-LINE (ROFL)
        addMessageHandler(new S1F17(this)); // Request ON-LINE (RONL)
        addMessageHandler(new S2F25(this)); // Request Loopback Diagnostic Request (LDR)
    }
    
}
