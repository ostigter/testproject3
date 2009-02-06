package ozmud.commands;


import ozmud.world.Equipment;
import ozmud.world.EquipmentSlot;
import ozmud.world.Player;


/**
 * Command 'equipment' to list the equipment the player is currently using.
 * This includes worn clothing, armour and wielded weapons.
 * 
 * @author Oscar Stigter
 */
public class EquipmentCommand implements Command {


	/** Message when an argument is specified. */
	private static final String INVALID = "${GRAY}What?\n\r";
	

	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "equipment";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		return "eq";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument != null) {
			player.send(INVALID);
		} else {
			player.send("${GRAY}You are using the following equipment:\n\r${MAGENTA}");
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				Equipment eq = player.getEquipment(slot);
				String eqName;
				if (eq != null) {
					eqName = "${MAGENTA}" + eq.getFullName();
				} else {
					eqName = "-";
				}
				player.send(String.format("  ${GRAY}%-15s%s\n\r",
						slot.getName(), eqName));
			}
		}
	}
	

}
