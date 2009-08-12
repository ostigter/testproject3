package th;

import java.util.ArrayList;
import java.util.List;

/**
 * Console version of the game.
 * 
 * @author Oscar Stigter
 */
public class ConsoleGame implements GameListener {
	
	/** The size of the big blind. */
	private static final int BLIND = 2;

	/** The amount of starting cash per player. */
	private static final int STARTING_CASH = 100;
	
	public ConsoleGame() {
		// Create some players.
		List<Player> players = new ArrayList<Player>();
		players.add(new ConsolePlayer("Player 1", STARTING_CASH));
		players.add(new ConsolePlayer("Player 2", STARTING_CASH));
		players.add(new ConsolePlayer("Player 3", STARTING_CASH));
		players.add(new ConsolePlayer("Player 4", STARTING_CASH));

		// Play the game.
		GameEngine engine = new GameEngine(BLIND, players);
		engine.addListener(this);
		engine.run();
	}

	public static void main(String[] args) {
		new ConsoleGame();
	}
	
	@Override
	public void boardUpdated(int hand, Card[] cards, int noOfCards, int bet, int pot) {
	}

	@Override
	public void playerActed(PlayerInfo playerInfo) {
	}

	@Override
	public void messageReceived(String message) {
		System.out.println(message);
	}

}
