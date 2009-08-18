package th;

import java.util.List;
import java.util.Set;

/**
 * Very simplistic implementation of a bot that always checks or calls (in that
 * order).
 * 
 * @author Oscar Stigter
 */
public class SimplisticBot extends Bot {

	/*
	 * (non-Javadoc)
	 * @see th.PlayerClient#act(java.util.Set, java.util.List, int, int)
	 */
	@Override
	public Action act(Set<Action> actions, List<Card> board, int minBet, int currentBet) {
		if (actions.contains(Action.CHECK)) {
			return Action.CHECK;
		} else {
			return Action.CALL;
		}
	}
	
}
