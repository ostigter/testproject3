package ozmud.commands;


import java.util.HashMap;
import java.util.Map;

import ozmud.world.Player;


/**
 * Command interpreter.
 * 
 * @author Oscar Stigter
 */
public class CommandInterpreter {
	
	
	/** Message for an unknown command. */
	private static final String UNKNOWN_COMMAND = "What?\n\r";
	
	/** Commands mapped by their name. */
	private final Map<String, Command> commands;
	
	/** Commands mapped by their (optional) alias. */
	private final Map<String, Command> aliasses;
	
	
	public CommandInterpreter() {
		commands = new HashMap<String, Command>();
		aliasses = new HashMap<String, Command>();
		populateCommands();
	}
	
	
	/**
	 * Parses a command line typed by a player and tries to execute the
	 * appropriate command with optional argument part.
	 *  
	 * @param player  the player
	 * @param line    the command line
	 */
	public void executeCommand(Player player, String line) {
		if (line == null) {
			// Empty line; do nothing.
		} else {
			line = line.trim();
			if (line.length() == 0) {
				// Empty line; do nothing.
			} else {
				String name = null;
				String argument = null;
				int p = line.indexOf(' ');
				if (p == -1) {
					// No arguments.
					name = line;
				} else {
					// Split command name and argument parts.
					name = line.substring(0, p);
					argument = line.substring(p + 1);
				}
				name = name.toLowerCase();
				Command command = aliasses.get(name);
				if (command == null) {
					command = commands.get(name);
				}
				if (command != null) {
					command.execute(player, argument);
				} else {
					// Unknown command.
					player.send(UNKNOWN_COMMAND);
				}
			}
		}
	}
	
	
	/**
	 * Populates the list of known commands.
	 */
	private void populateCommands() {
		// TODO: Use reflection to automatically populate commands
		addCommand(new LookCommand());
		addCommand(new SayCommand());
		addCommand(new QuitCommand());
	}
	
	
	/**
	 * Adds a command.
	 * 
	 * @param command  the command
	 */
	private void addCommand(Command command) {
		commands.put(command.getName(), command);
		String alias = command.getAlias();
		if (alias != null) {
			aliasses.put(alias, command);
		}
	}


}
