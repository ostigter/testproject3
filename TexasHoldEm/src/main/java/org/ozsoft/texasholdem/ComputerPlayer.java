package org.ozsoft.texasholdem;

/**
 * A computer controlled player.
 * 
 * The current implementation simply always Checks or Calls.
 * 
 * @author Oscar Stigter
 */
public class ComputerPlayer extends Player {

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 * @param money
	 *            The initial amount of money.
	 */
	public ComputerPlayer(String name, int money) {
		super(name, money);
	}

	/*
	 * (non-Javadoc)
	 * @see cards.poker.texasholdem.Player#performAction(cards.Card[], int, int, int)
	 */
	@Override
	public void performAction(Card[] board, int noOfBoardCards, int minBet, int currentBet) {
        if (currentBet == 0) {
            // No previous bets -- Check, Bet or Fold.
        	//FIXME: For now, always Check.
        	check();
        } else if (bet < currentBet) {
            // Must bet to proceed -- Call, Raise or Fold.
        	//FIXME: For now, always Call.
        	call(currentBet);
        } else {
            // Sufficient bet -- Check, Raise or Fold.
        	//FIXME: For now, always Check.
        	check();
        }
	}

}
