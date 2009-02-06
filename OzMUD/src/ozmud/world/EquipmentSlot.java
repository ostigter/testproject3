package ozmud.world;


/**
 * Equipment slot on a creature's body to wear an item on.
 * 
 * @author Oscar Stigter
 */
public enum EquipmentSlot {
	
	
	/** Head (e.g. helmet). */
	HEAD("head"),
	
	/** Neck (e.g. robe, cape, mantle). */
	NECK("neck"),
	
	/** Body (e.g. shirt, cuirass). */
	BODY("body"),
	
	/** Arms (e.g. vambraces). */
	ARMS("arms"),
	
	/** Hands (e.g. gloves, gauntlets). */
	HANDS("hands"),
	
	/** Right-hand ring finger (ring). */
	RIGHT_RING("right ring"),
	
	/** Left-hand ring finger (ring). */
	LEFT_RING("left ring"),
	
	/** Waist (e.g. belt, pouch). */
	WAIST("waist"),
	
	/** Legs (e.g. pants). */
	LEGS("legs"),
	
	/** Feet (e.g. shoes, boots). */
	FEET("feet"),
	
	/** Shield. */
	SHIELD("shield"),
	
	/** Weapon. */
	WEAPON("weapon");

	
	/** The name as listed in the 'equipment' command. */
	private String name;
	
	
	/**
	 * Constructor.
	 * 
	 * @param name  The slot name
	 */
	EquipmentSlot(String name) {
		this.name = name;
	}
	
	
	/**
	 * Returns the slot name.
	 * 
	 * @return The slot name
	 */
	public String getName() {
		return name;
	}
	
	
	@Override
	public String toString() {
		return name;
	}


}
