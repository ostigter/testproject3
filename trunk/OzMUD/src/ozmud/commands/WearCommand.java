package ozmud.commands;


import ozmud.world.BodyPart;
import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.WornItem;


/**
 * Command 'wear' to wear a piece of clothing or armour.
 * 
 * @author Oscar Stigter
 */
public class WearCommand implements Command {
	
	
	/** Error message when no target is specified. */
	private static final String NO_ITEM = "${GRAY}Wear what?\n\r";
	
	/** Reference to all worn items. */
	private static final String ALL = "all";
	
	/** Error message when nothing can be found. */
	private static final String NONE =
			"${GRAY}You don't carry anything that can be worn.\n\r";
	
	/** Error message when the item cannot be found. */
	private static final String NOT_CARRIED =
			"${GRAY}You are not carrying it.\n\r";
	
	/** Error message when the item cannot be worn. */
	private static final String NOT_WORN =
			"${GRAY}You can't wear that.\n\r";
	
	/** Error message when the weapon is already worn. */
	private static final String WEARING =
			"${GRAY}You are already wearing it.\n\r";
	
	/** Emote when the previously worn item is removed. */
	private static final String REMOVE =
			"${CYAN}${sender} remove${s} %s.\n\r";
	
	/** Emote when the item is worn. */
	private static final String WEAR = "${CYAN}${sender} wear${s} %s.\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "wear";
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
		} else if (target.equals(ALL)) {
			// Try to wear all carried items.
			int worn = 0;
			Item[] items = player.getCarriedItems();
			for (Item item : items) {
				if (item instanceof WornItem) {
					wear(player, (WornItem) item);
					worn++;
				}
			}
			if (worn == 0) {
				player.send(NONE);
			}
		} else {
			Item item = player.getCarriedItem(target);
			if (item == null) {
				player.send(NOT_CARRIED);
			} else if (player.isWearing(item)) {
				player.send(WEARING);
			} else if (!(item instanceof WornItem)) {
					player.send(NOT_WORN);
			} else {
				wear(player, (WornItem) item);
			}
		}
	}
	
	
	private void wear(Player player, WornItem item) {
		BodyPart bodyPart = item.getBodyPart();
		WornItem previousItem = player.getWornItem(bodyPart);
		if (previousItem != null) {
			player.removeWornItem(bodyPart);
			player.addCarriedItem(previousItem);
			player.broadcast(String.format(
					REMOVE, previousItem.getFullName()), null);
		}
		player.removeCarriedItem(item);
		player.wearItem(item);
		player.broadcast(String.format(WEAR, item.getFullName()), null);
	}
	
	
}
