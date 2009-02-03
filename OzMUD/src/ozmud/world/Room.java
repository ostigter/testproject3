package ozmud.world;


import java.util.ArrayList;
import java.util.List;

import ozmud.Util;


public class Room extends MudObject {
	

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Room ID. */
	private int id;
	
	/** The room's exits. */
	private final List<Exit> exits;
	
	/** Items located in this room. */
	private final List<Item> items;
	
	/** Creatures located in this room. */
	private final List<Creature> creatures;
	
	
	/**
	 * Default constructor.
	 */
	public Room() {
		this.exits = new ArrayList<Exit>();
		this.items = new ArrayList<Item>();
		this.creatures = new ArrayList<Creature>();
	}
	
	
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public List<Exit> getExits() {
		return exits;
	}
	
	
	public Exit getExit(String name) {
		for (Exit exit : exits) {
			if (exit.getName().equals(name)) {
				return exit;
			}
		}
		return null;
	}
	
	
	public void addExit(Exit exit) {
		exits.add(exit);
	}
	
	
	public List<Item> getItems() {
		return items;
	}
	
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	
	public void removeItem(Item item) {
		items.remove(item);
	}


	public List<Creature> getCreatures() {
		return creatures;
	}
	
	
	public void addCreature(Creature creature) {
		creatures.add(creature);
	}
	
	
	public void removeCreature(Creature creature) {
		creatures.remove(creature);
	}
	
	
	/**
	 * Returns a specific target from this room, or null if not found.
	 * 
	 * @param name  The targets's name
	 * 
	 * @return  The target if found, otherwise null
	 */
	public MudObject getTarget(String name) {
		// First try the items in this room...
		for (Item item : items) {
			if (item.matches(name)) {
				return item;
			}
		}
		// ...then the creatures.
		for (Creature creature : creatures) {
			if (creature.matches(name)) {
				return creature;
			}
		}

		// Not found.
		return null;
	}
	
	
	public void broadcast(
			String message, Creature sender, Creature target) {
		for (Creature creature : creatures) {
			Perspective perspective = Perspective.OTHERS;
			if (creature.equals(sender)) {
				perspective = Perspective.SELF;
			} else if (creature.equals(target)){
				perspective = Perspective.TARGET;
			}
			creature.send(format(message, sender, target, perspective));
		}
	}
	

	public void broadcastOthers(
			String message, Creature sender, Creature target) {
		for (Creature creature : creatures) {
			if (!creature.equals(sender)) {
				Perspective perspective = creature.equals(target) ?
								Perspective.TARGET : Perspective.OTHERS;
				creature.send(format(message, sender, target, perspective));
			}
		}
	}
	
	
	/* package */ String format(String message, Creature sender,
			Creature target, Perspective perspective) {
		String senderName = (sender != null) ? sender.getShortName() : null;
		String targetName = (target != null) ? target.getShortName() : null;
		switch (perspective) {
			case SELF:
				message = Util.replace(message, "${sender}", "you");
				if (sender != null && sender.equals(target)) {
					message = Util.replace(message, "${target}", "yourself");
				} else {
					message = Util.replace(message, "${target}", targetName);
				}
				message = Util.replace(message, "${s}", "");
				break;
			case TARGET:
				message = Util.replace(message, "${sender}", senderName);
				message = Util.replace(message, "${s}", "s");
				message = Util.replace(message, "${target}", "you");
				break;
			case OTHERS:
				if (sender != null && sender.equals(target)) {
					String self = sender.getGender().getSelf();
					message = Util.replace(message, "${target}", self);
				} else {
					message = Util.replace(message, "${target}", targetName);
				}
				message = Util.replace(message, "${sender}", senderName);
				message = Util.replace(message, "${s}", "s");
				break;
		}
		
		return Util.capitalize(message);
	}
	

}
