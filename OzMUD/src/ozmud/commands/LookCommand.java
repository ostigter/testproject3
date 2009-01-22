package ozmud.commands;


import ozmud.world.Player;
import ozmud.world.Room;


public class LookCommand implements Command {
	
	
//	private static final String MESSAGE = "${sender} say${s}: %s\n\r";

	private static final String UNKNOWN = "You look but can't see it.\n\r";
	
	
	public String getName() {
		return "look";
	}


	public void execute(Player player, String argument) {
		if (argument != null) {
			// TODO: Look at target
			player.send(UNKNOWN);
		} else {
			// Just look around the room.
			Room room = player.getRoom();
			player.send(String.format(
					"${GREEN}%s\n\r", room.getDescription()));
		}
	}


}
