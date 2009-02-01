package ozmud.world;


/**
 * An worn item (e.g. clothing or armour).
 *  
 * @author Oscar Stigter
 */
public class WornItem extends Item {


	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The protection gained by wearing this item. */
	private int protection;
	
	
	/**
	 * Default constructor.
	 */
	public WornItem() {
		// Empty implementation.
	}
	
	
	/**
	 * Returns the protection gained by wearing this item.
	 * 
	 * @return The protection gained by wearing this item
	 */
	public int getProtection() {
		return protection;
	}
	
	
	/**
	 * Sets the protection gained by wearing this item.
	 * 
	 * @param protection  The protection gained by wearing this item
	 */
	public void setProtection(int protection) {
		this.protection = protection;
	}


}
