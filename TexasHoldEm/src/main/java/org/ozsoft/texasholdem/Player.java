package org.ozsoft.texasholdem;

import org.ozsoft.texasholdem.actions.Action;
import org.ozsoft.texasholdem.actions.BetAction;
import org.ozsoft.texasholdem.actions.BigBlindAction;
import org.ozsoft.texasholdem.actions.CallAction;
import org.ozsoft.texasholdem.actions.CheckAction;
import org.ozsoft.texasholdem.actions.FoldAction;
import org.ozsoft.texasholdem.actions.RaiseAction;
import org.ozsoft.texasholdem.actions.SmallBlindAction;

/**
 * A Texas Hold'em player.
 * 
 * @author Oscar Stigter
 */
public abstract class Player {
    
	/** The name. */
	protected String name;
	
	/** Current amount of money. */
    protected int money;
    
    /** The hand of cards. */
    protected Hand hand = new Hand();

    /** The current bet. */
    protected int bet;
    
    /** Whether the player has gone all-in. */
    protected boolean allIn;
    
    /** The last action performed. */
    protected Action action;

    /** Whether the player has folded. */
    protected boolean hasFolded;
    
    /**
     * Constructs a player with the specified name and initial money.
     *
     * @param  name  the name
     * @param  money  the initial money
     */
    public Player(String name, int money) {
        this.name = name;
        this.money = money;
        reset();
    }
    
    /**
     * Prepares the player for another hand.
     */
    public final void reset() {
    	if (!isBroke()) {
	        bet = 0;
	        allIn = false;
	        action = null;
	        hasFolded = false;
    	}
    }
    
    /**
     * Sets the hole cards.
     */
    public final void setCards(Card[] cards) {
    	if (cards.length != 2) {
    		throw new IllegalArgumentException("Invalid number of cards");
    	}
        hand.removeAllCards();
        hand.addCards(cards);
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
        return (money == 0);
    }
    
    /**
     * Returns the player's current amount of money.
     *
     * @return The amount of money.
     */
    public final int getCash() {
        return money;
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
     * Returns whether the player has folded.
     *
     * @return True if folded, otherwise false.
     */
    public final boolean hasFolded() {
        return hasFolded;
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
	 * @param amount
	 *            The small blind.
	 */
    public final void postSmallBlind(int amount) {
        action = new SmallBlindAction();
        money -= amount;
        bet += amount;
    }
    
	/**
	 * Posts the big blinds.
	 * 
	 * @param amount
	 *            The big blind.
	 */
    public final void postBigBlind(int amount) {
        action = new BigBlindAction();
        money -= amount;
        bet += amount;
    }
    
    /**
     * Folds.
     */
    public final void fold() {
        action = new FoldAction();
        hasFolded = true;
    }
    
    /**
     * Checks.
     */
    public final void check() {
        action = new CheckAction();
    }
    
    /**
     * Calls to the specified bet.
     *
     * @param  bet  the current bet
     */
    public final void call(int newBet) {
        int amount = newBet - bet;
        action = new CallAction(amount);
        money -= amount;
        bet += amount;
    }
    
    /**
     * Bets an amount of money.
     * 
     * If the bet is equal to or greater than the remaining money, the player
     * goes all-in.
     *
     * @param  bet  the amount to bet
     */
    public final void bet(int amount) {
        if (amount >= money) {
            amount = money;
            allIn = true;
        }
        action = new BetAction(amount);
        money -= amount;
        bet += amount;
    }
    
    /**
     * Raises the specified bet with the specified raise.
     * If the amount to raise up to is more than the amount of money, the player
     * goes all in.
     *
     * @param  currentBet  the current bet
     * @param  raise  the amount to raise with
     */
    public final void raise(int currentBet, int raise) {
        action = new RaiseAction(raise);
        int amount = (currentBet < bet) ? (bet - currentBet) + raise : raise;
        money -= amount;
        bet += amount;
    }
    
	/**
	 * Wins the pot.
	 * 
	 * @param pot
	 *            The pot.
	 */
    public final void win(int pot) {
        money += pot;
    }
    
	/**
	 * Performs an action (Check, Call, Bet, Raise or Fold).
	 * 
	 * @param board
	 *            The community cards on the board.
	 * @param noOfBoardCards
	 *            The number of community cards on the board.
	 * @param minBet
	 *            The minimum bet.
	 * @param currentBet
	 *            The current bet.
	 */
    public abstract void performAction(
    		Card[] board, int noOfBoardCards, int minBet, int currentBet);

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return name;
    }
    
}
