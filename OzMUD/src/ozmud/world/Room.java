package ozmud.world;

import java.util.ArrayList;
import java.util.List;

public class Room {
	
	private final int id;
	private final String name;
	private final String description;
	private final List<Exit> exits;
	private final List<Item> items;
	private final List<Creature> creatures;
	
	public Room(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.exits = new ArrayList<Exit>();
		this.items = new ArrayList<Item>();
		this.creatures = new ArrayList<Creature>();
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDesciption() {
		return description;
	}
	
	public List<Exit> getExits() {
		return exits;
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

}
