package org.ozsoft.texasholdem.actions;

/**
 * The poker action of folding.
 * 
 * @author Oscar Stigter
 */
public class FoldAction implements Action {
    
    /*
     * (non-Javadoc)
     * @see cards.poker.Action#getVerb()
     */
	@Override
	public String getVerb() {
        return "folds";
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
        return "Fold";
    }
    
}
