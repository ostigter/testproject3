package org.ozsoft.secs;

/**
 * SECS Control State.
 * 
 * @author Oscar Stigter
 */
public enum ControlState {
    
    /** Offline as controlled by the equipment. */
    EQUIPMENT_OFFLINE,
    
    /** Offline, but attempting to get online. */
    ATTEMPT_ONLINE,
    
    /** Offline as requested by the host. */ 
    HOST_OFFLINE,
    
    /** Online, but only locally controllable. */
    ONLINE_LOCAL,
    
    /** Online and fully remotely controllable. */
    ONLINE_REMOTE,

}
