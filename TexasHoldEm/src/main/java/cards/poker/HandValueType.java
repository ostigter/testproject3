package cards.poker;

/**
 * The hand value types.
 */
public enum HandValueType {
    
    ROYAL_FLUSH     ("Royal Flush",     9),
    STRAIGHT_FLUSH  ("Straight Flush",  8),
    FOUR_OF_A_KIND  ("Four of a Kind",  7),
    FULL_HOUSE      ("Full House",      6),
    FLUSH           ("Flush",           5),
    STRAIGHT        ("Straight",        4),
    THREE_OF_A_KIND ("Three of a Kind", 3),
    TWO_PAIRS       ("Two Pairs",       2),
    ONE_PAIR        ("One Pair",        1),
    HIGH_CARD       ("High Card",       0),
    ;
    
    /** The description. */
    private String description;

    /** The hand value. */
    private int value;
    
    HandValueType(String description, int value) {
        this.description = description;
        this.value = value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getValue() {
        return value;
    }
    
}
