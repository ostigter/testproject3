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
	
	
	public Creature(String name, Gender gender, String description,
			World world) {
		this.name = name;
		this.gender = gender;
		this.description = description;
		this.world = world;
	}
	

	public String getName() {
		return name;
	}
	
	
	public Gender getGender() {
		return gender;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	
	public Room getRoom() {
		return room;
	}
	

	public void setRoom(Room room) {
		this.room = room;
	}
	

	public void moveTo(Room newRoom) {
		if (room != null) {
			room.removeCreature(this);
			room.broadcast("${sender} leaves.\n\r", this, null);
		}
		room = newRoom;
		if (room != null) {
			room.addCreature(this);
			room.broadcastOthers("${sender} enters.\n\r", this, null);
		}
	}
	
	
	/**
	 * Broadcasts a message to everyone in the room.
	 * 
	 * @param message  the message
	 * @param target   an optional target
	 */
	public void broadcast(String message, Creature target) {
		if (room != null) {
			room.broadcast(message, this, target);
		}
	}
	
	
	/**
	 * Broadcasts a message to everyone in the room.
	 * 
	 * @param message
	 *            the message
	 * @param target
	 *            an optional target
	 */
	public void broadcastOthers(String message, Creature target) {
		if (room != null) {
			room.broadcastOthers(message, this, target);
		}
	}
	
	
	/**
	 * Sends a message to the client.
	 * 
	 * @param message  the message
	 */
	public abstract void send(String message);


}
