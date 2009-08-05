package cards.poker.texasholdem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cards.Card;

public class HumanPlayer extends Player {
    
    private BufferedReader consoleReader;
    
    private String input;
    
    public HumanPlayer(String name, int cash) {
        super(name, cash);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    @Override // Player
    public void performAction(Card[] board, int noOfBoardCards, int minBet, int currentBet) {
        if (currentBet == 0) {
            // No previous bets.
            if (noOfBoardCards == 0) {
                // No flops yet, so Bet or Fold.
                System.out.print(name + ": Bet of Fold? ");
                getConsoleInput();
                if (input.length() != 0) {
                    if (input.charAt(0) == 'f') {
                        fold();
                    } else if (input.charAt(0) == 'b') {
                        bet(minBet);
                    } else {
                        System.out.println("Invalid action!");
                    }
                }
            } else {
                // Check, Bet or Fold.
                System.out.print(name + ": Check, Bet or Fold? ");
                getConsoleInput();
                if (input.length() != 0) {
                    if (input.charAt(0) == 'f') {
                        fold();
                    } else if (input.charAt(0) == 'c') {
                        check();
                    } else if (input.charAt(0) == 'b') {
                        bet(minBet);
                    } else {
                        System.out.println("Invalid action.");
                    }
                }
            }
        } else {
            // Call, Raise or Fold.
            System.out.print(name + ": Call, Raise of Fold? ");
            getConsoleInput();
            if (input.length() != 0) {
                if (input.charAt(0) == 'f') {
                    fold();
                }
                else if (input.charAt(0) == 'c') {
                    call(currentBet);
                } else if (input.charAt(0) == 'r') {
                    raise(currentBet, minBet);
                } else {
                    System.out.println("Invalid action.");
                }
            }
        }
    }
    
    private void getConsoleInput() {
        try {
            input = consoleReader.readLine();
        } catch (IOException ex) {
            // Ignore.
        }
    }

}
