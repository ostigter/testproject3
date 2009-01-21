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
	
	
	public Room getRoom(int id) {
		return rooms.get(0);
	}


	private void init() {
		Room room = null;

		room = new Room(0, "The Plaza", "The town's central plaza.");
		room.addExit(new Exit("east", 1));
		rooms.put(0, room);

		room = new Room(1, "East Road",
				"A broad road leading through town from east to west.");
		room.addExit(new Exit("west", 0));
		rooms.put(1, room);

		addPlayer(new Player("Gandalf", Gender.MALE, "admin", this));
	}


}
