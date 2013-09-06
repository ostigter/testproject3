package org.ozsoft.secs;

/**
 * SECS Communicate State.
 * 
 * @author Oscar Stigter
 */
public enum CommunicationState {
    
    /** Not enabled. */
    NOT_ENABLED,
    
    /** _Enabled, but communication not yet established. */
    NOT_COMMUNICATING,
    
    /** Enabled and communicating established. */
    COMMUNICATING,

}
