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

    /** Pronoun form. */
    private final String pronoun;
    
    /** Possessive form. */
    private final String possessive;
    
    /** Self-reference form. */
    private final String self;
    
    
    /**
     * Constructor.
     * 
     * @param name        The name.
     * @param pronoun     The pronoun form.
     * @param possessive  The possessive form.
     * @param self        The self-reference form.
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
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * Returns the pronoun form.
     * 
     * @return The pronoun form.
     */
    public String getPronoun() {
        return pronoun;
    }
    
    
    /**
     * Returns the possessive form.
     * 
     * @return The possessive form.
     */
    public String getPossessive() {
        return possessive;
    }
    
    
    
    /**
     * Returns the self-reference form.
     * 
     * @return The self-reference form.
     */
    public String getSelf() {
        return self;
    }
    
    
    
    /**
     * Returns the <code>String</code> representation of this object.
     * 
     * @return The <code>String</code> representation of this object.
     */
    @Override
    public String toString() {
        return name;
    }


}
