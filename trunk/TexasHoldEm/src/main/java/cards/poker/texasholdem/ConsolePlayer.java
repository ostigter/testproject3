package cards.poker.texasholdem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Human player with a console interface.
 * 
 * @author Oscar Stigter
 */
public class ConsolePlayer extends Player {
    
    private BufferedReader consoleReader;
    
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 * @param cash
	 *            The starting amount of money.
	 */
    public ConsolePlayer(String name, int cash) {
        super(name, cash);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    /*
     * (non-Javadoc)
     * @see cards.poker.texasholdem.Player#performAction(cards.Card[], int, int, int)
     */
    @Override
    public void performAction(Card[] board, int noOfBoardCards, int minBet, int currentBet) {
        if (currentBet == 0) {
            // No previous bets.
            if (noOfBoardCards == 0) {
                // No flops yet, so Bet or Fold.
                String response = getConsoleInput(name + ": Bet or Fold? ", new String[] {"b", "f"});
                if (response.equals("b")) {
                    bet(minBet);
                } else {
                    fold();
                }
            } else {
                // Check, Bet or Fold.
                String response = getConsoleInput(name + ": Check, Bet or Fold? ", new String[] {"c", "b", "f"});
                if (response.equals("c")) {
                	check();
                } else if (response.equals("b")) {
                    bet(minBet);
                } else {
                    fold();
                }
            }
        } else {
            // Call, Raise or Fold.
            String response = getConsoleInput(name + ": Call, Raise or Fold? ", new String[] {"c", "r", "f"});
            if (response.equals("c")) {
                call(currentBet);
            } else if (response.equals("r")) {
                raise(currentBet, minBet);
            } else {
                fold();
            }
        }
    }
    
    /**
     * Asks the user for input on the console, and returns the response.
     * 
     * @param message The message to display.
     * @param responses The allowed responses.
     */
    private String getConsoleInput(String message, String[] responses) {
    	String response = null;
    	while (response == null) {
            try {
            	System.out.print(message);
                String input = consoleReader.readLine();
                for (String res : responses) {
                	if (input.startsWith(res)) {
                		response = res;
                		break;
                	}
                }
                if (response == null) {
                	System.out.println("Invalid option -- please try again.");
                }
            } catch (IOException ex) {
                // Ignore.
            }
    	}
        return response;
    }

}
