package org.ozsoft.portal.server;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ozsoft.encryption.EncryptionException;
import org.ozsoft.encryption.Encryptor;
import org.ozsoft.telnet.TelnetClient;
import org.ozsoft.telnet.TelnetListener;

/**
 * Portal servlet.
 * 
 * @author Oscar Stigter
 */
public class PortalServlet extends HttpServlet implements TelnetListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 4463827583543771031L;
    
    /** Shared key used for AES encryption. */
    private static final String ENCRYPTION_KEY = "F5iQ!w6#pYm&wB";
    
    /** NewLine character. */
    private static final String NEWLINE = "\r\n";
    
    /** Telnet client. */
    private final TelnetClient telnetClient;
    
    /** Buffer with text received from the Telnet server. */
    private final StringBuilder receivedText;
    
    /** The encryptor. */
    private Encryptor encryptor; 
    
    /** Whether currently connected to a Telnet server. */
    private boolean isConnected = false;
    
    /** The hostname of the currently connected Telnet server. */
    private String host;
    
    /** The port of the currently connected Telnet server. */
    private int port;
    
    /**
     * Constructs the portal servlet.
     */
    public PortalServlet() {
        telnetClient = new TelnetClient();
        telnetClient.addTelnetListener(this);
        
        receivedText = new StringBuilder();
        
        try {
            encryptor = new Encryptor();
            encryptor.setKey(ENCRYPTION_KEY);
        } catch (EncryptionException e) {
            // This should never happen.
            throw new RuntimeException("ERROR: Could not initialize encryptor", e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get request.
        BufferedReader reader = request.getReader();
        String requestBody = reader.readLine();
        String responseBody = null;
        if (requestBody == null || requestBody.length() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody = "Missing or empty command";
        } else {
            try {
                requestBody = encryptor.decrypt(requestBody);
                requestBody = requestBody.split("\n")[0];
                if (requestBody.equals("RECEIVE")) {
                    if (isConnected) {
                        synchronized (receivedText) {
                            responseBody = receivedText.toString();
                            receivedText.delete(0, receivedText.length());
                        }
                    }
                } else if (requestBody.startsWith("SEND ")) {
                    if (isConnected) {
                        telnetClient.sendText(requestBody.substring(5) + NEWLINE);
                    }
                } else if (requestBody.equals("STATUS")) {
                    if (isConnected) {
                        responseBody = String.format("CONNECTED %s %d", host, port);
                    } else {
                        responseBody = "DISCONNECTED";
                    }
                } else if (requestBody.startsWith("CONNECT ")) {
                    String[] tokens = requestBody.split(" ");
                    if (tokens.length == 3) {
                        host = tokens[1];
                        try {
                            port = Integer.parseInt(tokens[2]);
                        } catch (NumberFormatException e) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            responseBody = String.format("Invalid port in CONNECT command: '%s'", requestBody);
                        }
                        telnetClient.connect(host, port);
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        responseBody = String.format("Invalid CONNECT command: '%s'", requestBody);
                    }
                } else if (requestBody.equals("DISCONNECT")) {
                    telnetClient.disconnect();
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    responseBody = String.format("Unknown command: '%s'", requestBody);
                }
            } catch (EncryptionException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        
        if (responseBody != null) {
            try {
                responseBody = encryptor.encrypt(responseBody);
                response.setContentType("text/plain");
                response.getWriter().write(responseBody);
            } catch (EncryptionException e) {
                System.err.println("PortalServlet: ERROR: Could not encrypt response body: " + e);
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
//        telnetClient.disconnect();
//        telnetClient.removeTelnetListener(this);
        telnetClient.shutdown();
        receivedText.delete(0, receivedText.length());
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#connected()
     */
    @Override
    public void connected() {
        isConnected = true;
        receivedText.delete(0, receivedText.length());
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#disconnected()
     */
    @Override
    public void disconnected() {
        isConnected = false;
        receivedText.delete(0, receivedText.length());
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#textReceived(java.lang.String)
     */
    @Override
    public void textReceived(String text) {
        synchronized (receivedText) {
            receivedText.append(text);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#ansiCodeReceived(java.lang.String)
     */
    @Override
    public void ansiCodeReceived(String code) {
        // Not implemented.
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.telnet.TelnetListener#telnetExceptionCaught(java.lang.Throwable)
     */
    @Override
    public void telnetExceptionCaught(Throwable t) {
        System.err.println("PortalServlet: TelnetException: " + t);
    }

}
