package ozmud.world;


/**
 * A player's current state.
 * 
 * @author Oscar Stigter
 */
public enum PlayerState {
    

    /** Offline after having properly logged out. */
    OFFLINE,

    /** Online and active (non-idle). */
    ONLINE,
    
    /** Online but idle. */
    IDLE,
    
    /** Offline without properly logging out. */
    LINKDEAD,
    
    /** Resting. */
    RESTING,
    
    /** Knocked down. */
    KNOCKED_DOWN,
    
    /** Stunned. */
    STUNNED,
    
    /** Dead (after being killed). */
    DEAD,
    

}
