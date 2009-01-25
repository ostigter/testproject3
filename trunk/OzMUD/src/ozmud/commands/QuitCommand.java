package ozmud.commands;


import ozmud.world.Player;


/**
 * Command 'quit' to quit the MUD session.
 *  
 * @author Oscar Stigter
 */
public class QuitCommand implements Command {
	
	
	private static final String GOODBYE = "Goodbye!\n\r"; 

	private static final String LEAVE =
			"${sender} disappear${s} into thin air.\n\r";
	
	private static final String INVALID = "You can't quit that.\n\r"; 

	
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
		if (argument == null) {
			player.send(GOODBYE);
			player.broadcast(LEAVE, null);
			player.moveTo(null);
			player.disconnect();
		} else {
			player.send(INVALID);
		}
	}


}
