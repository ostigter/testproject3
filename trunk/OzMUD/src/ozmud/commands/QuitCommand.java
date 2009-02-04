package ozmud.commands;


import ozmud.world.Player;


/**
 * Command 'quit' to quit the MUD session.
 *  
 * @author Oscar Stigter
 */
public class QuitCommand implements Command {
	
	
	/** Error message when an argument is specified. */
	private static final String INVALID = "${GRAY}You can't quit that.\n\r"; 

	/** Emote when the player quits. */
	private static final String LEAVE =
			"${CYAN}${sender} disappear${s} into thin air.\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "quit";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		// No alias for this dangerous command.
		return null;
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument != null) {
			player.send(INVALID);
		} else {
			player.broadcast(LEAVE, null);
			player.getRoom().removeCreature(player);
			player.disconnect();
		}
	}


}
