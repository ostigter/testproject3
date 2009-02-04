package ozmud.commands;


import ozmud.world.Item;
import ozmud.world.Player;


/**
 * Command 'drop' to drop an item from the player's inventory.
 * 
 * @author Oscar Stigter
 */
public class DropCommand implements Command {
	
	
	/** Message when no item is specified. */
	private static final String NO_TARGET = "${GRAY}Drop what?\n\r";
	
//	/** Reference to all items. */
//	private static final String ALL = "all";
	
	/** Message when the item cannot be found in the inventory. */
	private static final String NOT_FOUND = "${GRAY}You aren't carrying it.\n\r";
	
	/** Message when the item is worn. */
	private static final String WORN = "${GRAY}You are wearing it.\n\r";
	
	/** Message when an item is dropped. */
	private static final String DROP = "${CYAN}${sender} drop${s} %s.\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "drop";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return null;
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String target) {
		if (target == null) {
			player.send(NO_TARGET);
		} else {
			Item item = player.getItem(target);
			if (item == null) {
				player.send(NOT_FOUND);
			} else {
				if (player.isWearing(item)) {
					player.send(WORN);
				} else {
					player.removeItem(item);
					player.getRoom().addItem(item);
					player.broadcast(
							String.format(DROP, item.getFullName()), null);
				}
			}
		}
	}
	

}
