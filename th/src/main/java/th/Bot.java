package th;

import java.util.List;
import java.util.Set;

/**
 * Simplistic bot that always checks or calls.
 * 
 * @author Oscar Stigter
 */
public class Bot extends ComputerPlayer {

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 * @param cash
	 *            The starting cash.
	 */
	public Bot(String name, int cash) {
		super(name, cash);
	}

	/*
	 * (non-Javadoc)
	 * @see th.Player#act(java.util.Set, th.Card[], int, int, int)
	 */
	@Override
	public void act(Set<Action> actions, List<Card> board, int minBet, int currentBet) {
		if (actions.contains(Action.CHECK)) {
			check();
		} else {
			call(currentBet);
		}
	}

}
