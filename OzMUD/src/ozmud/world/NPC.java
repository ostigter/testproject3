package ozmud.world;


/**
 * A non-player (computer controlled) character (NPC).
 * 
 * @author Oscar Stigter
 */
public class NPC extends Creature {
	

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Default constructor.
	 */
	public NPC() {
		// Empty implementation.
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.world.Creature#send(java.lang.String)
	 */
	@Override
	public void send(String message) {
		// Empty implemention because an NPC has no connection.
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.world.Creature#die()
	 */
	@Override
	public void die() {
		//TODO: NPC dies.
	}
	

}
