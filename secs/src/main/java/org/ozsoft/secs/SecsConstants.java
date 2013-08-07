package org.ozsoft.secs;

/**
 * SECS constants.
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
    
    /** T3 timeout in miliseconds. */
    long DEFAULT_T3_TIMEOUT = 120000;  // 2 minutes
    
    /** T5 timeout in miliseconds. */
    long DEFAULT_T5_TIMEOUT = 5000L; // 5 seconds
    
    /** T6 timeout in miliseconds. */
    long DEFAULT_T6_TIMEOUT = 120000L; // 2 minutes
    
    /** T7 timeout in miliseconds. */
    long DEFAULT_T7_TIMEOUT = 120000L; // 2 minutes
    
    int HEADER_LENGTH = 10;
    
}
