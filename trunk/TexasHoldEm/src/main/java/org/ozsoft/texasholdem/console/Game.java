package org.ozsoft.texasholdem.console;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.ConsolePlayer;
import org.ozsoft.texasholdem.Deck;
import org.ozsoft.texasholdem.Hand;
import org.ozsoft.texasholdem.HandEvaluator;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.actions.Action;
import org.ozsoft.texasholdem.actions.BetAction;
import org.ozsoft.texasholdem.actions.CallAction;
import org.ozsoft.texasholdem.actions.FoldAction;
import org.ozsoft.texasholdem.actions.RaiseAction;

/**
 * Console version of Limit Texas Hold'em poker.
 * 
 * @author Oscar Stigter
 */
public class Game {
	
	/** The number of players at the table. */
	private static final int NO_OF_PLAYERS = 4;
	
	/** The number of hands to play. */
	private static final int MAX_NO_OF_HANDS = 1;
	
	/** The amount of starting cash. */
	private static final int STARTING_CASH = 100;
	
	/** The minimum bet. */
	private static final int MIN_BET = 2;
	
	/** The number of community cards on the board. */
	private static final int BOARD_SIZE = 5;
	
	/** The deck of cards. */
	private final Deck deck = new Deck();
    
	/** The community cards on the board. */
	private final Card[] board = new Card[BOARD_SIZE];
    
	/** The players. */
	private final Player[] players;
    
	/** The number of hands played. */
	private int noOfHands = 0;
    
	/** The current dealer position. */
	private int dealer = -1;
    
	/** The number of community cards on the board. */
	private int noOfBoardCards;
	
	/** The pot. */
	private int pot;
    
	/** The current bet. */
	private int bet;
    
	/** Whether the game is over. */
	private boolean gameOver = false;
	
	/**
	 * Application's entry point.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
    	new Game();
    }
    
	/**
	 * Constructor.
	 */
	public Game() {
    	players = new Player[NO_OF_PLAYERS];
    	for (int i = 0; i < NO_OF_PLAYERS; i++) {
    		players[i] = new ConsolePlayer(
    				"Player " + String.valueOf(i + 1), STARTING_CASH);
    	}
//    	players = new Player[] {
//    			new HumanPlayer("Buffy",  STARTING_CASH),
//    			new HumanPlayer("Willow", STARTING_CASH),
//    			new HumanPlayer("Xander", STARTING_CASH),
//    			new HumanPlayer("Anya",   STARTING_CASH),
//    	};
        
        while (!gameOver && noOfHands < MAX_NO_OF_HANDS) {

            // Prepare a new hand.
        	System.out.println("New hand.");
        	pot = 0;
            noOfBoardCards = 0;
            for (Player player : players) {
            	player.resetHand();
            }
            
            // Rotate the dealer position clockwise.
            dealer = (dealer + 1) % NO_OF_PLAYERS;
            System.out.format("%s is the dealer.\n", players[dealer]);
            
            // Shuffle the deck.
            deck.shuffle();
            System.out.format("%s shuffles the deck.\n", players[dealer]);
            
            // Post the small and big blinds.
            players[(dealer + 1) % NO_OF_PLAYERS].postSmallBlind(MIN_BET / 2);
            pot += MIN_BET / 2;
            bet = MIN_BET;
            System.out.format("%s posts the small blind.\n", players[dealer + 1]);
            players[(dealer + 2) % NO_OF_PLAYERS].postBigBlind(MIN_BET);
            pot += MIN_BET;
            System.out.format("%s posts the big blind.\n", players[dealer + 2]);
            
            // Deal the hole cards.
            System.out.format("\n%s deals the Hole cards.\n", players[dealer]);
            for (Player player : players) {
                player.setCards(deck.deal(2));
            }
            printPlayers();
            
            // Pre-flop betting round.
            doBettingRound(1);
            printPlayers();
            
            // Deal the flop cards.
            System.out.format("\n%s deals the Flop:  ", players[dealer]);
            noOfBoardCards = 3;
            for (int i = 0; i < noOfBoardCards; i++) {
                board[i] = deck.deal();
                System.out.print(board[i] + "  ");
            }
            System.out.println();
            
            // Flop betting round.
            bet = 0;
            doBettingRound(2);
            printPlayers();
            
            // Deal the turn card.
            board[noOfBoardCards] = deck.deal();
            noOfBoardCards++;
            System.out.format("\n%s deals the Turn:  ", players[dealer]);
            for (int i = 0; i < noOfBoardCards; i++) {
                System.out.print(board[i] + "  ");
            }
            System.out.println();
            
            // Turn betting round.
            bet = 0;
            doBettingRound(3);
            printPlayers();
            
            // Deal the river card.
            board[noOfBoardCards] = deck.deal();
            noOfBoardCards++;
            System.out.format("\n%s deals the River:  ", players[dealer]);
            for (int i = 0; i < noOfBoardCards; i++) {
                System.out.print(board[i] + "  ");
            }
            System.out.println();
            
            // River betting round.
            bet = 0;
            doBettingRound(4);
            printPlayers();
            
            // Showdown.
            int winner = -1;
            int winnerValue = -1;
            boolean allBroke = true;
            //TODO: Rewrite winning algorithm (e.g. ties).
            for (int i = 0; i < NO_OF_PLAYERS; i++) {
                int inTurn = (dealer + 1 + i) % NO_OF_PLAYERS;
                Player player = players[inTurn];
                if (!player.hasFolded()) {
                    allBroke = allBroke && (player.getCash() <= 0);
                    Hand hand = new Hand(board);
                    hand.addCards(player.getCards());
                    HandEvaluator evaluator = new HandEvaluator(hand);
                    String handDescription = evaluator.getType().getDescription();
                    int value = evaluator.getValue();
                    System.out.format("%s's hand: %s (%s, %d)\n", player, hand, handDescription, value);
                    if (value > winnerValue) {
                        winner = inTurn;
                        winnerValue = value;
                    }
                }
            }
            System.out.format("\n%s wins ($ %d).\n", players[winner], pot);
            players[winner].win(pot);
            
            gameOver = allBroke;
            
            noOfHands++;
        }
        
        System.out.println("\nGame over.");
    }
    
	/**
	 * Prints the status of all players.
	 */
	private void printPlayers() {
    	for (Player player : players) {
        	Action action = player.getAction();
        	String lastAction = (action == null) ? "-" : action.toString();
        	System.out.format("\t%s\t\t$ %3d\t%s\t$ %2d\t%s\n",
        			player, player.getCash(), player.getHand(), player.getBet(), lastAction);
    	}
    	System.out.format("\tPot: $ %d\n", pot);
    }

    private void doBettingRound(int round) {
    	// Determine starting actor. Normally position 2 (1 left of dealer),
    	// but two positons further at Pre-Flop (because of blinds). 
        int offset = (round == 1) ? 2 : 0;
        int actor = (dealer + offset) % NO_OF_PLAYERS;
        // Cound the number of active players left in this hand.
        int activePlayers = 0;
        for (Player player : players) {
        	if (!player.isBroke() && !player.hasFolded()) {
        		activePlayers++;
        	}
        }
        // Keep record of how many players still have to act.
        int playersToAct = activePlayers;
        // Keep going round the table until all players have acted.
        while (playersToAct > 0) {
        	// Rotate actor.
            actor = (actor + 1) % NO_OF_PLAYERS;
            Player player = players[actor];
            // Only allow active players.
            if (!player.hasFolded() && !player.isBroke()) {
            	// Ask player to act. 
                player.performAction(board, noOfBoardCards, MIN_BET, bet);
                playersToAct--;
                Action action = player.getAction();
                System.out.format("%s %s.\n", player, action.getVerb());
                if (action instanceof FoldAction && playersToAct == 1) {
                    // The last remaining player wins.
                	//TODO: Last remaining player wins.
                } else if (action instanceof CallAction) {
                	int amount = ((CallAction) action).getAmount(); 
                	pot += amount;
                } else if (action instanceof BetAction) {
                	
                    int amount = ((BetAction) action).getAmount();
                    bet = amount;
                    pot += amount;
                    // Make sure other players must react.
                    playersToAct = activePlayers - 1;
                } else if (action instanceof RaiseAction) {
                	int amount = ((RaiseAction) action).getAmount();
                    bet += amount;
                    pot += amount;
                    System.out.format("The bet is now $ %d.\n", bet);
                    // Make sure other players must react.
                    playersToAct = activePlayers - 1;
                } else {
                	// Player checked.
                }
            }
        }
        for (Player player : players) {
        	player.resetBet();
        }
    }
    
}
