package org.ozsoft.secs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.ozsoft.secs.format.A;
import org.ozsoft.secs.format.B;
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
    
    private static final String MDLN = "SECS Server";
    
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
                        if (requestMessage instanceof ControlMessage) {
                            switch (sType) {
                                case SELECT_REQ:
                                    byte headerByte3 = (connectionState == ConnectionState.NOT_SELECTED) ? (byte) 0x00 : (byte) 0x01;
                                    Message replyMessage = new ControlMessage(sessionId, 0x00, headerByte3, PType.SECS_II, SType.SELECT_RSP, systemBytes);
                                    LOG.debug("Reply message:    " + replyMessage);
                                    os.write(replyMessage.toByteArray());
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
                        } else {
                            // Data message.
                            DataMessage dataMessage = (DataMessage) requestMessage;
                            int stream = dataMessage.getStream();
                            int function = dataMessage.getFunction();
                            Data<?> requestText = dataMessage.getText();
                            
                            if (stream == 1 && function == 1) {
                                // S1F1 Are You There (R).
                                if (requestText != null) {
                                    throw new SecsException("Invalid data format for S1F1 message");
                                }
                                
                                // Send S1F2 On Line Data (D).
                                L replyText = new L();
                                replyText.addItem(new A(MDLN));
                                replyText.addItem(new A(SOFTREV));
                                Message replyMessage = new DataMessage(sessionId, 1, 2, PType.SECS_II, SType.DATA, systemBytes, replyText);
                                LOG.debug("Reply message:    " + replyMessage);
                                os.write(replyMessage.toByteArray());
                                os.flush();
                                
                            } else if (stream == 1 && function == 13) {
                                // F1S13 Establish Communication Request (CR).
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
                                replyText.addItem(new B(0x00)); // COMMACK = Accepted
                                l = new L();
                                l.addItem(new A(MDLN));
                                l.addItem(new A(SOFTREV));
                                replyText.addItem(l);
                                Message replyMessage = new DataMessage(sessionId, 1, 14, PType.SECS_II, SType.DATA, systemBytes, replyText);
                                LOG.debug("Reply message:    " + replyMessage);
                                os.write(replyMessage.toByteArray());
                                os.flush();
                                
                            } else if (stream == 1 && function == 15) {
                                // S1F17 Request OFF-LINE (ROFL).
                                if (requestText != null) {
                                    throw new SecsException("Invalid data format for S1F15 message");
                                }
                                
                                // Send S1F16 OFF-LINE Acknowledge (OFLA).
                                B replyText = new B(0x00); // OFLACK = OFF-LINE Acknowledge
                                Message replyMessage = new DataMessage(sessionId, 1, 16, PType.SECS_II, SType.DATA, systemBytes, replyText);
                                LOG.debug("Reply message:    " + replyMessage);
                                os.write(replyMessage.toByteArray());
                                os.flush();
                                
                            } else if (stream == 1 && function == 17) {
                                // S1F17 Request ON-LINE (RONL).
                                if (requestText != null) {
                                    throw new SecsException("Invalid data format for S1F17 message");
                                }
                                
                                // Send S1F18 ON-LINE Acknowledge (ONLA).
                                B replyText = new B(0x00); // ONLACK = ON-LINE Accepted
                                Message replyMessage = new DataMessage(sessionId, 1, 18, PType.SECS_II, SType.DATA, systemBytes, replyText);
                                LOG.debug("Reply message:    " + replyMessage);
                                os.write(replyMessage.toByteArray());
                                os.flush();

                            } else if (stream == 2 && function == 25) {
                                // S2F25 Loopback Diagnostic Request (LDR).
                                if (!(requestText instanceof B)) {
                                    throw new SecsException("Invalid data format for S2F25 message");
                                }
                                
                                // Send S2F26 Loopback Diagnostic Data (LDD).
                                Message replyMessage = new DataMessage(sessionId, 2, 26, PType.SECS_II, SType.DATA, systemBytes, requestText);
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
    
    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }
    
}
