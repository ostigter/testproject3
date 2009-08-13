package th;

import java.util.List;
import java.util.Set;

/**
 * Client interface of a Texas Hold'em player.
 * 
 * @author Oscar Stigter
 */
public interface PlayerClient {

	/**
	 * Asks a player to select a poker action.
	 * 
	 * @param actions
	 *            The allowed actions to select from.
	 * @param board
	 *            The community cards on the board.
	 * @param minBet
	 *            The minimum bet.
	 * @param currentBet
	 *            The current bet.
	 *
	 * @return The selected action.
	 */
    Action act(Set<Action> actions, List<Card> board, int minBet, int currentBet);

}
