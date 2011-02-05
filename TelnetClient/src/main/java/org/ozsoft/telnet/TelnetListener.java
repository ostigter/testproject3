package org.ozsoft.telnet;

/**
 * Telnet client listener.
 * 
 * @author Oscar Stigter
 */
public interface TelnetListener {
    
    void connected();
    
    void disconnected();
    
    void textReceived(String text);
    
    void ansiCodeReceived(String code);
    
    void telnetExceptionCaught(Throwable t);
    
}
