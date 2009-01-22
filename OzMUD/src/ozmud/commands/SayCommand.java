package ozmud.commands;


import ozmud.world.Player;


public class SayCommand implements Command {
	
	
	private static final String MESSAGE = "${sender} say${s}: %s\n\r";

	private static final String INVALID = "Say what?\n\r";
	
	
	public String getName() {
		return "say";
	}


	public void execute(Player player, String argument) {
		if (argument != null) {
			player.broadcast(String.format(MESSAGE, argument), null);
		} else {
			player.send(INVALID);
		}
	}


}
