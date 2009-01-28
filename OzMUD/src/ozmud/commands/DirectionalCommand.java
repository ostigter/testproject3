package ozmud.commands;


import ozmud.world.Exit;
import ozmud.world.Player;
import ozmud.world.Room;


public class DirectionalCommand implements Command {
	
	
	private static final String ARGUMENT = "Go where?\n\r";

	private static final String INVALID = "You can't go there.\n\r";

	private final String name;
	
	private final String alias;
	
	
	public DirectionalCommand(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return name;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return alias;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument != null) {
			player.send(ARGUMENT);
		} else {
			Room room = player.getRoom();
			Exit exit = room.getExit(name);
			if (exit != null) {
				player.moveTo(exit.getRoomId());
				player.handleCommand("look");
			} else {
				player.send(INVALID);
			}
		}
	}


}
