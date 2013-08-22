package org.ozsoft.secs;

/**
 * HSMS Connection State.
 * 
 * @author Oscar Stigter
 */
public enum ConnectionState {
    
    /** NOT CONNECTED. */
    NOT_CONNECTED,
    
    /** CONNECTED > NOT SELECTED. */
    NOT_SELECTED,
    
    /** CONNECTED > SELECTED. */
    SELECTED,

}
