package org.ozsoft.texasholdem.console;

import java.util.Date;
import java.util.Locale;

import org.ozsoft.texasholdem.Deck;
import org.ozsoft.texasholdem.Hand;
import org.ozsoft.texasholdem.HandEvaluator;

/**
 * Simulates a large number of poker hands.
 * 
 * @author  Oscar Stigter
 */
public class PerformanceTester {
    
    /** Number of hands to simulate. */
    private final static int NO_OF_HANDS = 500000;

    /** Number of cards in a hand. */
    private final static int HAND_SIZE   = 5;
    
    /**
     * Application's main entry point.
     * 
     * @param  args  command line arguments
     */
    public static void main(String[] args) {
        new PerformanceTester();
    }
    
    /**
     * Constructor.
     */
    public PerformanceTester() {
        Deck deck = new Deck();
        Hand hand = new Hand();
        
        System.out.println("Simulating poker hands...");
        
        long startTime = new Date().getTime();
        
        for (int i = 0; i < NO_OF_HANDS; i++) {
            deck.shuffle();
            hand.addCards(deck.deal(HAND_SIZE));
            new HandEvaluator(hand);
//            System.out.println("\nHand:        " + hand);
//            System.out.println("Value:       " + handValue.getValue());
            hand.removeAllCards();
        }
        
        long endTime = new Date().getTime();
        double duration = (endTime - startTime) / 1000.0;

        System.out.println(String.format(Locale.US,
                "\nDuration: %.2f sec", duration));
        int speed = (int) Math.round((double) NO_OF_HANDS / duration);
        System.out.println("Speed:    " + speed + " hands/sec");
    }
    
}
