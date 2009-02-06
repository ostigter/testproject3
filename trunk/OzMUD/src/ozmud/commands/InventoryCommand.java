package ozmud.commands;


import java.util.LinkedList;
import java.util.List;

import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.Weapon;


/**
 * Command 'inventory' to list the items the player is carrying.
 * It does not list equipment (worn items like clothing, armour or weapons).
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
			// List carried items.
			for (Item item : player.getCarriedItems()) {
				inventory.add(item);
			}
			if (inventory.size() == 0) {
				player.send(EMPTY);
			} else {
				player.send("${GRAY}You are carrying:\n\r${MAGENTA}");
				for (Item item : inventory) {
					player.send(String.format("  %s\n\r", item.getFullName()));
				}
			}
		}
	}
	

}
