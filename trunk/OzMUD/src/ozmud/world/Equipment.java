package ozmud.world;


/**
 * Base class for equipment items (e.g. clothing, armour, weapons).
 *  
 * @author Oscar Stigter
 */
public abstract class Equipment extends Item {


	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The equipment slot this item is worn. */
	private EquipmentSlot slot;
	
	/** Maximum number of hit points (endurance). */
	private int maximumHitpoints;
	
	/** Current number of hit points left. */
	private int hitpoints;
	
	
	/**
	 * Default constructor.
	 */
	public Equipment() {
		// Empty implementation.
	}
	
	
	public EquipmentSlot getEquipmentSlot() {
		return slot;
	}
	
	
	public void setEquipmentSlot(EquipmentSlot slot) {
		this.slot = slot;
	}
	
	
	public int getMaximumHitpoints() {
		return maximumHitpoints;
	}


	public void setMaximumHpoints(int maximumHitpoints) {
		this.maximumHitpoints = maximumHitpoints;
	}
	
	
	public int getHitpoints() {
		return hitpoints;
	}
	
	
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}
	
	
}
