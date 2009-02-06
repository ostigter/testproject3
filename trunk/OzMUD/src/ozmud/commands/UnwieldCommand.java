package ozmud.commands;


import ozmud.world.Item;
import ozmud.world.Player;


/**
 * Command 'unwield' to remove a wielded weapon.
 * 
 * @author Oscar Stigter
 */
public class UnwieldCommand implements Command {
	
	
	/** Error message when the item can not be found. */
	private static final String NOT_FOUND =
			"${GRAY}You don't see it.\n\r";
	
	/** Error message when the item is not being wielded. */
	private static final String NOT_WIELDED =
			"${GRAY}You are not wielding it.\n\r";
	
	/** Emote when the weapon is unwielded. */
	private static final String UNWIELD =
			"${CYAN}${sender} unwield${s} %s.\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "unwield";
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
		Item item = null;
		if (target != null) {
			item = player.getItem(target);
		} else {
			item = player.getWeapon();
		}
		if (item == null) {
			player.send(NOT_FOUND);
		} else {
			if (!player.isWielding(item)) {
				player.send(NOT_WIELDED);
			} else {
				player.setWeapon(null);
				player.addCarriedItem(item);
				player.broadcast(
						String.format(UNWIELD, item.getFullName()), null);
			}
		}
	}
	
	
}
