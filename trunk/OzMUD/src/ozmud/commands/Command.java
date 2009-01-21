package ozmud.commands;


import ozmud.world.Creature;


public interface Command {
	
	
	String getName();
	
	
	void execute(Creature sender, String argument);
	
	
}
