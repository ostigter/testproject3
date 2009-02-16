package ozmud.commands;


import ozmud.Util;
import ozmud.world.Creature;
import ozmud.world.Gender;
import ozmud.world.Item;
import ozmud.world.MudObject;
import ozmud.world.Player;
import ozmud.world.Room;


/**
 * Command 'look' to look around at the room or at a specific object.
 * 
 * @author Oscar Stigter
 */
public class LookCommand implements Command {
	
	
	/** Message when looking at a creature. */
	private static final String CREATURE =
			"${CYAN}${sender} look${s} at ${target}.\n\r";
		
	/** Message when trying to look at a non-existing target. */
	private static final String NOT_FOUND = "${GRAY}Look at what?\n\r";

	
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
			look(player);
		} else {
			look(player, argument);
		}
	}
	
	
	/**
	 * Look around the room (no target).
	 * 
	 * @param player  The player
	 */
	private void look(Player player) {
		Room room = player.getRoom();
		// Show room name and description.
		player.send(String.format(
				"\n\r${YELLOW}%s\n\r", room.getShortName()));
		player.send(String.format(
				"${GREEN}%s\n\r", room.getDescription()));
		// First show any items present...
		for (Item item : room.getItems()) {
			player.send(String.format(
					"${MAGENTA}  %s\n\r", item.getFullName()));
		}
		// ...then show any creatures present (besides yourself).
		for (Creature creature : room.getCreatures()) {
			if (!creature.equals(player)) {
				player.send(String.format(
						"${MAGENTA}  %s\n\r", creature.getFullName()));
			}
		}
	}


	/**
	 * Look at a specific target in the room.
	 * 
	 * @param player      The player
	 * @param targetName  The target's name or alias
	 */
	private void look(Player player, String targetName) {
		MudObject target = player.getTarget(targetName);
		if (target != null) {
			if (target instanceof Item) {
				look(player, (Item) target);
			} else if (target instanceof Creature) {
				look(player, (Creature) target);
			} else {
				// This should never happen.
				System.err.println(
						"ERROR: 'look' target not Creature or Item");
			}
		} else {
			player.send(NOT_FOUND);
		}
	}


	/**
	 * Look at a specific target in the room.
	 * 
	 * @param player      The player
	 * @param creature    The target
	 */
	private void look(Player player, Item item) {
		player.send(String.format(
				"${GREEN}%s\n\r", item.getDescription()));
	}


	/**
	 * Look at a specific creature in the room.
	 * 
	 * @param player      The player
	 * @param creature    The creature
	 */
	private void look(Player player, Creature creature) {
		player.broadcast(CREATURE, creature);
		player.send(String.format(
				"${GREEN}%s\n\r", creature.getDescription()));
		Gender gender = creature.getGender();
		String pronoun = gender.getPronoun();
		String message = creature.equals(player) ?
				String.format("You are a %s.\n\r", gender) :
					String.format("%s is a %s.\n\r", pronoun, gender);
		player.send(Util.capitalize(message));
	}


}
