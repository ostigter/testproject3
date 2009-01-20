package ozmud.world;

public class NPC extends Creature {
	
	public NPC(String name, Gender gender, String description) {
		super(name, gender, description);
	}
	
	public void processMessage(String message) {
		// Do nothing.
	}

}
