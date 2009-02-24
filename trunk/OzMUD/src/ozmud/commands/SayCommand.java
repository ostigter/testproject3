package ozmud.commands;


import ozmud.world.Player;


/**
 * Command 'say' to say something aloud, audible all creatures in the same
 * room.
 * 
 * @author Oscar Stigter
 */
public class SayCommand implements Command {
	
	
	/** Error message when no argument is specified. */
	private static final String INVALID = "${GRAY}Say what?\n\r";
	
	/** Emote message of a successful action. */
	private static final String MESSAGE = "${CYAN}${sender} say${s}: %s\n\r";

	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "say";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		// No alias.
		return null;
	}

	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument != null) {
			player.broadcast(String.format(MESSAGE, argument), null);
		} else {
			player.send(INVALID);
		}
	}


}
