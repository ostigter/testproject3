package ozmud.commands;


import ozmud.world.Player;


public interface Command {
	
	
	String getName();
	
	
	void execute(Player player, String argument);
	
	
}
