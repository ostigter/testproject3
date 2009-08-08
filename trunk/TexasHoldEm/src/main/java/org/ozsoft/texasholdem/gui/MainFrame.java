package org.ozsoft.texasholdem.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.Deck;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.actions.Action;
import org.ozsoft.texasholdem.actions.BetAction;
import org.ozsoft.texasholdem.actions.CallAction;
import org.ozsoft.texasholdem.actions.FoldAction;
import org.ozsoft.texasholdem.actions.RaiseAction;
import org.ozsoft.texasholdem.bots.DummyBot;

/**
 * The game's main frame.
 * 
 * @author Oscar Stigter
 */
public class MainFrame extends JFrame {
    
	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The number of players at the table. */
	private static final int NO_OF_PLAYERS = 4;
	
    /** The starting cash per player. */
    private static final int STARTING_CASH = 100;
    
    /** The minimum bet in dollars. */
    private static final int MIN_BET = 2;
    
    /** The polling delay in ms when waiting for a player to act. */
    private static final long POLLING_DELAY = 100L;
    
    private final GridBagConstraints gc = new GridBagConstraints();
    private final Deck deck = new Deck();
    private final Card[] boardCards = new Card[5];
    private final BoardPanel boardPanel = new BoardPanel(this);
    private Player[] players;
    private PlayerPanel[] playerPanels;
    private int noOfBoardCards;
    private int handNumber;
    private int dealer;
    private int actor;
    private int bet;
    private int pot;
    private boolean waitingForPlayer = false;
    private boolean gameOver = false;
    
    public MainFrame() {
        super("Texas Hold'em Limit poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UIConstants.TABLE_COLOR);
        setLayout(new GridBagLayout());
        players = new Player[] {
            new HumanPlayer("Buffy",    STARTING_CASH),
            new HumanPlayer("Willow",   STARTING_CASH),
            new HumanPlayer("Xander",   STARTING_CASH),
            new HumanPlayer("Anya",     STARTING_CASH),
//            new DummyBot("Giles",    STARTING_CASH),
//            new DummyBot("Wesley",   STARTING_CASH),
//            new DummyBot("Cordelia", STARTING_CASH),
//            new DummyBot("Angel",    STARTING_CASH),
//            new DummyBot("Spike",    STARTING_CASH),
//            new DummyBot("Drusilla", STARTING_CASH),
//            new DummyBot("Darla",    STARTING_CASH),
        };
        playerPanels = new PlayerPanel[NO_OF_PLAYERS];
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
            playerPanels[i] = new PlayerPanel(players[i]);
        }
        
        // 4 player table.
        addComponent(boardPanel,      1, 1, 1, 1);
        addComponent(playerPanels[0], 1, 0, 1, 1);
        addComponent(playerPanels[1], 2, 1, 1, 1);
        addComponent(playerPanels[2], 1, 2, 1, 1);
        addComponent(playerPanels[3], 0, 1, 1, 1);
        
//        // 10 player table.
//        addComponent(boardPanel,      1, 1, 3, 3);
//        addComponent(playerPanels[0], 1, 0, 1, 1);
//        addComponent(playerPanels[1], 2, 0, 1, 1);
//        addComponent(playerPanels[2], 3, 0, 1, 1);
//        addComponent(playerPanels[3], 4, 1, 1, 1);
//        addComponent(playerPanels[4], 4, 3, 1, 1);
//        addComponent(playerPanels[5], 3, 4, 1, 1);
//        addComponent(playerPanels[6], 2, 4, 1, 1);
//        addComponent(playerPanels[7], 1, 4, 1, 1);
//        addComponent(playerPanels[8], 0, 3, 1, 1);
//        addComponent(playerPanels[9], 0, 1, 1, 1);

//      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//      setSize(dimension.width, dimension.height - 30);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
        
        runGame();
    }
    
	/**
	 * Adds a UI component.
	 * 
	 * @param component
	 *            The component.
	 * @param x
	 *            The column.
	 * @param y
	 *            The row.
	 * @param width
	 *            The number of columns to span.
	 * @param height
	 *            The number of rows to span.
	 */
    private void addComponent(Component component, int x, int y, int width, int height) {
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        getContentPane().add(component, gc);
    }
    
    /**
     * Main loop of the game.
     */
    private void runGame() {
    	// Initialize the game.
    	handNumber = 0;
    	dealer = -1;
    	actor = -1;
    	
    	// Show welcome text.
        boardPanel.setMessage("Welcome to Texas Hold'em Limit poker!");
    	getInput(ControlPanel.CONTINUE);
    	
        // Main game loop, playing hands until all but one player are broke.
    	while (!gameOver) {
        	
            resetHand();
            
            // Rotate dealer.
            rotateDealer();
            boardPanel.setMessage(String.format("%s is the dealer.", players[dealer].getName()));
        	getInput(ControlPanel.CONTINUE);
            
            // Post the small blind.
            actor = dealer;
            rotateActor();
            players[actor].postSmallBlind(MIN_BET / 2);
            pot += MIN_BET / 2;
            boardPanel.setPot(pot);
            boardPanel.setMessage(players[actor].getName() + " posts the small blind.");
            playerPanels[actor].update();
        	getInput(ControlPanel.CONTINUE);
            
            // Post the big blind.
            rotateActor();
            players[actor].postBigBlind(MIN_BET);
            pot += MIN_BET;
            boardPanel.setPot(pot);
            boardPanel.setMessage(players[actor].getName() + " posts the big blind.");
            playerPanels[actor].update();
        	getInput(ControlPanel.CONTINUE);
            
            // Deal the Hole Cards.
            for (Player player : players) {
                player.setCards(deck.deal(2));
            }
            for (int i = 0; i < NO_OF_PLAYERS; i++) {
            	playerPanels[i].update();
            }
            boardPanel.setMessage(players[dealer].getName() + " deals the Hole Cards.");
        	getInput(ControlPanel.CONTINUE);
            
            // Pre-Flop betting round.
            doBettingRound();
            
            // Deal the Flop Cards.
            noOfBoardCards = 3;
            for (int i = 0; i < noOfBoardCards; i++) {
                boardCards[i] = deck.deal();
            }
            boardPanel.setCards(boardCards, noOfBoardCards);
            boardPanel.setMessage(players[dealer].getName() + " deals the Flop.");
        	getInput(ControlPanel.CONTINUE);
            
            // Flop betting round.
        	actor = dealer;
            doBettingRound();
            
            //FIXME: Stop here for now.
            gameOver = true;
        }
    	
        boardPanel.setMessage("Game over.");
        boardPanel.setActions(ControlPanel.NONE);
    }
    
    /**
     * Performs a betting round.
     */
    private void doBettingRound() {
        bet = MIN_BET;
        // Count the number of active players left in this hand.
        int activePlayers = 0;
        for (Player player : players) {
        	if (!player.isBroke() && !player.hasFolded()) {
        		activePlayers++;
        	}
        }
        // Keep record of how many players still have to act.
        int playersToAct = activePlayers;
        // Keep going round the table until all players have acted.
        while (playersToAct > 0) {
        	rotateActor();
            Player player = players[actor];
            // Only allow active players.
            if (!player.hasFolded() && !player.isBroke()) {
            	act(player);
                playersToAct--;
                Action action = player.getAction();
                if (action instanceof FoldAction && playersToAct == 1) {
                    // The last remaining player wins.
                	//TODO: Last remaining player wins.
                } else if (action instanceof CallAction) {
                	int amount = ((CallAction) action).getAmount(); 
                	pot += amount;
                } else if (action instanceof BetAction) {
                    int amount = ((BetAction) action).getAmount();
                    bet = amount;
                    pot += amount;
                    // Make sure other players must react.
                    playersToAct = activePlayers;
                } else if (action instanceof RaiseAction) {
                	int amount = ((RaiseAction) action).getAmount();
                    bet += amount;
                    pot += amount;
                    // Make sure other players must react.
                    playersToAct = activePlayers;
                } else {
                	// Player checked.
                }
            }
        }
        for (Player player : players) {
        	player.reset();
        }
    }
    
	/**
	 * Lets a player act her turn.
	 * 
	 * @param player
	 *            The player.
	 */
    private void act(Player player) {
    	boardPanel.setMessage(String.format("%s's turn.", player));
    	int action = -1;
    	
    	if (player instanceof DummyBot) {
        	player.performAction(boardCards, noOfBoardCards, MIN_BET, bet);
        	boardPanel.setMessage(String.format("%s %s.", player, player.getAction().getVerb()));
        	playerPanels[actor].update();
        	boardPanel.setPot(pot);
        	getInput(ControlPanel.CONTINUE);
    	} else {
	    	// Determine action.
	    	if (bet == 0) {
	            // Check, Bet or Fold.
	        	action = getInput(ControlPanel.CHECK_BET_FOLD);
	        } else if (player.getBet() < bet) {
	            // Call, Raise or Fold.
	        	action = getInput(ControlPanel.CALL_RAISE_FOLD);
	        } else {
	            // Check, Raise or Fold.
	        	action = getInput(ControlPanel.CHECK_RAISE_FOLD);
	        }
	
	    	// Perform action.
	    	if (action == ControlPanel.CHECK) {
	        	player.check();
	        } else if (action == ControlPanel.CALL) {
	        	player.call(bet);
	        } else if (action == ControlPanel.BET) {
	        	player.bet(MIN_BET);
	        } else if (action == ControlPanel.RAISE) {
	        	player.raise(bet, MIN_BET);
	        } else {
	        	player.fold();
	        }
	    	
	    	// Update screen.
        	playerPanels[actor].update();
        	boardPanel.setPot(pot);
    	}
    }
    
	/**
	 * Asks the human player to select an action, and returns the action.
	 * 
	 * @param actions
	 *            The allowed actions.
	 * 
	 * @return The selected action.
	 */
    private int getInput(int actions) {
        boardPanel.setActions(actions);
        waitingForPlayer = true;
        try {
            while (waitingForPlayer) {
                Thread.sleep(POLLING_DELAY);
            }
        } catch (InterruptedException e) {
            // Ignore.
        }
        return boardPanel.getAction();
    }
    
    public void playerActed() {
        waitingForPlayer = false;
    }
    
    private void resetHand() {
    	handNumber++;
    	noOfBoardCards = 0;
    	pot = 0;
    	bet = 0;
    	boardPanel.setPot(pot);
        for (Player player : players) {
        	player.reset();
        }
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
        	playerPanels[i].update();
        }
    	deck.shuffle();
    }
    
    private void rotateDealer() {
        if (dealer != -1) {
            playerPanels[dealer].setDealer(false);
        }
        dealer = (dealer + 1) % NO_OF_PLAYERS;
        playerPanels[dealer].setDealer(true);
    }
    
    private void rotateActor() {
        if (actor != -1) {
            playerPanels[actor].setInTurn(false);
        }
        actor = (actor + 1) % NO_OF_PLAYERS;
        playerPanels[actor].setInTurn(true);
    }
    
}
