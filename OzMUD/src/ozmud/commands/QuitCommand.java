package ozmud.commands;


import ozmud.world.Creature;


public class QuitCommand implements Command {
	
	
	public String getName() {
		return "quit";
	}


	public void execute(Creature sender, String argument) {
		if (argument == null) {
			String message = "${sender} vanishes${s} in thin air.";
			sender.getRoom().broadcast(message, sender, null);
		} else {
			sender.send("You can't quit that.");
		}
	}


}
