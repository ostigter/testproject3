package ozmud.commands;


import ozmud.world.Creature;


public class SayCommand implements Command {
	
	
	public String getName() {
		return "say";
	}


	public void execute(Creature sender, String argument) {
		if (argument != null) {
			String message = "${sender} say${s}: " + argument; 
			sender.getRoom().broadcast(message, sender, null);
		} else {
			sender.send("Say what?");
		}
	}


}
