package th;

import java.util.List;
import java.util.Set;


/**
 * A Texas Hold'em player.
 * 
 * @author Oscar Stigter
 */
public abstract class Player {
    
	/** The name. */
	protected final String name;
	
    /** The hand of cards. */
    protected final Hand hand;

	/** Current amount of cash. */
    protected int cash;
    
    /** The current bet. */
    protected int bet;
    
    /** The number of bets and raises in the current betting round. */
    protected int raises;
    
    /** The last action performed. */
    protected Action action;

    /** Whether the player has gone all-in. */
    protected boolean allIn;
    
	/**
	 * Constructs a player.
	 * 
	 * @param name
	 *            The name.
	 * @param cash
	 *            The starting cash.
	 */
    public Player(String name, int cash) {
        this.name = name;
        this.cash = cash;

        hand = new Hand();

        resetHand();
    }
    
    /**
     * Prepares the player for another hand.
     */
    public final void resetHand() {
        action = null;
    	resetBet();
    }
    
    /**
     * Prepares the player for another betting round.
     */
    public final void resetBet() {
        bet = 0;
        raises = 0;
        allIn = false;
    }
    
    /**
     * Sets the hole cards.
     */
    public final void setCards(List<Card> cards) {
        hand.removeAllCards();
    	if (cards != null) {
    		if (cards.size() == 2) {
    	        hand.addCards(cards);
    	        System.out.format("%s's cards: %s\n", name, hand);
    		} else {
    			throw new IllegalArgumentException("Invalid number of cards");
    		}
    	}
    }
    
    /**
     * Returns the player's name.
     *
     * @return The name.
     */
    public final String getName() {
        return name;
    }
    
    /**
     * Returns whether the player is broke.
     *
     * @return True if the player is broke, otherwise false.
     */
    public final boolean isBroke() {
        return (cash == 0);
    }
    
    /**
     * Returns the player's current amount of cash.
     *
     * @return The amount of cash.
     */
    public final int getCash() {
        return cash;
    }
    
    /**
     * Returns the player's current bet.
     *
     * @return The current bet.
     */
    public final int getBet() {
        return bet;
    }
    
    /**
	 * Returns the number of raises the player has done in this betting round.
	 * 
	 * @return The number of raises.
	 */
    public final int getRaises() {
    	return raises;
    }
    
    /**
     * Returns whether the player has gone all-in.
     *
     * @return True if all-in, otherwise false.
     */
    public final boolean isAllIn() {
        return allIn;
    }
    
    /**
     * Returns the player's action.
     *
     * @return  the action
     */
    public final Action getAction() {
        return action;
    }
    
    /**
     * Returns the player's hand of cards.
     *
     * @return The hand of cards.
     */
    public final Hand getHand() {
        return hand;
    }
    
    /**
     * Returns the player's hole cards.
     *
     * @return The hole cards.
     */
    public final Card[] getCards() {
        return hand.getCards();
    }
    
	/**
	 * Posts the small blind.
	 * 
	 * @param blind
	 *            The small blind.
	 */
    public final void postSmallBlind(int blind) {
        action = Action.SMALL_BLIND;
        cash -= blind;
        bet += blind;
    }
    
	/**
	 * Posts the big blinds.
	 * 
	 * @param blind
	 *            The big blind.
	 */
    public final void postBigBlind(int blind) {
        action = Action.BIG_BLIND;
        cash -= blind;
        bet += blind;
    }
    
    /**
     * Checks.
     */
    public final void check() {
        action = Action.CHECK;
    }
    
    /**
     * Calls.
     *
     * @param  bet  the current bet
     */
    public final void call(int currentBet) {
        action = Action.CALL;
        int amount = currentBet - bet;
        cash -= amount;
        bet += amount;
    }
    
    /**
     * Bets.
     * 
     * If the bet is equal to or greater than the remaining cash, the player
     * goes all-in.
     *
     * @param  bet  the amount to bet
     */
    public final void bet(int amount) {
        if (amount >= cash) {
            amount = cash;
            allIn = true;
        }
        action = Action.BET;
        cash -= amount;
        bet += amount;
        raises++;
    }
    
	/**
	 * Raises the specified bet with the specified raise.
	 * 
	 * If the amount to raise up to is more than the amount of cash, the player
	 * goes all in.
	 * 
	 * @param currentBet
	 *            The current bet
	 * @param raise
	 *            The amount to raise with
	 */
    public final void raise(int currentBet, int raise) {
        action = Action.RAISE;
        currentBet += raise;
        int amount = currentBet - bet;
        cash -= amount;
        bet += amount;
        raises++;
    }
    
    /**
     * Folds.
     */
    public final void fold() {
        action = Action.FOLD;
        hand.removeAllCards();
    }
    
	/**
	 * Wins the pot.
	 * 
	 * @param pot
	 *            The pot.
	 */
    public final void win(int pot) {
        cash += pot;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
    	return name;
    }
    
	/**
	 * Performs an action.
	 * 
	 * @param actions
	 *            The allowed actions to choose from.
	 * @param board
	 *            The community cards on the board.
	 * @param minBet
	 *            The minimum bet.
	 * @param currentBet
	 *            The current bet.
	 */
    public abstract void act(Set<Action> actions, List<Card> board, int minBet, int currentBet);

}
