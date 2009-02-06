package ozmud.commands;


import ozmud.world.Item;
import ozmud.world.Player;


/**
 * Command 'drop' to drop one or more carried items from the inventory.
 * 
 * @author Oscar Stigter
 */
public class DropCommand implements Command {
	
	
	/** Message when no item is specified. */
	private static final String NO_TARGET = "${GRAY}Drop what?\n\r";
	
	/** Reference to all items. */
	private static final String ALL = "all";
	
	/** Message when the inventory is empty. */
	private static final String EMPTY =
			"${GRAY}You aren't carrying anything that can be dropped.\n\r";
	
	/** Message when the item cannot be found in the carried inventory. */
	private static final String NOT_FOUND =
			"${GRAY}You aren't carrying it.\n\r";
	
	/** Message when the item is wielded. */
	private static final String WIELDING =
			"${GRAY}You are wielding it.\n\r";
	
	/** Message when the item is worn. */
	private static final String WEARING =
			"${GRAY}You are wearing it.\n\r";
	
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
			// Drop all items.
			if (target.equals(ALL)) {
				Item[] items = player.getCarriedItems();
				if (items.length == 0) {
					player.send(EMPTY);
				} else {
					for (Item item : items) {
						drop(player, item);
					}
				}
			} else {
				// Drop a specific items.
				Item item = player.getItem(target);
				if (item == null) {
					player.send(NOT_FOUND);
				} else {
					if (player.isWielding(item)) {
						player.send(WIELDING);
					} else if (player.isWearing(item)) {
						player.send(WEARING);
					} else {
						drop(player, item);
					}
				}
			}
		}
	}
	
	
	/**
	 * Drops an item.
	 * 
	 * @param player  The player
	 * @param item    The item
	 */
	private void drop(Player player, Item item) {
		player.removeCarriedItem(item);
		player.getRoom().addItem(item);
		player.broadcast(
				String.format(DROP, item.getFullName()), null);
	}
	

}
