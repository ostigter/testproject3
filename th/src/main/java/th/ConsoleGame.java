package th;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Console version of the game.
 * 
 * @author Oscar Stigter
 */
public class ConsoleGame {
	
	/** The size of the big blind. */
	private static final int BIG_BLIND = 2;

	/** The amount of starting cash per player. */
	private static final int STARTING_CASH = 100;
	
	/**
	 * Constructor.
	 */
	public ConsoleGame() {
		GameEngine engine = new GameEngine(BIG_BLIND);
		engine.addPlayer(new Player("Player 1", STARTING_CASH, new ConsoleClient()));
		engine.addPlayer(new Player("Player 2", STARTING_CASH, new ConsoleClient()));
		engine.addPlayer(new Player("Player 3", STARTING_CASH, new ConsoleClient()));
		engine.addPlayer(new Player("Player 4", STARTING_CASH, new ConsoleClient()));
		engine.start();
	}

	/**
	 * Application's entry point.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		new ConsoleGame();
	}
	
}
