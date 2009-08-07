package cards.poker.texasholdem;

import cards.Card;
import cards.Hand;
import cards.poker.Action;
import cards.poker.BetAction;
import cards.poker.BigBlindAction;
import cards.poker.CallAction;
import cards.poker.CheckAction;
import cards.poker.FoldAction;
import cards.poker.RaiseAction;
import cards.poker.SmallBlindAction;

public abstract class Player {
    
    protected String name;
    protected int cash;
    protected Hand hand = new Hand();
    protected boolean isPlaying;
    protected int bet;
    protected boolean allIn;
    protected Action action;
    protected boolean hasFolded;
    
    /**
     * Constructs a player with the specified name and initial cash.
     *
     * @param  name  the name
     * @param  cash  the initial cash
     */
    public Player(String name, int cash) {
        this.name = name;
        this.cash = cash;
        reset();
    }
    
    /**
     * Prepares the player for another hand, unless broke.
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
        hand.removeAllCards();
        hand.addCards(cards);
    }
    
    /**
     * Returns the player's name.
     *
     * @return  the name
     */
    public final String getName() {
        return name;
    }
    
    /**
     * Returns true if the player is playing.
     *
     * @return  true if the player is playing
     */
    public final boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * Returns true if the player is broke.
     *
     * @return  true if the player is broke
     */
    public final boolean isBroke() {
        return (cash == 0);
    }
    
    /**
     * Returns the player's amount of cash.
     *
     * @return  the amount of cash
     */
    public final int getCash() {
        return cash;
    }
    
    /**
     * Returns the player's current bet.
     *
     * @return  the current bet
     */
    public final int getBet() {
        return bet;
    }
    
    /**
     * Returns true if folded.
     *
     * @return  true if folded
     */
    public final boolean hasFolded() {
        return hasFolded;
    }
    
    /**
     * Returns the player's hand.
     *
     * @return  the hand
     */
    public final Hand getHand() {
        return hand;
    }
    
    /**
     * Returns the player's hole cards.
     *
     * @return  the hole cards
     */
    public final Card[] getCards() {
        return hand.getCards();
    }
    
    /**
     * Posts the specified small blind.
     *
     * @param  amount  the small blind
     */
    public final void postSmallBlind(int amount) {
        action = new SmallBlindAction();
        cash -= amount;
        bet += amount;
    }
    
    /**
     * Posts the specified big blind.
     *
     * @param  amount  the big blind
     */
    public final void postBigBlind(int amount) {
        action = new BigBlindAction();
        cash -= amount;
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
        cash -= amount;
        bet += amount;
    }
    
    /**
     * Bets the specified bet.
     * If the bet is equal to or greater than the remaining cash, the player
     * goes all in.
     *
     * @param  bet  the bet
     */
    public final void bet(int amount) {
        if (amount >= cash) {
            amount = cash;
            allIn = true;
        }
        action = new BetAction(amount);
        cash -= amount;
        bet += amount;
    }
    
    /**
     * Raises the specified bet with the specified raise.
     * If the amount to raise up to is more than the amount of cash, the player
     * goes all in.
     *
     * @param  currentBet  the current bet
     * @param  raise  the raise
     */
    public final void raise(int currentBet, int raise) {
        action = new RaiseAction(raise);
        int amount = (currentBet < bet) ? (bet - currentBet) + raise : raise;
        cash -= amount;
        bet += amount;
    }
    
    /**
     * Wins the specified pot.
     *
     * @return  the pot
     */
    public final void win(int pot) {
        cash += pot;
    }
    
    /**
     * Performs an action: Fold, Check, Call, Bet or Raise.
     *
     * @param  board           the board cards
     * @param  noOfBoardCards  the number of board cards
     * @param  minBet          the minimum bet
     * @param  currentBet      the current bet
     */
    public abstract void performAction(
    		Card[] board, int noOfBoardCards, int minBet, int currentBet);

    /**
     * Returns the player's action.
     *
     * @return  the action
     */
    public final Action getAction() {
        return action;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return name;
    }
    
}
