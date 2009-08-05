package cards.poker;

/**
 * The action of paying the big blind.
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
    
    @Override
    public String toString() {
        return "Big Blind";
    }
    
}
