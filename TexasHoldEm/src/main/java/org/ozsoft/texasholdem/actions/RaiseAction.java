package org.ozsoft.texasholdem.actions;

/**
 * The poker action of raising.
 * 
 * @author Oscar Stigter
 */
public class RaiseAction implements Action {
    
	/** The amount to raise the current bet with. */
	private int amount;
    
	/**
	 * Constructor.
	 * 
	 * @param amount
	 *            The amount to raise with.
	 */
	public RaiseAction(int amount) {
        this.amount = amount;
    }
    
	/**
	 * Returns the amount to raise with.
	 * 
	 * @return The amount.
	 */
	public int getAmount() {
        return amount;
    }
    
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.texasholdem.actions.Action#getVerb()
	 */
	@Override
    public String getVerb() {
        return "raises with $ " + amount;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
        return "Raise";
    }
    
}
