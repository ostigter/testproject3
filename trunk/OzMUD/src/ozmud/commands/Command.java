package ozmud.commands;


import ozmud.world.Player;


public interface Command {
	
	
	String getName();
	
	
	String getAlias();
	
	
	void execute(Player player, String argument);
	
	
}
