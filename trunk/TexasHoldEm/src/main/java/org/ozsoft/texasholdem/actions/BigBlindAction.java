package org.ozsoft.texasholdem.actions;

/**
 * The poker action of posting the big blind.
 * 
 * @author Oscar Stigter
 */
public class BigBlindAction implements Action {
    
    /*
     * (non-Javadoc)
     * @see cards.poker.Action#getVerb()
     */
	public String getVerb() {
        return "pays the big blind";
    }
    
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
        return "Big Blind";
    }
    
}
