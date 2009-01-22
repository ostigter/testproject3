package ozmud.world;


/**
 * A Non-Player Character (NPC).
 * 
 * @author Oscar Stigter
 */
public class NPC extends Creature {
	

	/**
	 * Constructor.
	 */
	public NPC(String name, Gender gender, String description, World world) {
		super(name, gender, description, world);
	}


	@Override
	public void send(String message) {
		// Empty implemention because an NPC has no connection.
	}
	

}
