package th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

/**
 * Console version of a Texas Hold'em client.
 * 
 * @author Oscar Stigter
 */
public class ConsoleClient implements Client {
	
	/** The size of the big blind. */
	private static final int BIG_BLIND = 2;

	/** The amount of starting cash per player. */
	private static final int STARTING_CASH = 100;
	
	/** The console reader. */
	private final BufferedReader consoleReader;
    
	/**
	 * Constructor.
	 */
	public ConsoleClient() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
		Table table = new Table(BIG_BLIND);
		table.addPlayer(new Player("Player", STARTING_CASH, this));
		table.addPlayer(new Player("Joe",    STARTING_CASH, new DummyBot()));
		table.addPlayer(new Player("Mike",   STARTING_CASH, new DummyBot()));
		table.addPlayer(new Player("Eddie",  STARTING_CASH, new DummyBot()));
		table.start();
	}

	/**
	 * Application's entry point.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		new ConsoleClient();
	}
	
	@Override
	public void messageReceived(String message) {
		System.out.println(message);
	}

	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
	}

	@Override
	public void handStarted(Player dealer) {
		System.out.format("New hand, %s is the dealer.\n", dealer);
	}

	@Override
	public void actorRotated(Player actor) {
		System.out.format("It's %s's turn to act.\n", actor);
	}

	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
		System.out.format("Board: %s, Bet: %d, Pot: %d\n", cards, bet, pot);
	}

	@Override
	public void holeCardsUpdated(Card[] cards) {
//		if (cards.length == 2) {
//			System.out.format("Hole cards: %s %s\n", cards[0], cards[1]);
//		}
	}

	@Override
	public void playerActed(Player player) {
		System.out.format("%s %s.\n", player, player.getAction().getVerb());
	}

    @Override
	public Action act(Set<Action> actions) {
    	StringBuilder sb = new StringBuilder("Please select an action: ");
    	int i = actions.size();
    	for (Action action : actions) {
    		sb.append(action);
    		i--;
    		if (i > 1) {
    			sb.append(", ");
    		} else if (i == 1) {
    			sb.append(" or ");
    		} else {
    			// No more actions.
    		}
    	}
    	sb.append("? ");
    	String prompt = sb.toString();
    	Action selectedAction = null;
    	while (selectedAction == null) {
        	System.out.print(prompt);
            try {
                String input = consoleReader.readLine();
                if (input != null) {
	                for (Action action : actions) {
	                	String command = action.toString().toLowerCase();
	                	if (command.startsWith(input.toLowerCase())) {
	                		selectedAction = action;
	                		break;
	                	}
	                }
	                if (selectedAction == null) {
	                	System.out.println("Invalid action -- please try again.");
	                }
                }
            } catch (IOException e) {
            	// The VM is killed; ignore.
            }
    	}
    	return selectedAction;
	}

}
