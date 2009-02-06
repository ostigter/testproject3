package ozmud.world;


public class Weapon extends Equipment {


	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Whether this is a two-handed weapon. */
	private boolean isTwoHanded = false;

	/** The maximum damage dealt with this weapon. */
	private int damage;
	
	/**
	 * Default constructor.
	 */
	public Weapon() {
		// Empty implementation
	}
	
	
	/**
	 * Returns whether this is a two-handed weapon.
	 * 
	 * @return  Whether this is a two-handed weapon 
	 */
	public boolean isTwoHanded() {
		return isTwoHanded;
	}
	
	
	/**
	 * Sets whether this is a two-handed weapon.
	 * 
	 * @param isTwoHanded  True if a two-handed weapon, otherwise false
	 */
	public void setTwoHanded(boolean isTwoHanded) {
		this.isTwoHanded = isTwoHanded;
	}
	
	
	/**
	 * Returns the maximum amount of damage this weapon can do.
	 * 
	 * @return The maximum amount of damage this weapon can do
	 */
	public int getDamage() {
		return damage;
	}
	
	
	/**
	 * Sets the maximum amount of damage this weapon can do.
	 * 
	 * @param damage  The maximum amount of damage this weapon can do
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	
}
