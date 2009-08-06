package cards.poker.texasholdem.test;

import cards.Card;
import cards.Deck;
import cards.Hand;
import cards.poker.Action;
import cards.poker.BetAction;
import cards.poker.HandValue;
import cards.poker.RaiseAction;
import cards.poker.texasholdem.HumanPlayer;
import cards.poker.texasholdem.Player;

/**
 * Console version of Texas Hold'em Poker.
 * 
 * @author Oscar Stigter
 */
public class Game {
	
	private static final int NO_OF_PLAYERS   = 4;
	
	private static final int INITIAL_MONEY   = 100;
	
	private static final int MINIMUM_BET     = 2;
	
	private static final int MAX_NO_OF_HANDS = 1;
	
	private static final int BOARD_SIZE      = 5;
	
    private final Deck deck = new Deck();
    
    private final Card[] board = new Card[BOARD_SIZE];
    
    private final Player[] players;
    
    private int noOfHands = 0;
    
    private int dealer = -1;
    
    private int noOfBoardCards;
    
    private int bet;
    
	private boolean gameOver = false;
	
    public static void main(String[] args) {
    	new Game();
    }
    
    public Game() {
    	players = new Player[NO_OF_PLAYERS];
    	for (int i = 0; i < NO_OF_PLAYERS; i++) {
    		players[i] = new HumanPlayer(
    				"Player " + String.valueOf(i + 1), INITIAL_MONEY);
    	}
//    	players = new Player[] {
//    			new HumanPlayer("Buffy",  INITIAL_MONEY),
//    			new HumanPlayer("Willow", INITIAL_MONEY),
//    			new HumanPlayer("Xander", INITIAL_MONEY),
//    			new HumanPlayer("Anya",   INITIAL_MONEY),
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
    
    private void printPlayers() {
    	for (Player player : players) {
    		printPlayer(player);
    	}
    }

    private void printPlayer(Player player) {
    	Action action = player.getAction();
    	String lastAction = (action == null) ? "-" : action.toString();
    	System.out.format("\t%s\t\t$ %3d\t%s\t$ %2d\t%s\n",
    			player, player.getCash(), player.getHand(), player.getBet(), lastAction);
    }

    private void doBettingRound(int round) {
        boolean aggressiveAct;
        int offset = (round == 1) ? 3 : 1;
        do {
            aggressiveAct = false;
            for (int i = 0; i < NO_OF_PLAYERS; i++) {
                int actor = (dealer + offset + i) % NO_OF_PLAYERS;
                Player player = players[actor];
                if (!player.hasFolded() && !player.isBroke()) {
                    player.performAction(board, noOfBoardCards, MINIMUM_BET, bet);
                    Action action = player.getAction();
                    System.out.format("%s %s.\n", player, action.getVerb());
                    if (action instanceof BetAction) {
                        bet = ((BetAction) action).getAmount();
                        aggressiveAct = true;
                    } 
                    if (action instanceof RaiseAction) {
                        bet += ((RaiseAction) action).getAmount();
                        aggressiveAct = true;
                    }
                }
            }
        } while (aggressiveAct);
    }
    
}
