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
	
	/**
	 * Constructor.
	 */
	public ConsoleGame() {
		// Create some players.
		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player 1", new ConsoleClient(), STARTING_CASH));
		players.add(new Player("Player 2", new ConsoleClient(), STARTING_CASH));
		players.add(new Player("Player 3", new ConsoleClient(), STARTING_CASH));
		players.add(new Player("Player 4", new ConsoleClient(), STARTING_CASH));

		// Play the game.
		GameEngine engine = new GameEngine(BLIND, players);
		engine.addListener(this);
		engine.run();
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
	
	/*
	 * (non-Javadoc)
	 * @see th.GameListener#boardUpdated(int, java.util.List, int, int)
	 */
	@Override
	public void boardUpdated(int hand, List<Card> cards, int bet, int pot) {
		//TODO: Broadcast board update
	}

	/*
	 * (non-Javadoc)
	 * @see th.GameListener#playerActed(th.PlayerInfo)
	 */
	@Override
	public void playerActed(PlayerInfo playerInfo) {
		//TODO: Broadcast player action
	}

	/*
	 * (non-Javadoc)
	 * @see th.GameListener#messageReceived(java.lang.String)
	 */
	@Override
	public void messageReceived(String message) {
		System.out.println(message);
	}

}
