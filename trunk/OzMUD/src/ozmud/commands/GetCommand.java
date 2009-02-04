package ozmud.commands;


import java.util.List;

import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.Room;


/**
 * Command 'get' to get an item from the room.
 * 
 * @author Oscar Stigter
 */
public class GetCommand implements Command {
	
	
	/** Message when no target is specified. */
	private static final String NO_TARGET = "${GRAY}Get what?\n\r";
	
	/** Reference to all items in the room. */
	private static final String ALL = "all";
	
	/** Message when the item cannot be taken. */
	private static final String INVALID = "${GRAY}You can't get that.\n\r";
	
	/** Message when the item cannot be found. */
	private static final String NOT_FOUND = "${GRAY}You don't see it.\n\r";
	
	/** Error message when nothing can be found. */
	private static final String NOTHING =
			"${GRAY}There is nothing to get.\n\r";
	
	/** Emote when the item is taken. */
	private static final String TAKEN = "${CYAN}${sender} get${s} %s.\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "get";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return "g";
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
			if (target.equals(ALL)) {
				// Try to get all items from the room.
				Item[] items = room.getItems();
				if (items.length == 0) {
					player.send(NOTHING);
				} else {
					for (Item item : items) {
						get(player, item);
					}
				}
			} else {
				// Take a specific item.
				if (room.getCreature(target) != null) {
					player.send(INVALID);
				} else {
					Item item = room.getItem(target);
					if (item == null) {
						player.send(NOT_FOUND);
					} else {
						get(player, item);
					}
				}
			}
		}
	}
	
	
	private void get(Player player, Item item) {
		player.getRoom().removeItem(item);
		player.addItem(item);
		player.broadcast(String.format(TAKEN, item.getFullName()), null);
	}
	

}
