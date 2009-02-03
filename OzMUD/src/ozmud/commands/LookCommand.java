package ozmud.commands;


import ozmud.world.Creature;
import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.Room;


/**
 * Command 'look' to look around at the room or at a specific creature or item.
 * 
 * @author Oscar Stigter
 */
public class LookCommand implements Command {
	
	
	/** Message when trying to look at a non-existing target. */
	private static final String UNKNOWN = "Look at what?\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "look";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return "l";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument == null) {
			lookAtRoom(player);
		} else {
			lookAtTarget(player, argument);
		}
	}
	
	
	private void lookAtRoom(Player player) {
		Room room = player.getRoom();
		// Show room name and description.
		player.send(String.format(
				"${YELLOW}%s\n\r", room.getShortName()));
		player.send(String.format(
				"${GREEN}%s\n\r", room.getDescription()));
		// Show items present.
		for (Item item : room.getItems()) {
			player.send(String.format(
					"  %s\n\r", item.getFullName()));
		}
		// Show creatures present (besides yourself).
		for (Creature creature : room.getCreatures()) {
			if (!creature.equals(player)) {
				player.send(String.format(
						"  %s\n\r", creature.getFullName()));
			}
		}
	}
	
	
	private void lookAtTarget(Player player, String target) {
		// Are we looking at ourselves?
		if (target.equals("me") ||
				target.equalsIgnoreCase(player.getShortName())) {
			lookAtCreature(player, player);
		} else {
			// TODO: Look at other targets.
			player.send(UNKNOWN);
		}
	}
	
	
	private void lookAtCreature(Player player, Creature creature) {
		player.send(String.format(
				"${GREEN}%s\n\r", creature.getDescription()));
	}


}
