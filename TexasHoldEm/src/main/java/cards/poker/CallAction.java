package cards.poker;

/**
 * The action of calling.
 * 
 * @author Oscar Stigter
 */
public class CallAction implements Action {
    
    /** The amount. */
	private int amount;
    
	/**
	 * Constructor.
	 * 
	 * @param amount The amount.
	 */
	public CallAction(int amount) {
        this.amount = amount;
    }
    
	/**
	 * Returns the amount.
	 * 
	 * @return The amount.
	 */
	public int getAmount() {
        return amount;
    }
    
    /*
     * (non-Javadoc)
     * @see cards.poker.Action#getVerb()
     */
	@Override
	public String getVerb() {
        return "calls";
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
        return "Call";
    }
    
}
