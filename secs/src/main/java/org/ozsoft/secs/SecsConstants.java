package org.ozsoft.secs;

/**
 * Generic SECS constants.
 * 
 * @author Oscar Stigter
 */
public interface SecsConstants {
    
    /** Default model name. */
    String DEFAULT_MDLN = "SECS Equipment";
    
    /** Default software revision. */
    String DEFAULT_SOFTREV = "1.0";
    
    /** Default connect mode. */
    ConnectMode DEFAULT_CONNECT_MODE = ConnectMode.PASSIVE;
    
    /** Default host. */
    String DEFAULT_HOST = "localhost";

    /** Default port. */
    int DEFAULT_PORT = 5555;
    
    /** Default active state (true means active, false means passive). */
    boolean IS_ACTIVE = false;
    
    /** Default device ID (session ID). */
    int DEFAULT_DEVICE_ID = 1;
    
    /** T3 (Reply) timeout in miliseconds. */
    int DEFAULT_T3 = 120000; // 2 minutes
    
    /** T5 (Connect Separation) timeout in miliseconds. */
    int DEFAULT_T5 = 10000; // 10 seconds
    
    /** T6 (Control Transaction) timeout in miliseconds. */
    int DEFAULT_T6 = 5000; // 5 seconds
    
    /** T7 (Connect Idle) timeout in miliseconds. */
    int DEFAULT_T7 = 10000; // 10 seconds
    
    /** SECS header length in bytes. */
    int HEADER_LENGTH = 10;
    
}
