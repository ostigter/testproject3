package ozmud.commands;


import java.util.LinkedList;
import java.util.List;

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
			"${GRAY}You aren't carrying anything.\n\r";
	

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
			List<Item> inventory = new LinkedList<Item>();
			for (Item item : player.getCarriedItems()) {
				inventory.add(item);
			}
			for (Item item : player.getWornItems()) {
				inventory.add(item);
			}
			if (inventory.size() == 0) {
				player.send(EMPTY);
			} else {
				player.send("${GRAY}You are carrying:\n\r");
				for (Item item : inventory) {
					String name = item.getFullName();
					String message;
					if (player.isWielding(item)) {
						message = "  %s (wielded)\n\r";
					} else if (player.isWearing(item)) {
						message = "  %s (worn)\n\r"; 
					} else {
						message = "  %s\n\r"; 
					}
					player.send(String.format(message, name));
				}
			}
		}
	}
	

}
