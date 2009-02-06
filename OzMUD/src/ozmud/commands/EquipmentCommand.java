package ozmud.commands;


import java.util.LinkedList;
import java.util.List;

import ozmud.world.Item;
import ozmud.world.Player;
import ozmud.world.Weapon;


/**
 * Command 'equipment' to list the player's equipment (wielded weapon and worn
 * items).
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
			player.send("${GRAY}You are using the following equipment:\n\r");
			
		}
	}
	

}
