package org.ozsoft.secs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.Data;
import org.ozsoft.secs.format.L;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;
import org.ozsoft.secs.message.ControlMessage;
import org.ozsoft.secs.message.DataMessage;
import org.ozsoft.secs.message.Message;
import org.ozsoft.secs.message.MessageParser;

public class SecsServer implements Runnable {
    
    private static final int SOCKET_TIMEOUT = 100;
    
    private static final long POLL_INTERVAL = 10L;
    
    private static final int BUFFER_SIZE = 8192;
    
    private static final String MDLN = "SecsServer";
    
    private static final String SOFTREV = "1.0";
    
    private static final Logger LOG = Logger.getLogger(SecsServer.class);
    
    private final int port;
    
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
                        if (requestMessage instanceof DataMessage) {
                            DataMessage dataMessage = (DataMessage) requestMessage;
                            int stream = dataMessage.getStream();
                            int function = dataMessage.getFunction();
                            Data<?> requestText = dataMessage.getText();
                            if (stream == 1 && function == 13) {
                                // Establish Communication Request.
                                if (!(requestText instanceof L)) {
                                    throw new SecsException("Invalid data format for S1F13 message");
                                }
                                L l = (L) requestText;
                                String mdln = null;
                                String softrev = null;
                                if (l.length() == 0) {
                                    // No MDLN and SOFTREV specified.
                                    mdln = "";
                                    softrev = "";
                                } else if (l.length() == 2) {
                                    Data<?> dataItem = l.getItem(0);
                                    if (!(dataItem instanceof A)) {
                                        throw new SecsException("Invalid data format for S1F13 message");
                                    }
                                    mdln = ((A) dataItem).getValue();
                                    dataItem = l.getItem(1);
                                    if (!(dataItem instanceof A)) {
                                        throw new SecsException("Invalid data format for S1F13 message");
                                    }
                                    softrev = ((A) dataItem).getValue();
                                } else {
                                    throw new SecsException("Invalid data format for S1F13 message");
                                }
                                LOG.debug(String.format("MDLN = '%s'", mdln));
                                LOG.debug(String.format("SOFTREV = '%s'", softrev));
                                
                                // Send S1F14 Establish Communication Request Acknowledge (CRA).
                                L replyText = new L();
                                replyText.addItem(new A(MDLN));
                                replyText.addItem(new A(SOFTREV));
                                Message replyMessage = new DataMessage(sessionId, (byte) 1, (byte) 14, PType.SECS_II, SType.DATA, systemBytes, replyText);
                                LOG.debug("Reply message: " + replyMessage);
                                LOG.debug("Reply message: " + replyMessage.toB());
                                os.write(replyMessage.toB().toByteArray());
                                os.flush();
                            }
                        } else {
                            // Control message.
                            switch (sType) {
                                case SELECT_REQ:
                                    byte headerByte3 = (connectionState == ConnectionState.NOT_SELECTED) ? (byte) 0 : (byte) 1;
                                    Message replyMessage = new ControlMessage(sessionId, (byte) 0x00, headerByte3, PType.SECS_II, SType.SELECT_RSP, systemBytes);
                                    LOG.debug("Reply message: " + replyMessage);
                                    LOG.debug("Reply message: " + replyMessage.toB());
                                    os.write(replyMessage.toB().toByteArray());
                                    os.flush();
                                    break;
                                case DESELECT_REQ:
                                    //TODO: Handle DESELECT_REQ.
                                    break;
                                case SEPARATE:
                                    disconnect();
                                    is.close();
                                    os.close();
                                    clientSocket.close();
                                    break;
                                case LINKTEST_REQ:
                                    //TODO: Handle LINKTEST_REQ.
                                    break;
                                case REJECT:
                                    //TODO: Handle REJECT.
                                    break;
                                default:
                                    LOG.error("Unsupported control message type: " + sType);
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
    
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }
    
}
