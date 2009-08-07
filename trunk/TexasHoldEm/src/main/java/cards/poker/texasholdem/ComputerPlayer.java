package cards.poker.texasholdem;


public class ComputerPlayer extends Player {

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 * @param cash
	 *            The initial amount of cash.
	 */
	public ComputerPlayer(String name, int cash) {
		super(name, cash);
	}

	/*
	 * (non-Javadoc)
	 * @see cards.poker.texasholdem.Player#performAction(cards.Card[], int, int, int)
	 */
	@Override
	public void performAction(Card[] board, int noOfBoardCards, int minBet, int currentBet) {
		if (bet < currentBet) {
			// Call, Raise or Fold.
			// TODO: For now, always call.
			call(currentBet);
		} else {
			// Check, Bet/Raise or Fold.
			// TODO: For now, always check.
			check();
		}
	}

}
