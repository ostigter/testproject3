package ozmud.world;


/**
 * A creature's gender.
 * 
 * @author Oscar Stigter
 */
public enum Gender {
	

	/** Male. */
	MALE("male", "he", "his", "himself"),

	/** Female. */
	FEMALE("female", "she", "her", "herself"),

	/** Neutral (no gender). */
	NEUTRAL("neutral", "it", "its", "itself");
	

	/** Name. */
	private final String name;

	/** Pronoun. */
	private final String pronoun;
	
	/** Possessive. */
	private final String possessive;
	
	/** Self reference. */
	private final String self;
	
	
	/**
	 * Constructor.
	 * 
	 * @param name        The name
	 * @param pronoun     The pronoun
	 * @param possessive  The possessive form
	 */
	Gender(String name, String pronoun, String possessive, String self) {
		this.name = name;
		this.pronoun = pronoun;
		this.possessive = possessive;
		this.self = self;
	}
	
	
	/**
	 * Returns the name.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Returns the pronoun.
	 * 
	 * @return The pronoun
	 */
	public String getPronoun() {
		return pronoun;
	}
	
	
	/**
	 * Returns the possessive form.
	 * 
	 * @return The possessive form
	 */
	public String getPossessive() {
		return possessive;
	}
	
	
	
	/**
	 * Returns the self reference.
	 * 
	 * @return The self reference
	 */
	public String getSelf() {
		return self;
	}
	
	
	
	/**
	 * Returns the <code>String</code> representation of this object.
	 * 
	 * @return The <code>String</code> representation of this object
	 */
	@Override
	public String toString() {
		return name;
	}


}
