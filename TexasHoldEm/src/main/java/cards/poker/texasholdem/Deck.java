package cards.poker.texasholdem;

import java.util.Random;

/**
 * Represents a standard deck of cards.
 *
 * NOTE: This class is implemented with the focus on performance (instead of clean design).
 */
public class Deck {
    
    /** The number of cards in a deck. */
    private static final int NO_OF_CARDS = Card.NO_OF_RANKS * Card.NO_OF_SUITS;
    
    /** The cards in the deck. */
    private Card[] cards;
    
    /** The index of the next card to deal. */
    private int nextCardIndex = 0;
    
    /** The randomizer instance. */
    private Random random = new Random();

    /**
     * Constructs a full deck of cards, ordered from Ace of Spades to Deuce of
     * Diamonds.
     */
    public Deck() {
        cards = new Card[NO_OF_CARDS];
        int index = 0;
        for (int suit = Card.NO_OF_SUITS - 1; suit >= 0; suit--) {
            for (int rank = Card.NO_OF_RANKS - 1; rank >= 0 ; rank--) {
                cards[index++] = new Card(rank, suit);
            }
        }
    }
    
    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        for (int oldIndex = 0; oldIndex < NO_OF_CARDS; oldIndex++) {
            int newIndex = random.nextInt(NO_OF_CARDS);
            Card tempCard = cards[oldIndex];
            cards[oldIndex] = cards[newIndex];
            cards[newIndex] = tempCard;
        }
        nextCardIndex = 0;
    }
    
    /**
     * Resets the deck to full.
     * This method does not reorder the cards.
     */
    public void reset() {
        nextCardIndex = 0;
    }
    
    /**
     * Returns a single dealt cards.
     *
     * @return  the card dealt
     */
    public Card deal() {
        if (nextCardIndex + 1 >= NO_OF_CARDS) {
            throw new IllegalArgumentException(
                    "Deck.deal(): Not enough cards left in the deck.");
        }
        return cards[nextCardIndex++];
    }
    
    /**
     * Returns the specified number of dealt cards.
     *
     * @param   noOfCards  the number of cards to deal
     * @return  the cards dealt
     */
    public Card[] deal(int noOfCards) {
        if (noOfCards < 1) {
            throw new IllegalArgumentException(
                    "Deck.deal(): Illegal number of cards to deal: " + noOfCards);
        }
        if (nextCardIndex + noOfCards >= NO_OF_CARDS) {
            throw new IllegalArgumentException(
                    "Deck.deal(): Not enough cards left in the deck.");
        }
        Card[] dealtCards = new Card[noOfCards];
        for (int i = 0; i < noOfCards; i++) {
            dealtCards[i] = cards[nextCardIndex++];
        }
        return dealtCards;
    }
    
    public Card deal(int rank, int suit) {
        if (nextCardIndex + 1 >= NO_OF_CARDS) {
            throw new IllegalArgumentException(
                    "Deck.deal(): Not enough cards left in the deck.");
        }
        Card card = null;
        int index = -1;
        for (int i = nextCardIndex; i < NO_OF_CARDS; i++) {
            if ((cards[i].getRank() == rank) && (cards[i].getSuit() == suit)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            if (index != nextCardIndex) {
                Card nextCard = cards[nextCardIndex];
                cards[nextCardIndex] = cards[index];
                cards[index] = nextCard;
            }
            card = deal();
        }
        return card;
    }
    
    /**
     * Returns a string representation of this object instance.
     *
     * @return  the string representation
     */
    @Override // Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card);
            sb.append(' ');
        }
        return sb.toString().trim();
    }
    
}
