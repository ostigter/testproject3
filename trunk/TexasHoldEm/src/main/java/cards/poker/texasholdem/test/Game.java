package cards.poker.texasholdem.test;

import cards.poker.texasholdem.Card;
import cards.poker.texasholdem.ConsolePlayer;
import cards.poker.texasholdem.Deck;
import cards.poker.texasholdem.Hand;
import cards.poker.texasholdem.HandValue;
import cards.poker.texasholdem.Player;
import cards.poker.texasholdem.actions.Action;
import cards.poker.texasholdem.actions.BetAction;
import cards.poker.texasholdem.actions.FoldAction;
import cards.poker.texasholdem.actions.RaiseAction;

/**
 * Console version of Texas Hold'em Poker.
 * 
 * @author Oscar Stigter
 */
public class Game {
	
	/** The number of players at the table. */
	private static final int NO_OF_PLAYERS   = 4;
	
	/** The number of hands to play. */
	private static final int MAX_NO_OF_HANDS = 3;
	
	/** The amount of starting money. */
	private static final int STARTING_MONEY   = 100;
	
	/** The minimum bet. */
	private static final int MINIMUM_BET     = 2;
	
	/** The number of community cards on the board. */
	private static final int BOARD_SIZE      = 5;
	
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
    				"Player " + String.valueOf(i + 1), STARTING_MONEY);
    	}
//    	players = new Player[] {
//    			new HumanPlayer("Buffy",  STARTING_MONEY),
//    			new HumanPlayer("Willow", STARTING_MONEY),
//    			new HumanPlayer("Xander", STARTING_MONEY),
//    			new HumanPlayer("Anya",   STARTING_MONEY),
//    	};
        
        while (!gameOver && noOfHands < MAX_NO_OF_HANDS) {

            // Prepare a new hand.
        	System.out.println("New hand.");
            noOfBoardCards = 0;
            bet = 0;
            for (Player player : players) {
            	player.reset();
            }
            
            // Rotate the dealer position clockwise.
            dealer = (dealer + 1) % NO_OF_PLAYERS;
            System.out.format("%s is the dealer.\n", players[dealer]);
            
            // Shuffle the deck.
            deck.shuffle();
            System.out.format("%s shuffles the deck.\n", players[dealer]);
            
            // Post the small and big blinds.
            players[(dealer + 1) % NO_OF_PLAYERS].postSmallBlind(MINIMUM_BET / 2);
            System.out.format("%s posts the small blind.\n", players[dealer + 1]);
            players[(dealer + 2) % NO_OF_PLAYERS].postBigBlind(MINIMUM_BET);
            System.out.format("%s posts the big blind.\n", players[dealer + 2]);
            
            // Deal the hole cards.
            System.out.format("\n%s deals the Hole cards.\n", players[dealer]);
            for (Player player : players) {
                player.setCards(deck.deal(2));
            }
            printPlayers();
            
            // Pre-flop betting round.
            bet = MINIMUM_BET;
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
            int pot = 0;
            boolean allBroke = true;
            for (int i = 0; i < NO_OF_PLAYERS; i++) {
                int inTurn = (dealer + 1 + i) % NO_OF_PLAYERS;
                Player player = players[inTurn];
                pot += player.getBet();
                if (!player.hasFolded()) {
                    allBroke = allBroke && (player.getCash() <= 0);
                    Hand hand = new Hand(board);
                    hand.addCards(player.getCards());
                    HandValue handValue = new HandValue(hand);
                    int value = handValue.getValue();
                    System.out.format("%s's hand: %s (%s, %d)\n", player, hand, handValue.getType().getDescription(), value);
                    if (value > winnerValue) {
                        winner = inTurn;
                        winnerValue = value;
                    }
                }
            }
            System.out.format("%s wins ($ %d).\n", players[winner], pot);
            players[winner].win(pot);
            
            gameOver = allBroke;
            
            noOfHands++;
        }
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
                player.performAction(board, noOfBoardCards, MINIMUM_BET, bet);
                playersToAct--;
                Action action = player.getAction();
                System.out.format("%s %s.\n", player, action.getVerb());
                if (action instanceof FoldAction && playersToAct == 1) {
                    // The last remaining player wins.
                	//TODO: Last remaining player wins.
                } else if (action instanceof BetAction) {
                    bet = ((BetAction) action).getAmount();
                    // Make sure other players must react to this bet.
                    playersToAct = activePlayers - 1;
                } else if (action instanceof RaiseAction) {
                    bet += ((RaiseAction) action).getAmount();
                    System.out.format("The bet is now $ %d.\n", bet);
                    // Make sure other players must react to this raise.
                    playersToAct = activePlayers - 1;
                }
            }
        }
    }
    
}
