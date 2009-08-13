package th;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Limit Texas Hold'em poker engine, controlling the game flow.
 * 
 * @author Oscar Stigter
 */
public class GameEngine {
	
    /** The maximum number of bets or raises in a single hand per player. */
    private static final int MAX_RAISES = 4;
    
    /** The size of the big blind. */
    private final int bigBlind;
	
    /** The players at the table. */
    private final List<Player> players;
	
    /** The active players in the current hand. */
    private final List<Player> activePlayers;
	
    /** The deck of cards. */
    private final Deck deck;
	
	/** The community cards on the board. */
    private final List<Card> board;
	
    /** The listeners to this game. */
    private final Set<GameListener> listeners;
	
	/** The number of hands played. */
    private int hand;
	
    /** The current dealer position. */
    private int dealerPosition;

    /** The current dealer. */
    private Player dealer;

    /** The position of the acting player. */
    private int actorPosition;
    
    /** The acting player. */
    private Player actor;

    /** The minimum bet in the current hand. */
    private int minBet;
	
    /** The bet in the current hand. */
    private int bet;
	
    /** The pot in the current hand. */
    private int pot;
	
    /** Whether the game is over. */
    private boolean gameOver;
	
    /**
     * Constructor.
     * 
     * @param bigBlind The size of the big blind.
     * @param players The players at the table.
     */
    public GameEngine(int bigBlind, List<Player> players) {
		this.bigBlind = bigBlind;
		this.players = players;
		activePlayers = new ArrayList<Player>();
		deck = new Deck();
		board = new ArrayList<Card>();
		listeners = new HashSet<GameListener>();
	}
	
	/**
	 * Adds a game listener.
	 * 
	 * @param listener
	 *            The game listener.
	 */
    public void addListener(GameListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a game listener.
	 * 
	 * @param listener
	 *            The game listener.
	 */
    public void removeListener(GameListener listener) {
		listeners.remove(listener);
	}
	
    /**
     * Main game loop.
     */
    public void run() {
		resetGame();
		while (!gameOver) {
			playHand();
		}
		sendMessage("Game over.");
	}
	
    /**
     * Resets the game.
     */
    private void resetGame() {
		sendMessage("New game.");
		hand = 0;
    	dealerPosition = -1;
    	actorPosition = -1;
		gameOver = false;
	}
	
	/**
	 * Resets the game for a new hand.
	 */
    private void resetHand() {
		hand++;
		sendMessage("New hand.");
		board.clear();
		for (Player player : players) {
			player.resetHand();
		}
		activePlayers.clear();
		for (Player player : players) {
			if (!player.isBroke()) {
				activePlayers.add(player);
			}
		}
		rotateDealer();
		sendMessage("%s shuffles the deck.", dealer);
		deck.shuffle();
		actorPosition = dealerPosition;
		minBet = bigBlind;
	}

    /**
     * Rotates the dealer position.
     */
    private void rotateDealer() {
        dealerPosition = (dealerPosition + 1) % players.size();
        dealer = players.get(dealerPosition);
        sendMessage("%s is the dealer.", dealer);
    }

    /**
     * Rotates the position of the player in turn (the actor).
     */
    private void rotateActor() {
    	if (activePlayers.size() != 0) {
        	do {
        		actorPosition = (actorPosition + 1) % players.size();
        		actor = players.get(actorPosition);
        	} while (!activePlayers.contains(actor));
    	} else {
    		// Should never happen.
    		throw new IllegalStateException("No active players left");
    	}
    }
    
    /**
     * Plays a single hand.
     */
    private void playHand() {
		resetHand();
		postSmallBlind();
		postBigBlind();
        bet = bigBlind;
		// The Pre-Flop.
		dealHoleCards();
		doBettingRound();
		if (activePlayers.size() > 1) {
			// The Flop.
			dealCommunityCards("Flop", 3);
			doBettingRound();
			if (activePlayers.size() > 1) {
				// The Turn.
    			dealCommunityCards("Turn", 1);
    			doBettingRound();
    			if (activePlayers.size() > 1) {
    				// The River.
        			dealCommunityCards("River", 1);
        	        bet = 2 * bigBlind;
        			doBettingRound();
        			if (activePlayers.size() > 1) {
        				// The Showdown.
        				doShowdown();
        			}
    			}
			}
		}
	}
	
    /**
     * Posts the small blind.
     */
    private void postSmallBlind() {
        rotateActor();
        sendMessage("%s posts the small blind.", actor);
		int smallBlind = bigBlind / 2;
        actor.postSmallBlind(smallBlind);
        pot += smallBlind;
	}
	
    /**
     * Posts the big blind.
     */
    private void postBigBlind() {
        rotateActor();
        sendMessage("%s posts the big blind.", actor);
        actor.postBigBlind(bigBlind);
        pot += bigBlind;
	}
	
    /**
     * Deals the Hole Cards.
     */
    private void dealHoleCards() {
        sendMessage("%s deals the Hole Cards.", dealer);
        for (Player player : players) {
            player.setCards(deck.deal(2));
        }
	}
	
	/**
	 * Deals a number of community cards.
	 * 
	 * @param name
	 *            The name of the phase.
	 * @param noOfCards
	 *            The number of cards to deal.
	 */
    private void dealCommunityCards(String name, int noOfCards) {
        sendMessage("%s deals the %s.", dealer, name);
        for (int i = 0; i < noOfCards; i++) {
        	board.add(deck.deal());
        }
        sendMessage("Board: %s", board);
	}
	
    /**
     * Performs a betting round.
     */
    private void doBettingRound() {
		// Determine the initial bet size.
		if (board.size() == 0) {
			// Pre-Flop; bet is the big blind.
			bet = bigBlind;
		} else {
			// Otherwise, start with no bet.
			bet = 0;
		}
		int playersToAct = activePlayers.size();
		while (playersToAct > 0) {
        	sendMessage("Hand: %d, MinBet: %d, Bet: %d, Pot: %d", hand, minBet, bet, pot);
        	rotateActor();
        	sendMessage("It's %s's turn to act.", actor);
        	boolean smallBlindPosition = (actor.getBet() == bigBlind / 2);
        	Action action = actor.act(getAllowedActions(actor), board, minBet, bet);
        	sendMessage("%s %s.", actor, action.getVerb());
        	playersToAct--;
        	switch (action) {
        		case CHECK:
        			// Do nothing.
        			break;
        		case CALL:
        			if (smallBlindPosition) {
        				bet -= 1;
        			}
    				pot += bet;
        			break;
        		case BET:
        			bet = minBet;
        			pot += bet;
        			playersToAct = activePlayers.size();
        			break;
        		case RAISE:
        			bet += minBet;
        			pot += bet;
        			if (actor.getRaises() == MAX_RAISES) {
            			playersToAct = activePlayers.size() - 1;
        			} else {
            			playersToAct = activePlayers.size();
        			}
        			break;
        		case FOLD:
            		actor.setCards(null);
            		activePlayers.remove(actor);
            		if (activePlayers.size() == 1) {
            			// The player left is the winner.
            			playerWins(activePlayers.get(0));
            			playersToAct = 0;
            		}
            		break;
        		default:
        			throw new IllegalStateException("Invalid action: " + action);
        	}
		}
		for (Player player : players) {
			player.resetBet();
		}
	}
	
	/**
	 * Returns the allowed actions of a specific player.
	 * 
	 * @param player
	 *            The player.
	 * 
	 * @return The allowed actions.
	 */
    private Set<Action> getAllowedActions(Player player) {
		int actorBet = actor.getBet();
    	Set<Action> actions = new HashSet<Action>();
    	if (bet == 0) {
    		actions.add(Action.CHECK);
    		if (player.getRaises() < MAX_RAISES) {
    			actions.add(Action.BET);
    		}
    	} else {
        	if (actorBet < bet) {
        		actions.add(Action.CALL);
        		if (player.getRaises() < MAX_RAISES) {
        			actions.add(Action.RAISE);
        		}
        	} else {
        		actions.add(Action.CHECK);
        		if (player.getRaises() < MAX_RAISES) {
        			actions.add(Action.RAISE);
        		}
        	}
    	}
    	actions.add(Action.FOLD);
    	return actions;
	}
	
    /**
     * Performs the Showdown.
     */
    private void doShowdown() {
		sendMessage("Showdown!");
		sendMessage("The board: %s", board);
		int highestValue = 0;
		List<Player> winners = new ArrayList<Player>();
		for (Player player : activePlayers) {
			// Create a hand with the community cards and the player's hole cards.
			Hand playerHand = new Hand(board);
			playerHand.addCards(player.getCards());
			// Evaluate the combined hand.
			HandEvaluator evaluator = new HandEvaluator(player.getHand());
			int value = evaluator.getValue();
			sendMessage("%s's cards:  %s\t(%d)", player, player.getHand(), value);
			// Look for one or more winners.
			if (value > highestValue) {
				// New winner.
				highestValue = value;
				winners.clear();
				winners.add(player);
			} else if (value == highestValue) {
				// Tie winner.
				winners.add(player);
			} else {
				// Loser.
			}
		}
		if (winners.size() == 1) {
			// Single winner.
			playerWins(winners.get(0));
		} else {
			// Tie; multiple winners.
			//TODO: Handle tie
			sendMessage("A tie! X and Y share the pot.");
		}
	}
	
	/**
	 * Let's a player win the pot.
	 * 
	 * @param player
	 *            The winning player.
	 */
    private void playerWins(Player player) {
		sendMessage("%s wins.", player);
		player.win(pot);
		pot = 0;
	}
	
	/**
	 * Broadcasts a game message to the listeners.
	 * 
	 * @param message
	 *            The formatted message.
	 * @param args
	 *            Any arguments.
	 */
    private void sendMessage(String message, Object... args) {
    	message = String.format(message, args);
    	for (GameListener listener : listeners) {
    		listener.messageReceived(message);
    	}
    }

}
