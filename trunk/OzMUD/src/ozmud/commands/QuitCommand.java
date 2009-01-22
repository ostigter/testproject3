package ozmud.commands;


import ozmud.world.Player;


public class QuitCommand implements Command {
	
	
	private static final String GOODBYE = "Goodbye!\n\r"; 

	private static final String LEAVE =
			"${sender} disappear${s} into thin air.\n\r";
	
	private static final String INVALID = "You can't quit that.\n\r"; 

	
	public String getName() {
		return "quit";
	}


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
