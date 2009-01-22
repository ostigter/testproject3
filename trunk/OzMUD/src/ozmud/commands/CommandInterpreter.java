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
	
	
	private final Map<String, Command> commands;
	
	
	public CommandInterpreter() {
		commands = new HashMap<String, Command>();
		populateCommands();
	}
	
	
	public void executeCommand(Player player, String line) {
		if (line == null) {
			// TODO: Empty command.
		} else {
			line = line.trim();
			if (line.length() == 0) {
				// TODO: Empty command.
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
				Command command = commands.get(name.toLowerCase());
				if (command != null) {
					command.execute(player, argument);
				} else {
					// Unknown command.
					player.send("What?");
				}
			}
		}
	}
	
	
	private void populateCommands() {
		// TODO: Use reflection to automagically populate commands
		addCommand(new SayCommand());
		addCommand(new QuitCommand());
	}
	
	
	private void addCommand(Command command) {
		commands.put(command.getName(), command);
	}


}
