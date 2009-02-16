package ozmud.world;


/**
 * A piece of armour.
 * 
 * @author Oscar Stigter
 */
public class Armour extends Equipment {


	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The armour value. */
	private int armourValue;
	
	
	/**
	 * Default constructor.
	 */
	public Armour() {
		// Empty implementation.
	}
	
	
	/**
	 * Returns the protection gained by wearing this item.
	 * 
	 * @return The protection gained by wearing this item
	 */
	public int getArmourValue() {
		return armourValue;
	}
	
	
	/**
	 * Sets the armour value.
	 * 
	 * @param armourValue  The armour value
	 */
	public void setArmourValue(int armourValue) {
		this.armourValue = armourValue;
	}


}
