package org.ozsoft.texasholdem.bots;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Player;

/**
 * The most simplistic bot possible, which always checks or calls.
 * 
 * @author Oscar Stigter
 */
public class DummyBot extends Player {

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The bot's name.
	 * @param cash
	 *            The bot's starting amount of cash.
	 */
	public DummyBot(String name, int cash) {
		super(name, cash);
	}

	/*
	 * (non-Javadoc)
	 * @see cards.poker.texasholdem.Player#performAction(cards.Card[], int, int, int)
	 */
	@Override
	public void performAction(Card[] board, int noOfBoardCards, int minBet, int currentBet) {
        if (currentBet == 0) {
            // Check, Bet or Fold.
        	//FIXME: For now, always Check.
        	check();
        } else if (bet < currentBet) {
            // Call, Raise or Fold.
        	//FIXME: For now, always Call.
        	call(currentBet);
        } else {
            // Check, Raise or Fold.
        	//FIXME: For now, always Check.
        	check();
        }
	}

}