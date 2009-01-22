package ozmud.world;


/**
 * Base class for all creatures.
 * 
 * @author Oscar Stigter
 */
public abstract class Creature {
	

	protected final String name;
	
	protected final Gender gender;
	
	protected final String description;
	
	protected final World world;
	
	protected Room room;
	
	
	public Creature(String name, Gender gender, String description, World world) {
		this.name = name;
		this.gender = gender;
		this.description = description;
		this.world = world;
	}
	

	public final String getName() {
		return name;
	}
	
	
	public final Gender getGender() {
		return gender;
	}
	
	
	public final String getDescription() {
		return description;
	}
	
	
	public final Room getRoom() {
		return room;
	}
	

	public final void setRoom(Room room) {
		this.room = room;
	}
	

	public void send(String message) {
		// Empty implementation.
	}


}
