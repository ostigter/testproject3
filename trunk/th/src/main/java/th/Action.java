package th;

/**
 * A Texas Hold'em poker action.
 * 
 * @author Oscar Stigter
 */
public enum Action {
	
	/** Posting the small blind. */
	SMALL_BLIND("Small blind", "posts the small blind"),

	/** Posting the big blind. */
	BIG_BLIND("Big blind", "posts the big blind"),
	
	/** Checking. */
	CHECK("Check", "checks"),
	
	/** Calling a bet. */
	CALL("Call", "calls"),
	
	/** Place an initial bet. */
	BET("Bet", "bets"),
	
	/** Raising the current bet. */
	RAISE("Raise", "raises"),
	
	/** Folding. */
	FOLD("Fold", "folds"),
	
	;
	
	/** The name. */
	private final String name;
	
	/** The verb. */
	private final String verb;
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 */
	Action(String name, String verb) {
		this.name = name;
		this.verb = verb;
	}
	
	/**
	 * Returns the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the verb form of this action.
	 * 
	 * @return The verb.
	 */
	public String getVerb() {
		return verb;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
