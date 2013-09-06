package org.ozsoft.secs;

/**
 * HSMS Connection State.
 * 
 * @author Oscar Stigter
 */
public enum ConnectionState {
    
    /** Not connected. */
    NOT_CONNECTED,
    
    /** Connected, but not yet selected. */
    NOT_SELECTED,
    
    /** Connected and selected. */
    SELECTED,

}
