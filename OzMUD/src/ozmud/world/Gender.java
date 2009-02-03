package ozmud.world;


/**
 * A creature's gender.
 * 
 * @author Oscar Stigter
 */
public enum Gender {
	

	/** Male. */
	MALE("male", "he", "his"),

	/** Female. */
	FEMALE("female", "she", "her"),

	/** Neutral (no gender). */
	NEUTRAL("neutral", "it", "its");
	

	/** Name. */
	private final String name;

	/** Pronoun form. */
	private final String pronoun;
	
	/** Possessive form. */
	private final String possessiveForm;
	
	
	/**
	 * Constructor.
	 * 
	 * @param name  The name
	 * @param pronoun  The pronoun
	 * @param possessiveForm  The possessive form
	 */
	Gender(String name, String pronoun, String possessiveForm) {
		this.name = name;
		this.pronoun = pronoun;
		this.possessiveForm = possessiveForm;
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
	public String getPossessiveForm() {
		return possessiveForm;
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
