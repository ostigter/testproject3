package th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

/**
 * Console implementation of a player client.
 * 
 * Intended for human players with a console interface.
 * 
 * @author Oscar Stigter
 */
public class ConsoleClient implements Client {
    
	/** The console reader. */
	private final BufferedReader consoleReader;
    
	/**
	 * Constructor.
	 */
    public ConsoleClient() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }
    
	@Override
	public void messageReceived(String message) {
		System.out.println(message);
	}

	@Override
	public void dealerRotated(Player dealer) {
		System.out.format("%s is the dealer.\n", dealer);
	}

	@Override
	public void actorRotated(Player actor) {
		System.out.format("It's %s's turn to act.\n", actor);
	}

	@Override
	public void boardUpdated(List<Card> cards, int bet, int pot) {
	}

	@Override
	public void holeCardsUpdated(Card[] cards) {
	}

	@Override
	public void joinedTable(int bigBlind, List<Player> players) {
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
