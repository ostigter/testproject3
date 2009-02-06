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
	
	/** Ccarried items. */
	private final Set<Item> carriedItems;
	
	/** Worn items mapped by body part. */
	private final Map<BodyPart, WornItem> wornItems;
	
	/** The currently wielded weapon. */
	private Weapon weapon; 
	
	
	/**
	 * Default constructor.
	 */
	public Creature() {
		carriedItems = new HashSet<Item>();
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
	 * Returns the carried items.
	 * 
	 * @return  The carried items
	 */
	public Item[] getCarriedItems() {
		return carriedItems.toArray(new Item[0]);
	}
	
	
	public Item getCarriedItem(String name) {
		for (Item item : carriedItems) {
			if (item.matches(name)) {
				return item;
			}
		}
		return null;
	}
	
	
	public WornItem getWornItem(BodyPart bodyPart) {
		return wornItems.get(bodyPart);
	}
	
	
	public WornItem getWornItem(String name) {
		for (Item item : wornItems.values()) {
			if (item.matches(name)) {
				return (WornItem) item;
			}
		}
		return null;
	}
	
	
	/**
	 * Returns a specific inventory item, or null if not found.
	 * First the wielded weapon is checked, then the worn items, then the
	 * carried items.
	 * 
	 * @param name  The item's name
	 * 
	 * @return  The item if found, otherwise null
	 */
	public Item getItem(String name) {
		Item item = null;
		if (weapon != null && weapon.matches(name)) {
			item = weapon;
		} else {
			item = getWornItem(name);
			if (item == null) {
				item = getCarriedItem(name);
			}
		}
		return item;
	}


	/**
	 * Adds an item to the inventory.
	 *
	 * @param item  The item
	 */
	public void addCarriedItem(Item item) {
		carriedItems.add(item);
	}
	
	
	/**
	 * Returns true if a specific item is being carried.
	 * 
	 * @param item  The item
	 * 
	 * @return True if carried, otherwise false
	 */
	public boolean isCarrying(Item item) {
		return carriedItems.contains(item);
	}


	/**
	 * Removes an item from the inventory.
	 *
	 * @param item  The item
	 */
	public void removeCarriedItem(Item item) {
		carriedItems.remove(item);
	}


	/**
	 * Returns the worn items.
	 * 
	 * @return  The worn items
	 */
	public Item[] getWornItems() {
		return wornItems.values().toArray(new Item[0]);
	}
	
	
	/**
	 * Wears an item.
	 * 
	 * @param bodyPart  The body part to wear in on
	 * @param item      The item
	 */
	public void wearItem(WornItem item) {
		wornItems.put(item.getBodyPart(), item);
	}


	/**
	 * Removes a worn item.
	 * 
	 * @param bodyPart  The body part the item is worn on
	 */
	public void removeWornItem(BodyPart bodyPart) {
		wornItems.remove(bodyPart);
	}
	
	
	/**
	 * Returns true if a specific item is being worn.
	 * 
	 * @param item  The item
	 * 
	 * @return True if worn, otherwise false
	 */
	public boolean isWearing(Item item) {
		return wornItems.containsValue(item);
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
	 * Returns true if an item is the currently wielded weapon.
	 * 
	 * @param item  The item
	 * 
	 * @return True if an item is the currently wielded weapon
	 */
	public boolean isWielding(Item item) {
		return item.equals(weapon);
	}


	/**
	 * Returns a specific target, or null if not found.
	 * The target is searched for in this specific order:
	 * <ul>
	 * <li>the creature itself (self-reference)</li>
	 * <li>the wielded weapon</li>
	 * <li>a worn item</li>
	 * <li>a carried item</li>
	 * <li>an item in the room</li>
	 * </ul>
	 * 
	 * The target can be either the creature itself (self reference), a worn
	 * item, the wielded weapon, a carried item or an item in the room.
	 *
	 * @param name  The target's name
	 *
	 * @return  The target if found, otherwise null
	 */
	public MudObject getTarget(String name) {
		// See if we are looking for ourselves.
		if (name.equals(ME) || matches(name)) {
			return this;
		}
		// Check the wielded weapon.
		if (weapon != null && weapon.matches(name)) {
			return weapon;
		}
		// Sarch the worn items.
		for (Item item : wornItems.values()) {
			if (item.matches(name)) {
				return item;
			}
		}
		// Search the carried items.
		for (Item item : carriedItems) {
			if (item.matches(name)) {
				return item;
			}
		}
		// Search the room we're in.
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
