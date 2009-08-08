package org.ozsoft.texasholdem.actions;

/**
 * The poker action of placing a bet.
 * 
 * @author Oscar Stigter
 */
public class BetAction implements Action {
    
	/** The bet amount. */
	private int amount;
    
    /**
     * Constructor.
     * 
     * @param amount The bet amount.
     */
	public BetAction(int amount) {
        this.amount = amount;
    }
    
    /**
	 * Returns the bet amount.
	 * 
	 * @return The bet amount.
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
        return "bets $ " + amount;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Bet";
    }
    
}
