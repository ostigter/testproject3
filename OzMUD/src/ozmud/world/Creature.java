package ozmud.world;


/**
 * Base class for all creatures.
 * 
 * @author Oscar Stigter
 */
public abstract class Creature extends MudObject {
	

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The creature's gender. */
	private Gender gender = Gender.NEUTRAL;
	
	/** The room this creature is currently in. */
	private Room room;
	
	
	/**
	 * Default constructor.
	 */
	public Creature() {
		// Empty implementation.
	}
	

	public Gender getGender() {
		return gender;
	}
	
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	
	public Room getRoom() {
		return room;
	}
	

	public void setRoom(Room room) {
		this.room = room;
	}
	
	
	public void moveTo(int roomId) {
		Room room = World.getInstance().getRoom(roomId);
		if (room != null) {
			moveTo(room);
		} else {
			System.err.println("*** ERROR: Room not found: " + roomId);
		}
	}
	

	public void moveTo(Room newRoom) {
		if (room != null) {
			room.removeCreature(this);
			room.broadcast("${sender} leave${s}.\n\r", this, null);
		}
		room = newRoom;
		if (room != null) {
			room.addCreature(this);
			room.broadcastOthers("${sender} enter${s}.\n\r", this, null);
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
