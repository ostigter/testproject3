package ozmud.commands;

import ozmud.world.Player;


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
		}
	}
	
	

}
