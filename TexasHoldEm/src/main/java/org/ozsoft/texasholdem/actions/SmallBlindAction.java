package org.ozsoft.texasholdem.actions;

public class SmallBlindAction implements Action {
    
    /*
     * (non-Javadoc)
     * @see cards.poker.Action#getVerb()
     */
	@Override
	public String getVerb() {
        return "pays the small blind";
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Small Blind";
    }
    
}
