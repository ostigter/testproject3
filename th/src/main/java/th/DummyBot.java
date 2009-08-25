package th;

import java.util.List;
import java.util.Set;

/**
 * Very simplistic implementation of a bot that always checks or calls (in that
 * order).
 * 
 * @author Oscar Stigter
 */
public class DummyBot extends Bot {

	/*
	 * (non-Javadoc)
	 * @see th.Client#messageReceived(java.lang.String)
	 */
	@Override
	public void messageReceived(String message) {
		// Not implemented.
	}

	/*
	 * (non-Javadoc)
	 * @see th.Client#joinedTable(int, java.util.List)
	 */
	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
		// Not implemented.
	}

	/*
	 * (non-Javadoc)
	 * @see th.Client#handStarted(th.Player)
	 */
	@Override
	public void handStarted(Player dealer) {
		// Not implemented.
	}

	/*
	 * (non-Javadoc)
	 * @see th.Client#actorRotated(th.Player)
	 */
	@Override
	public void actorRotated(Player actor) {
		// Not implemented.
	}

	/*
	 * (non-Javadoc)
	 * @see th.Client#boardUpdated(java.util.List, int, int)
	 */
	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
		// Not implemented.
	}

	/*
	 * (non-Javadoc)
	 * @see th.Client#holeCardsUpdated(th.Card[])
	 */
	@Override
	public void holeCardsUpdated(Card[] cards) {
		// Not implemented.
	}

	/*
	 * (non-Javadoc)
	 * @see th.Client#playerActed(th.Player)
	 */
	@Override
	public void playerActed(Player player) {
		// Not implemented.
	}
	
	/*
	 * (non-Javadoc)
	 * @see th.Client#act(java.util.Set)
	 */
	@Override
	public Action act(Set<Action> actions) {
		if (actions.contains(Action.CHECK)) {
			return Action.CHECK;
		} else {
			return Action.CALL;
		}
	}

}
