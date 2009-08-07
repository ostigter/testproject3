package cards.poker.texasholdem.test;

import java.util.Date;
import java.util.Locale;

import cards.poker.texasholdem.Card;
import cards.poker.texasholdem.Deck;
import cards.poker.texasholdem.Hand;
import cards.poker.texasholdem.HandEvaluator;

/**
 * Test driver to calculate the winning chance with a specific hand against a
 * specific number of opponents.
 * 
 * @author Oscar Stigter
 */
public abstract class Statistics {
    
    /** The total number of players. */
	private static final int NO_OF_PLAYERS  = 10;
	
	/** The number of hands to simulate. */
    private static final int NO_OF_HANDS    = 100000;
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        Deck deck = new Deck();
        Hand[] hands = new Hand[NO_OF_PLAYERS];
        Hand board = new Hand();
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
            hands[i] = new Hand();
        }
        int noOfWins = 0;

        System.out.println("Simulating poker hands...");
        
        long startTime = new Date().getTime();
        
        for (int i = 0; i < NO_OF_HANDS; i++) {
            deck.reset();
            deck.shuffle();
            board.removeAllCards();
            
            for (int j = 0; j < NO_OF_PLAYERS; j++) {
                hands[j].removeAllCards();
                if (j == 0) {
                    hands[j].addCard(deck.deal(Card.ACE,   Card.SPADES));
                    hands[j].addCard(deck.deal(Card.THREE, Card.SPADES));
                } else {
                    hands[j].addCards(deck.deal(2));
                }
//                System.out.println(hands[j]);
            }
            
            board.addCards(deck.deal(5));
            
            int highestValue = 0;
            int winner = -1;
            for (int j = 0; j < NO_OF_PLAYERS; j++) {
                hands[j].addCards(board.getCards());
//                System.out.println(hands[j]);
                int value = new HandEvaluator(hands[j]).getValue();
                if (value > highestValue) {
                    highestValue = value;
                    winner = j;
                }
            }
            
            
            if (winner == 0) {
                noOfWins++;
            }
        }

        long endTime = new Date().getTime();
        double chance = ((double) noOfWins / (double) NO_OF_HANDS) * 100;
        double duration = ((double) (endTime - startTime)) / 1000;
        int speed = (int) Math.round((double) NO_OF_HANDS / (double) duration);
        System.out.println(String.format("\nChance:   %.1f %%", chance));
        System.out.println(String.format("\nDuration: %.1f sec", duration));
        System.out.println(String.format("Speed:    %d hands/sec", speed));
    }
    
}
