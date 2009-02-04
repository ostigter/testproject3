package ozmud.world;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Base class for all creatures.
 * 
 * @author Oscar Stigter
 */
public abstract class Creature extends MudObject {
	

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Reference to self. */
	private static final String ME = "me";

	/** The creature's gender. */
	private Gender gender = Gender.NEUTRAL;
	
	/** The room this creature is currently in. */
	private Room room;
	
	/** Inventory (carried items). */
	private final Set<Item> items;
	
	/** Worn items mapped by body part. */
	private final Map<BodyPart, WornItem> wornItems;
	
	/** The currently wielded weapon. */
	private Weapon weapon; 
	
	
	/**
	 * Default constructor.
	 */
	public Creature() {
		items = new HashSet<Item>();
		wornItems = new HashMap<BodyPart, WornItem>();
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
	
	
	public void moveTo(int roomId, String direction) {
		Room room = World.getInstance().getRoom(roomId);
		if (room != null) {
			moveTo(room, direction);
		} else {
			System.err.println("*** ERROR: Room not found: " + roomId);
		}
	}
	

	public void moveTo(Room newRoom) {
		moveTo(newRoom, null);
	}
	

	public void moveTo(Room newRoom, String direction) {
		if (room != null) {
			if (direction != null) {
				room.broadcast(String.format(
						"${CYAN}${sender} leave${s} %s.\n\r", direction),
						this, null);
			} else {
				room.broadcast("${CYAN}${sender} leave${s}.\n\r", this, null);
			}
			room.removeCreature(this);
		}
		room = newRoom;
		if (room != null) {
			room.addCreature(this);
			room.broadcastOthers(
					"${CYAN}${sender} enter${s}.\n\r", this, null);
		}
	}


	/**
	 * Adds an item to the inventory.
	 *
	 * @param item  The item
	 */
	public void addItem(Item item) {
		items.add(item);
	}


	/**
	 * Removes an item from the inventory.
	 *
	 * @param item  The item
	 */
	public void removeItem(Item item) {
		items.remove(item);
	}


	public void wearItem(BodyPart bodyPart, WornItem item) {
		wornItems.put(bodyPart, item);
	}


	public void removeItem(BodyPart bodyPart) {
		wornItems.remove(bodyPart);
	}


	/**
	 * Returns the wielded weapon.
	 *
	 * @return  The weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}


	/**
	 * Sets the wielded weapon.
	 *
	 * @param weapon  The weapon
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}


	/**
	 * Returns a specific target, or null if not found.
	 *
	 * @param name  The target's name
	 *
	 * @return  The target if found, otherwise null
	 */
	public MudObject getTarget(String name) {
		// First see if we are looking for ourselves...
		if (name.equals(ME) || matches(name)) {
			return this;
		}
		// ...then search our inventory...
		for (Item item : items) {
			if (item.matches(name)) {
				return item;
			}
		}
		// ...then search the room we're in.
		if (room != null) {
			MudObject target = room.getTarget(name);
			if (target != null) {
				return target;
			}
		}
		// Not found.
		return null;
	}


	/**
	 * Broadcasts a message to all creatures in the room.
	 *
	 * @param message  The message
	 * @param target   An optional target
	 */
	public void broadcast(String message, Creature target) {
		if (room != null) {
			room.broadcast(message, this, target);
		}
	}
	
	
	/**
	 * Broadcasts a message to all creatures in the room, except yourself.
	 * 
	 * @param message  The message
	 * @param target   An optional target
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
