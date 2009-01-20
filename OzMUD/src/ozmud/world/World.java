package ozmud.world;

import java.util.HashMap;
import java.util.Map;

public class World {

	private final Map<Integer, Room> rooms;
	private final Map<String, Player> players;

	public World() {
		rooms = new HashMap<Integer, Room>();
		players = new HashMap<String, Player>();
		init();
	}

	public Player getPlayer(String name) {
		return players.get(name);
	}

	public void addPlayer(String name, String password) {
		players.put(name.toLowerCase(), new Player(name, Gender.MALE, password));
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

		addPlayer("oscar", "apple");
	}

}
