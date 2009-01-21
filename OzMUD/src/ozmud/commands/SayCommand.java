package ozmud.commands;


import ozmud.world.Creature;


public class SayCommand extends AbstractCommand {
	
	
	protected static final String NAME = "say";


	@Override
	public void execute(Creature sender, String argument) {
		String message = "${sender} say${s}: " + argument; 
		sender.getRoom().broadcast(message, sender, null);
	}


}
