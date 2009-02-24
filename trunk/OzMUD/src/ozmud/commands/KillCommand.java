package ozmud.commands;


import ozmud.world.Creature;
import ozmud.world.Player;
import ozmud.world.Room;


/**
 * Command 'kill' to attack a creature in the same room.
 * 
 * @author Oscar Stigter
 */
public class KillCommand implements Command {
	
	
	/** Error message when no target is specified. */
	private static final String NO_TARGET = "${GRAY}Kill who?\n\r";
	
	/** Error message when the creature cannot be found. */
	private static final String NOT_FOUND =
			"${GRAY}You don't see such a creature.\n\r";
	
	/** Error message when the creature is already being fought. */
	private static final String ALREADY_FIGHTING =
			"${GRAY}You are already fighting %s.\n\r";
	
	/** Emote when the creature is attacked. */
	private static final String ATTACK =
			"${CYAN}${sender} attack${s} ${target}!\n\r";
	
	/** Emote when another creature is attacked. */
	private static final String ATTACK_OTHER =
			"${CYAN}${sender} turn${s} to attack ${target}!\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "kill";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return "k";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String target) {
		if (target == null) {
			player.send(NO_TARGET);
		} else {
			Room room = player.getRoom();
			Creature creature = room.getCreature(target);
			if (creature == null) {
				player.send(NOT_FOUND);
			} else {
				Creature opponent = player.getOpponent();
				if (opponent != null) {
					if (opponent.equals(creature)) {
						player.send(String.format(
								ALREADY_FIGHTING, creature.getShortName()));
					} else {
						player.broadcast(ATTACK_OTHER, creature);
					}
				} else {
					player.broadcast(ATTACK, creature);
				}
				player.setOpponent(creature);
				if (creature.getOpponent() == null) {
					creature.setOpponent(player);
				}
			}
		}
	}
	
}
