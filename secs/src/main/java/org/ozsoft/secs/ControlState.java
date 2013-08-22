package org.ozsoft.secs;

/**
 * SECS Control State.
 * 
 * @author Oscar Stigter
 */
public enum ControlState {
    
    /** OFFLINE > EQUIPMENT OFFLINE. */
    EQUIPMENT_OFFLINE,
    
    /** OFFLINE > ATTEMPT ONLINE. */
    ATTEMPT_ONLINE,
    
    /** OFFLINE > HOST OFFLINE. */
    HOST_OFFLINE,
    
    /** ONLINE > ONLINE LOCAL. */
    ONLINE_LOCAL,
    
    /** ONLINE > ONLINE REMOTE. */
    ONLINE_REMOTE,

}
