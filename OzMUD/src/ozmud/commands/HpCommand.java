package ozmud.commands;


import ozmud.world.Player;


/**
 * Command 'hp' to show the current vital stats.
 * 
 * @author Oscar Stigter
 */
public class HpCommand implements Command {
	
	
	/** Error message when specified an argument. */
	private static final String INVALID = "What?\n\r";
	
	/** The message to display. */
	private static final String MESSAGE =
			"${GRAY}[HP: %s%d ${GRAY}/ ${GREEN}%d${GRAY}]\n\r";

	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getName()
	 */
	public String getName() {
		return "hp";
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#getAlias()
	 */
	public String getAlias() {
		// No alias.
		return null;
	}

	
	/*
	 * (non-Javadoc)
	 * @see ozmud.commands.Command#execute(ozmud.world.Player, java.lang.String)
	 */
	public void execute(Player player, String argument) {
		if (argument != null) {
			player.send(INVALID);
		} else {
			int hp = player.getHitpoints();
			int maxHp = player.getMaximumHitpoints();
			double ratio = (double) hp / (double) maxHp;
			String color;
			if (ratio < 0.25) {
				color = "${RED}";
			} else if (ratio < 0.5) {
				color = "${YELLOW}";
			} else {
				color = "${GREEN}";
			}
			player.send(String.format(MESSAGE, color, hp, maxHp));
		}
	}


}
