package org.ozsoft.texasholdem.actions;

/**
 * The poker action of checking.
 *  
 * @author Oscar Stigter
 */
public class CheckAction implements Action {
    
	/*
	 * (non-Javadoc)
	 * @see cards.poker.Action#getVerb()
	 */
	public String getVerb() {
        return "checks";
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
        return "Check";
    }
    
}
