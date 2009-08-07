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
	 * @param money
	 *            The initial amount of money.
	 */
	public HumanPlayer(String name, int money) {
		super(name, money);
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
