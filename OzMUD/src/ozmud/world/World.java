package ozmud.world;


import java.util.HashMap;
import java.util.Map;

import ozmud.commands.CommandInterpreter;


/**
 * The MUD world.
 * 
 * @author Oscar Stigter
 */
public class World {


	private final CommandInterpreter commandInterpreter;
	
	private final Map<Integer, Room> rooms;
	
	private final Map<String, Player> players;


	public World() {
		commandInterpreter = new CommandInterpreter();
		rooms = new HashMap<Integer, Room>();
		players = new HashMap<String, Player>();
		
		init();
	}
	
	
	public CommandInterpreter getCommandInterpreter() {
		return commandInterpreter;
	}


	public Player getPlayer(String name) {
		return players.get(name);
	}


	public void addPlayer(Player player) {
		players.put(player.getName(), player);
	}
	
	
	public Room getStartingRoom() {
		return getRoom(0);
	}
	
	
	public Room getRoom(int id) {
		return rooms.get(id);
	}


	private void init() {
		Room room;

		room = new Room(0, "The Plaza",
				"You are standing in the large town plaza, the natural "
				+ "gathering place for town folk, merchants and adventurers "
				+ "alike. You see people of all races and classes passing by "
				+ "as they go about their business.");
		room.addExit(new Exit("east", 1));
		addRoom(room);

		room = new Room(1, "East Road",
				"You are on a wide east-west road leading through town. "
				+ "Many shops can be found here, like a general store to the "
				+ "north and the town's bank to the south. To the west you "
				+ "hear the sounds from many people on the town's plaza. to "
				+ "the east you see the town's east gate.");
		room.addExit(new Exit("north", 3));
		room.addExit(new Exit("east",  4));
		room.addExit(new Exit("south", 2));
		room.addExit(new Exit("west",  0));
		addRoom(room);

		room = new Room(2, "Bank",
				"You are inside the town's large bank. The floor and high ceiling "
				+ "are made of black marble, giving the place a luxureous "
				+ "look. Golden decorations can be found everywhere. You feel "
				+ "somewhat humble in this expensive place.");
		room.addExit(new Exit("north", 1));
		addRoom(room);

		room = new Room(3, "General store",
				"You are in a small general store.");
		room.addExit(new Exit("south", 1));
		addRoom(room);

		room = new Room(4, "East town gate",
				"You stand before the town's massive eastern gate. A heavy "
				+ "steel gate, currently risen, can be quickly closed to "
				+ "protect the town from any invasion from the wilderness "
				+ "outside. On top of the two gate towers you see bowmen "
				+ "watching the passing town folk and keeping a watchful eye "
				+ "out for trouble from afar.");
		room.addExit(new Exit("east", 5));
		room.addExit(new Exit("west", 1));
		addRoom(room);

		room = new Room(5, "Outside the east gate",
				"You stand just outside the town's eastern gates. The "
				+ "wilderness stretches out in all directions as far as your"
				+ "eyes can see.");
		room.addExit(new Exit("west", 4));
		addRoom(room);

		addPlayer(new Player("Guest", Gender.MALE, "guest", this));
	}
	
	
	private void addRoom(Room room) {
		rooms.put(room.getId(), room);
	}


}
