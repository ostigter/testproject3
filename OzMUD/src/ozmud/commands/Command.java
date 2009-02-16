package ozmud.commands;


import ozmud.world.Player;


/**
 * A command entered by a player.
 * 
 * @author Oscar Stigter
 */
public interface Command {
	
	
	/**
	 * Returns the name (as typed by a player).
	 * 
	 * @return The name.
	 */
	String getName();
	
	
	/**
	 * Returns the alias, or null if none defined.
	 * 
	 * @return The alias, or null if none defined.
	 */
	String getAlias();
	
	
	/**
	 * Executes the command by a player with any arguments.
	 * 
	 * @param player   The player that entered the command.
	 * @param argument Any arguments.
	 */
	void execute(Player player, String argument);
	
	
}
