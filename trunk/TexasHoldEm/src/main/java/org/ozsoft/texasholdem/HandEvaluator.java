package org.ozsoft.texasholdem;

/**
 * Evaluator to calculate the hand value in Texas Hold'em poker.
 *
 * NOTE: This class is implemented with the focus on performance (instead of clean design).
 */
public class HandEvaluator {
    
    /** The number of hand rankings. */
    private static final int NO_OF_RANKINGS  = 6;
    
    /** The maximum number of counting pairs. */
    private static final int MAX_NO_OF_PAIRS = 2;
    
    /** Pre-calculated ranking factors (power of 13). */
    private static final int[] RANKING_FACTORS = {371293, 28561, 2197, 169, 13, 1};
    
    /** The hand value type. */
    private HandValueType type;
    
    /** The hand value as integer number. */
    private int value = 0;
    
    /** The cards. */
    private final Card[] cards;
    
    private int[] rankDist = new int[Card.NO_OF_RANKS];
    
    private int[] suitDist = new int[Card.NO_OF_SUITS];
    
    private int flushRank = -1;
    
    private int straightRank = -1;
    
    private int quadRank = -1;
    
    private int tripleRank = -1;
    
    private int noOfPairs = 0;
    
    private int[] pairs = new int[MAX_NO_OF_PAIRS];
    
    private int[] rankings = new int[NO_OF_RANKINGS];

    /**
     * Constructor.
     *
     * @param  hand  The hand to evaulate.
     */
    public HandEvaluator(Hand hand) {
        cards = hand.getCards();
        
        // Find patterns.
        calculateDistributions();
        findFlush();
        findStraight();
        findDuplicates();
        
        // Find special values.
        boolean isSpecialValue =
                (isRoyalFlush()    ||
                 isStraightFlush() ||
                 isFourOfAKind()   ||
                 isFullHouse()     ||
                 isFlush()         ||
                 isStraight()      ||
                 isThreeOfAKind()  ||
                 isTwoPairs()      ||
                 isOnePair());
        if (!isSpecialValue) {
            calculateHighCard();
        }
        
        // Calculate value.
        for (int i = 0; i < NO_OF_RANKINGS; i++) {
            value += rankings[i] * RANKING_FACTORS[i];
        }
    }
    
    /**
     * Returns the hand value type.
     *
     * @return  the hand value type
     */
    public HandValueType getType() {
        return type;
    }
    
    /**
     * Returns the hand value as an integer.
     * This method should be used to compare hands.
     *
     * @return  the hand value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Calculates the rank and suit distributions.
     */
    private void calculateDistributions() {
        for (Card card : cards) {
            rankDist[card.getRank()]++;
            suitDist[card.getSuit()]++;
        }
    }
    
    /**
     * Looks for a flush, i.e. five cards with the same suit.
     */
    private void findFlush() {
        for (int i = 0; i < Card.NO_OF_SUITS; i++) {
            if (suitDist[i] >= 5) {
                // We found a flush! Now find the matching highest rank.
                for (Card card : cards) {
                    if (card.getSuit() == i) {
                        flushRank = card.getRank();
                        break;
                    }
                }
                break;
            }
        }
    }

    /**
     * Looks for a Straight, i.e. five cards with descending rank.
     * The Ace has the rank of One in case of a Five-high Straight (5 4 3 2 A).
     */
    private void findStraight() {
        boolean inStraight = false;
        int rank = -1;
        int count = 0;
        for (int i = Card.NO_OF_RANKS - 1; i >= 0 ; i--) {
            if (rankDist[i] == 0) {
                inStraight = false;
                count = 0;
            } else {
                if (!inStraight) {
                    // First card of the potential Straight.
                    inStraight = true;
                    rank = i;
                }
                count++;
                if (count >= 5) {
                    // Found a Straight!
                    straightRank = rank;
                    break;
                }
            }
        }
        // Special case for the Five-high Straight with a wheeling Ace.
        if ((count == 4) && (rank == Card.FIVE) && (rankDist[Card.ACE] > 0)) {
            straightRank = rank;
        }
    }

    /**
     * Finds duplicates (pairs, triples and quads), i.e. two or more cards with
     * the same rank.
     */
    private void findDuplicates() {
        // Find quads, triples and pairs.
        for (int i = Card.NO_OF_RANKS - 1; i >= 0 ; i--) {
            if (rankDist[i] == 4) {
                quadRank = i;
            } else if (rankDist[i] == 3) {
                tripleRank = i;
            } else if (rankDist[i] == 2) {
                if (noOfPairs < MAX_NO_OF_PAIRS) {
                    pairs[noOfPairs++] = i;
                }
            }
        }
    }

    /**
     * Calculates the hand value based on the highest card and four kickers.
     */
    private void calculateHighCard() {
        type = HandValueType.HIGH_CARD;
        rankings[0] = type.getValue();
        // Get the five highest values.
        int index = 1;
        for (Card card : cards) {
            rankings[index++] = card.getRank();
            if (index > 5) {
                break;
            }
        }
    }

    /**
     * Returns true if this hand contains One Pair.
     * The value of a One Pair is primarily based on the rank of the pair and
     * secondarily on the ranks of the remaining three kickers.
     *
     * @return  true if this hand contains One Pair
     */
    private boolean isOnePair() {
        if (noOfPairs == 1) {
            type = HandValueType.ONE_PAIR;
            rankings[0] = type.getValue();
            // Get the rank of the pair.
            int rank = pairs[0];
            rankings[1] = rank;
            // Get the three kickers.
            int index = 2;
            for (Card card : cards) {
                int rank2 = card.getRank();
                if (rank2 != rank) {
                    rankings[index++] = rank2;
                    if (index > 5) {
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this hand contains Two Pairs.
     * The value of a Two Pairs is primarily based on the rank of the highest
     * pair, secondarily on the rank of the lowest pair and tertiarily on the
     * ranks of the remaining one kicker.
     *
     * @return  true if this hand contains Two Pairs
     */
    private boolean isTwoPairs() {
        if (noOfPairs == 2) {
            type = HandValueType.TWO_PAIRS;
            rankings[0] = type.getValue();
            // Get the value of the high and low pairs.
            int highRank = pairs[0];
            int lowRank  = pairs[1];
            rankings[1] = highRank;
            rankings[2] = lowRank;
            // Get the kicker card.
            for (Card card : cards) {
                int rank2 = card.getRank();
                if ((rank2 != highRank) && (rank2 != lowRank)) {
                    rankings[3] = rank2;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this hand contains a Three of a Kind.
     * The value of a Three of a Kind is primarily based on the rank of the
     * pair, while the remaining two cards are used as kickers.
     *
     * @return  true if this hand contains a Three of a Kind
     */
    private boolean isThreeOfAKind() {
        if (tripleRank != -1) {
            type = HandValueType.THREE_OF_A_KIND;
            rankings[0] = type.getValue();
            // Get the rank of the triple.
            int rank = tripleRank;
            rankings[1] = rank;
            // Get the remaining two cards as kickers.
            int index = 3;
            for (Card card : cards) {
                int rank2 = card.getRank();
                if (rank2 != rank) {
                    rankings[index++] = rank2;
                    if (index > 5) {
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this hand contains a Straight.
     * The value of a Straight is based on the rank of the highest card in the
     * straight. There are no kickers.
     *
     * @return  true if this hand contains a Straight
     */
    private boolean isStraight() {
        if (straightRank != -1) {
            type = HandValueType.STRAIGHT;
            rankings[0] = type.getValue();
            rankings[1] = straightRank;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this hand contains a Flush.
     * The value of a Flush is based on the rank of the highest flushing card.
     * There are no kickers.
     *
     * @return  true if this hand contains a Flush
     */
    private boolean isFlush() {
        if (flushRank != -1) {
            type = HandValueType.FLUSH;
            rankings[0] = type.getValue();
            rankings[1] = flushRank;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this hand contains a Full House.
     * The value of a Full House is primarily based on the rank of the triple
     * and secondarily on the rank of the pair. There are no kickers.
     *
     * @return  true if this hand contains a Full House
     */
    private boolean isFullHouse() {
        if ((tripleRank != -1) && (noOfPairs > 0)) {
            type = HandValueType.FULL_HOUSE;
            rankings[0] = type.getValue();
            rankings[1] = tripleRank;
            rankings[2] = pairs[0];
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns true if this hand contains a Four of a Kind.
     * The value of a Four of a Kind is primarily based on the rank of the
     * quad. There remaining card is used as kicker.
     *
     * @return  true if this hand contains a Four of a Kind
     */
    private boolean isFourOfAKind() {
        if (quadRank != -1) {
            type = HandValueType.FOUR_OF_A_KIND;
            rankings[0] = type.getValue();
            rankings[1] = quadRank;
            // Get the remaining card as kicker.
            int index = 4;
            for (Card card : cards) {
                int rank2 = card.getRank();
                if (rank2 != quadRank) {
                    rankings[index++] = rank2;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns true if this hand contains a Straight Flush.
     * The value of a Straight Flush is based on the rank of the highest card
     * of the straight. There are no kickers.
     *
     * @return  true if this hand contains a Straight Flush
     */
    private boolean isStraightFlush() {
        if ((straightRank != -1) && (flushRank != -1)) {
            type = HandValueType.STRAIGHT_FLUSH;
            rankings[0] = type.getValue();
            rankings[1] = straightRank;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns true if this hand contains a Royal Flush.
     * The value of a Royal Flush is fixed, i.e. two Royal Flushes will tie.
     *
     * @return  true if this hand contains a Royal Flush
     */
    private boolean isRoyalFlush() {
        if ((straightRank == Card.ACE) && (flushRank == Card.ACE)) {
            type = HandValueType.ROYAL_FLUSH;
            rankings[0] = type.getValue();
            return true;
        } else {
            return false;
        }
    }

}
