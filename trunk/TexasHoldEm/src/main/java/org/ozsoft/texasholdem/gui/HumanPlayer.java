package org.ozsoft.texasholdem.gui;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Player;

/**
 * A human controlled player.
 * 
 * @author Oscar Stigter
 */
public class HumanPlayer extends Player {

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 * @param cash
	 *            The starting amount of cash.
	 */
	public HumanPlayer(String name, int cash) {
		super(name, cash);
	}

	/*
	 * (non-Javadoc)
	 * @see cards.poker.texasholdem.Player#performAction(cards.Card[], int, int, int)
	 */
	@Override
	public void performAction(Card[] board, int noOfBoardCards, int minBet, int currentBet) {
		//FIXME: Move logic from MainFrame here.
	}

}
