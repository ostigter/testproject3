package ozmud.commands;


import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.WornItem;


/**
 * Command 'unwield' to remove a wielded weapon.
 * 
 * @author Oscar Stigter
 */
public class RemoveCommand implements Command {
	
	
	/** Error message when no item is specified. */
	private static final String NO_ITEM = "${GRAY}Remove what?\n\r";
	
	/** Reference to all worn items. */
	private static final String ALL = "all";
	
	/** Message when no items are worn. */
	private static final String NONE =
			"${GRAY}You aren't wearing anything.\n\r";
	
	/** Error message when the item can not be found. */
	private static final String NOT_FOUND =
			"${GRAY}You don't see it.\n\r";
	
	/** Error message when the item is not worn. */
	private static final String NOT_WORN =
			"${GRAY}You are not wearing it.\n\r";
	
	/** Emote when the worn item is removed. */
	private static final String REMOVE = "${CYAN}${sender} remove${s} %s.\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "remove";
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
			player.send(NO_ITEM);
		} else {
			if (target.equals(ALL)) {
				// Remove all worn items.
				Item[] items = player.getWornItems();
				if (items.length == 0) {
					player.send(NONE);
				} else {
					for (Item item : items) {
						remove(player, (WornItem) item);
					}
				}
			} else {
				// Remove a specific worn item.
				WornItem item = player.getWornItem(target);
				if (item == null) {
					player.send(NOT_FOUND);
				} else {
					if (!player.isWearing(item)) {
						player.send(NOT_WORN);
					} else {
						remove(player, (WornItem) item);
					}
				}
			}
		}
	}
	
	
	private void remove(Player player, WornItem item) {
		player.removeWornItem(item.getBodyPart());
		player.addCarriedItem(item);
		player.broadcast(String.format(REMOVE, item.getFullName()), null);
	}
	
	
}
