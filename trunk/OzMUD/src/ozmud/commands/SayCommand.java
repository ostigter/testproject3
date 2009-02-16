package ozmud.commands;


import ozmud.world.Player;


/**
 * Command 'say' to say something aloud, audible to everyone in the same room.
 * 
 * @author Oscar Stigter
 */
public class SayCommand implements Command {
	
	
	private static final String MESSAGE = "${sender} say${s}: %s\n\r";

	private static final String INVALID = "Say what?\n\r";
	
	
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
