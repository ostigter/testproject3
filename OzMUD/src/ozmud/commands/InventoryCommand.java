package ozmud.commands;


import java.util.Set;

import ozmud.world.Item;
import ozmud.world.Player;


/**
 * Command 'inventory' to list the player's inventory.
 * 
 * @author Oscar Stigter
 */
public class InventoryCommand implements Command {


	/** Message when an argument is specified. */
	private static final String INVALID = "${GRAY}What?\n\r";
	
	/** Message when no items are present. */
	private static final String EMPTY =
			"${GRAY}You are not carrying any items.\n\r";
	

	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "inventory";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return "i";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument != null) {
			player.send(INVALID);
		} else {
			Set<Item> items = player.getItems();
			if (items.size() == 0) {
				player.send(EMPTY);
			} else {
				player.send("${GRAY}You are carrying:\n\r");
				for (Item item : items) {
					player.send(String.format("  %s\n\r", item.getFullName()));
				}
			}
		}
	}
	

}
