package ozmud.commands;

import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.Room;


/**
 * Command 'get' to get an item from the room.
 * 
 * @author Oscar Stigter
 */
public class GetCommand implements Command {
	
	
	/** Message when trying to get a non-existing item. */
	private static final String INVALID = "Get what?\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "get";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return "g";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument == null) {
			player.send(INVALID);
		} else {
			Room room = player.getRoom();
			Item item = room.getItem(argument);
			if (item != null) {
				room.removeItem(item);
				player.addItem(item);
				String message = String.format(
						"${CYAN}${sender} get${s} %s.\n\r",
						item.getFullName());
				player.broadcast(message, null);
			}
		}
	}
	

}
