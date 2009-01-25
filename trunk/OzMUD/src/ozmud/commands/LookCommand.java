package ozmud.commands;


import ozmud.world.Player;
import ozmud.world.Room;


/**
 * Command 'look' to look around at the room or at a specific creature or item.
 * 
 * @author Oscar Stigter
 */
public class LookCommand implements Command {
	
	
	/** Message when trying to look at a non-existing target. */
	private static final String UNKNOWN = "Look at what?\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "look";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return "l";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
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
