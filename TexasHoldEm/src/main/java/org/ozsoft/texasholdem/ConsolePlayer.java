package org.ozsoft.texasholdem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A human controlled player using a console interface.
 * 
 * @author Oscar Stigter
 */
public class ConsolePlayer extends Player {
    
	/** The console reader. */
	private BufferedReader consoleReader;
    
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 * @param cash
	 *            The starting amount of cash.
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
            String response = getConsoleInput(name + ": Check, Bet or Fold? ", new String[] {"c", "b", "f"});
            if (response.equals("c")) {
                check();
            }else if (response.equals("b")) {
                bet(minBet);
            } else {
                fold();
            }
        } else if (bet < currentBet) {
            // Call, Raise or Fold.
            String response = getConsoleInput(name + ": Call, Raise or Fold? ", new String[] {"c", "r", "f"});
            if (response.equals("c")) {
                call(currentBet);
            } else if (response.equals("r")) {
                raise(currentBet, minBet);
            } else {
                fold();
            }
        } else {
            // Check, Raise or Fold.
            String response = getConsoleInput(name + ": Check, Raise or Fold? ", new String[] {"c", "r", "f"});
            if (response.equals("c")) {
            	check();
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
                if (input != null) {
	                for (String res : responses) {
	                	if (input.startsWith(res)) {
	                		response = res;
	                		break;
	                	}
	                }
	                if (response == null) {
	                	System.out.println("Invalid option -- please try again.");
	                }
                }
            } catch (IOException e) {
            	// Ignore.
            }
    	}
        return response;
    }

}
