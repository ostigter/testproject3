package ozmud.commands;


import java.util.HashMap;
import java.util.Map;

import ozmud.world.Player;


/**
 * Command interpreter that parses and executes a player's commands.
 * 
 * @author Oscar Stigter
 */
public class CommandInterpreter {
	
	
	/** Message for an unknown command. */
	private static final String UNKNOWN = "${GRAY}What?\n\r";
	
	/** Commands mapped by their name. */
	private final Map<String, Command> commands;
	
	/** Commands mapped by their (optional) alias. */
	private final Map<String, Command> aliasses;
	
	
	public CommandInterpreter() {
		commands = new HashMap<String, Command>();
		aliasses = new HashMap<String, Command>();
		init();
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
				if (command == null) {
					player.send(UNKNOWN);
				} else {
					command.execute(player, argument);
				}
			}
		}
	}
	
	
	/**
	 * Adds a command.
	 * The command is separately mapped by name and alias for performance.
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


	/**
	 * Initializes the list of commands.
	 */
	private void init() {
		// General.
		addCommand(new QuitCommand());
		// Communication.
		addCommand(new SayCommand());
		// Navigation.
		addCommand(new LookCommand());
		addCommand(new DirectionalCommand("north",     "n"));
		addCommand(new DirectionalCommand("east",      "e"));
		addCommand(new DirectionalCommand("south",     "s"));
		addCommand(new DirectionalCommand("west",      "w"));
		addCommand(new DirectionalCommand("northeast", "ne"));
		addCommand(new DirectionalCommand("southeast", "se"));
		addCommand(new DirectionalCommand("southwest", "sw"));
		addCommand(new DirectionalCommand("northwest", "nw"));
		addCommand(new DirectionalCommand("up",        "u"));
		addCommand(new DirectionalCommand("down",      "d"));
		// Equipment.
		addCommand(new InventoryCommand());
		addCommand(new GetCommand());
		addCommand(new DropCommand());
		addCommand(new EquipmentCommand());
		addCommand(new WieldCommand());
		addCommand(new UnwieldCommand());
		addCommand(new WearCommand());
		addCommand(new RemoveCommand());
	}
	
	
}
