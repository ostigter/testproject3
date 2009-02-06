package ozmud.commands;


import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.Weapon;


/**
 * Command 'wield' to wield a weapon.
 * 
 * @author Oscar Stigter
 */
public class WieldCommand implements Command {
	
	
	/** Error message when no target is specified. */
	private static final String NO_TARGET = "${GRAY}Wield what?\n\r";
	
	/** Error message when the item cannot be found. */
	private static final String NOT_CARRIED =
			"${GRAY}You are not carrying it.\n\r";
	
	/** Error message when the item cannot be wielded. */
	private static final String NOT_WEAPON =
			"${GRAY}You can't wield that.\n\r";
	
	/** Error message when the weapon is already wielded. */
	private static final String ALREADY_WIELDING =
			"${GRAY}You are already wielding it.\n\r";
	
	/** Emote when the current weapon is unwielded. */
	private static final String UNWIELD =
			"${CYAN}${sender} unwield${s} %s.\n\r";
	
	/** Emote when the weapon is wielded. */
	private static final String WIELD =
			"${CYAN}${sender} wield${s} %s.\n\r";
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "wield";
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
				player.send(NOT_CARRIED);
			} else if (player.isWielding(item)) {
				player.send(ALREADY_WIELDING);
			} else if (!(item instanceof Weapon)) {
					player.send(NOT_WEAPON);
			} else {
				Weapon previousWeapon = player.getWeapon(); 
				if (previousWeapon != null) {
					player.addCarriedItem(previousWeapon);
					player.broadcast(String.format(
							UNWIELD, previousWeapon.getFullName()), null);
				}
				player.removeCarriedItem(item);
				player.setWeapon((Weapon) item);
				player.broadcast(
						String.format(WIELD, item.getFullName()), null);
			}
		}
	}
	
	
}
