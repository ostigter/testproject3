package ozmud.commands;


import ozmud.world.Creature;


public class QuitCommand extends AbstractCommand {
	
	
	protected static final String NAME = "quit";


	@Override
	public void execute(Creature sender, String argument) {
		if (argument == null) {
			String message = "${sender} vanishes${s} in thin air.";
			sender.getRoom().broadcast(message, sender, null);
		} else {
			sender.processMessage("You can't quit that.");
		}
	}


}
