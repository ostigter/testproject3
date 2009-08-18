package th;

/**
 * The public information of a player at the table.
 * 
 * Contains only information that can be seen by the other players.
 * 
 * @author Oscar Stigter
 */
public class PlayerInfo {
	
	private final String name;
	
	private final int cash;
	
	private final int bet;
	
	private final Action action;
	
	private final Card[] cards;
	
	public PlayerInfo(Player player, boolean showCards) {
		name = player.getName();
		cash = player.getCash();
		bet = player.getBet();
		action = player.getAction();
		if (showCards) {
			cards = player.getCards();
		} else {
			cards = null;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getCash() {
		return cash;
	}
	
	public int getBet() {
		return bet;
	}
	
	public Action getAction() {
		return action;
	}
	
	public Card[] getCards() {
		return cards;
	}

}
