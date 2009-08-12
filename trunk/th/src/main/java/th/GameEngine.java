package th;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Limit Texas Hold'em poker game engine.
 * 
 * @author Oscar Stigter
 */
public class GameEngine {
	
    /** The maximum number of community cards on the board. */
    private static final int BOARD_SIZE = 5;
	
    /** The maximum number of bets and raises per player. */
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
	
    public GameEngine(int bigBlind, List<Player> players) {
		this.bigBlind = bigBlind;
		this.players = players;
		activePlayers = new ArrayList<Player>();
		deck = new Deck();
		board = new ArrayList<Card>();
		listeners = new HashSet<GameListener>();
	}
	
	public void addListener(GameListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(GameListener listener) {
		listeners.remove(listener);
	}
	
	public void run() {
		resetGame();
		while (!gameOver) {
			playHand();
			
			gameOver = true;
		}
		sendMessage("Game over.");
	}
	
	private void playHand() {
		resetHand();
		postSmallBlind();
		postBigBlind();
		dealHoleCards();
		doBettingRound();
	}
	
	private void postSmallBlind() {
        rotateActor();
        sendMessage("%s posts the small blind.", actor);
		int smallBlind = bigBlind / 2;
        actor.postSmallBlind(smallBlind);
        bet = smallBlind;
        pot += smallBlind;
	}
	
	private void postBigBlind() {
        rotateActor();
        sendMessage("%s posts the big blind.", actor);
        actor.postBigBlind(bigBlind);
        bet = bigBlind;
        pot += bigBlind;
	}
	
	private void dealHoleCards() {
        sendMessage("%s deals the Hole Cards.", dealer);
        for (Player player : players) {
            player.setCards(deck.deal(2));
        }
	}
	
	private void doBettingRound() {
		int playersToAct = activePlayers.size();
		while (playersToAct > 0) {
        	rotateActor();
        	actor.act(getAllowedActions(actor), board, minBet, bet);
        	Action action = actor.getAction();
        	playersToAct--;
        	switch (action) {
        		case FOLD:
            		actor.setCards(null);
            		activePlayers.remove(actor);
            		break;
        		case CHECK:
        			// Do nothing.
        			break;
        		case CALL:
        			pot += bet;
        			break;
        		case BET:
        			pot += bet;
        		case RAISE:
        			pot += 2 * bet;
        			break;
        		default:
        			throw new IllegalStateException("Invalid action: " + action);
        	}
		}
	}
	
	private Set<Action> getAllowedActions(Player player) {
    	Set<Action> actions = new HashSet<Action>();
    	if (bet == 0) {
    		actions.add(Action.CHECK);
    		actions.add(Action.BET);
    	} else if (actor.getBet() < bet) {
    		actions.add(Action.CALL);
    	} else {
    		actions.add(Action.RAISE);
    	}
    	actions.add(Action.FOLD);
    	return actions;
	}
	
	private void resetGame() {
		sendMessage("New game.");
		activePlayers.clear();
		for (Player player : players) {
			activePlayers.add(player);
		}
		hand = 0;
    	dealerPosition = -1;
    	actorPosition = -1;
		gameOver = false;
	}
	
	private void resetHand() {
		hand++;
		sendMessage("New hand");
		for (Player player : players) {
			player.resetHand();
		}
		rotateDealer();
		actorPosition = dealerPosition;
		minBet = bigBlind;
	}

    private void rotateDealer() {
        dealerPosition = (dealerPosition + 1) % players.size();
        dealer = players.get(dealerPosition);
        sendMessage("%s is the dealer.", dealer);
    }

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
    
    private void sendMessage(String message, Object... args) {
    	message = String.format(message, args);
    	for (GameListener listener : listeners) {
    		listener.messageReceived(message);
    	}
    }

}
