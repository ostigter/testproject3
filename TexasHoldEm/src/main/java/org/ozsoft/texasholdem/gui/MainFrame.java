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
    
    /** The maximum number of bets and raises per player. */
    private static final int MAX_RAISES = 4;
    
    /** The maximum number of community cards on the board. */
    private static final int BOARD_SIZE = 5;
    
    /** The polling delay in ms when waiting for a player to act. */
    private static final long POLLING_DELAY = 100L;
    
    private final GridBagConstraints gc = new GridBagConstraints();
    
    /** The deck of cards. */
    private final Deck deck;
    
    /** The board with the community cards. */
    private final Card[] boardCards;
    
    /** The board panel. */
    private final BoardPanel boardPanel;
    
    /** The players. */
    private Player[] players;
    
    /** The player panels. */
    private PlayerPanel[] playerPanels;

    /** The number of dealt community cards. */
    private int noOfBoardCards;

    /** The current hand number. */
    private int hand;

    /** The current dealer position. */
    private int dealer;

    /** The current player in turn (actor). */
    private int actor;

    /** The minimum bet. */
    private int minBet;

    /** The current bet. */
    private int bet;
    
    /** The pot. */
    private int pot;
    
    /** Whether the game is waiting for a player to act. */
    private boolean waitingForPlayer;

    /** Whether the game is over. */
    private boolean gameOver;
    
    /**
     * Constructor.
     */
    public MainFrame() {
        super("Limit Texas Hold'em poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UIConstants.TABLE_COLOR);
        setLayout(new GridBagLayout());
        
        deck = new Deck();

        boardCards = new Card[BOARD_SIZE];
        boardPanel = new BoardPanel(this);        
        
        players = new Player[] {
            new HumanPlayer("Player", STARTING_CASH),
            new HumanPlayer("Joe", STARTING_CASH),
            new HumanPlayer("Mike", STARTING_CASH),
            new HumanPlayer("Eddy", STARTING_CASH),
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
	 * Adds an UI component.
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
    	hand = 0;
    	dealer = -1;
    	actor = -1;
    	gameOver = false;
    	
    	// Show welcome text.
        boardPanel.setMessage("Welcome to Limit Texas Hold'em poker!");
    	getInput(ControlPanel.CONTINUE);
    	
        // Main game loop, playing hands until all but one player are broke.
    	while (!gameOver) {
        	
            resetHand();
            
            // Rotate dealer.
            rotateDealer();
            boardPanel.setMessage(String.format("%s is the dealer.", players[dealer]));
        	getInput(ControlPanel.CONTINUE);
            
            // Post the small blind.
            actor = dealer;
            rotateActor();
            players[actor].postSmallBlind(MIN_BET / 2);
            bet = MIN_BET;
            pot += MIN_BET / 2;
            boardPanel.update(hand, bet, pot);
            boardPanel.setMessage(players[actor] + " posts the small blind.");
            playerPanels[actor].update();
        	getInput(ControlPanel.CONTINUE);
            
            // Post the big blind.
            rotateActor();
            players[actor].postBigBlind(MIN_BET);
            pot += MIN_BET;
            boardPanel.update(hand, bet, pot);
            boardPanel.setMessage(players[actor] + " posts the big blind.");
            playerPanels[actor].update();
        	getInput(ControlPanel.CONTINUE);
            
            // Deal the Hole Cards.
            for (Player player : players) {
                player.setCards(deck.deal(2));
            }
            for (int i = 0; i < NO_OF_PLAYERS; i++) {
            	playerPanels[i].update();
            }
            boardPanel.setMessage(players[dealer] + " deals the Hole Cards.");
        	getInput(ControlPanel.CONTINUE);
            
            // Pre-Flop betting round.
        	minBet = MIN_BET;
            doBettingRound();
            
            // Deal the Flop.
            boardCards[noOfBoardCards++] = deck.deal();
            boardCards[noOfBoardCards++] = deck.deal();
            boardCards[noOfBoardCards++] = deck.deal();
            boardPanel.setCards(boardCards, noOfBoardCards);
            boardPanel.setMessage(players[dealer] + " deals the Flop.");
        	getInput(ControlPanel.CONTINUE);
            
            // Flop betting round.
        	actor = dealer;
            doBettingRound();
            
            // Deal the Turn.
            boardCards[noOfBoardCards++] = deck.deal();
            boardPanel.setCards(boardCards, noOfBoardCards);
            boardPanel.setMessage(players[dealer] + " deals the Turn.");
        	getInput(ControlPanel.CONTINUE);
            
            // Turn betting round.
        	actor = dealer;
        	minBet = 2 * MIN_BET;
            doBettingRound();
            
            // Deal the River.
            boardCards[noOfBoardCards++] = deck.deal();
            boardPanel.setCards(boardCards, noOfBoardCards);
            boardPanel.setMessage(players[dealer] + " deals the River.");
        	getInput(ControlPanel.CONTINUE);
            
            // River betting round.
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
        	boolean hasActed = false;
        	rotateActor();
            Player player = players[actor];
            // Only allow active players.
            if (!player.isBroke() && !player.hasFolded() && player.getRaises() < MAX_RAISES) {
            	act(player);
            	hasActed = true;
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
                    // Make sure other players must react to the bet.
                    // This player will get another turn.
                    playersToAct = activePlayers;
                } else if (action instanceof RaiseAction) {
                	int amount = ((RaiseAction) action).getAmount();
                    bet += amount;
                    pot += amount;
                    // Make sure other players must react to the raise.
                    playersToAct = activePlayers;
                } else {
                	// Player checked.
                }
            }
            if (!hasActed) {
            	playersToAct = 0;
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
	 *            The player who's turn it is to act.
	 */
    private void act(Player player) {
    	boardPanel.setMessage(String.format("%s's turn.", player));
    	int action = -1;
    	
    	if (player instanceof DummyBot) {
        	player.performAction(boardCards, noOfBoardCards, minBet, bet);
        	boardPanel.setMessage(String.format("%s %s.", player, player.getAction().getVerb()));
        	playerPanels[actor].update();
            boardPanel.update(hand, bet, pot);
        	getInput(ControlPanel.CONTINUE);
    	} else {
	    	// Determine action.
	    	if (bet == 0) {
	            // Check, Bet or Fold.
	        	action = getInput(ControlPanel.CHECK_BET_FOLD);
	        } else if (player.getBet() < bet) {
	        	if (player.getRaises() < MAX_RAISES) {
		            // Call, Raise or Fold.
		        	action = getInput(ControlPanel.CALL_RAISE_FOLD);
	        	} else {
		            // Call or Fold.
		        	action = getInput(ControlPanel.CALL_FOLD);
	        	}
	        } else {
	            // Check, Raise or Fold.
	        	//FIXME: Is this even possible?
	        	action = getInput(ControlPanel.CHECK_RAISE_FOLD);
	        }
	
	    	// Perform action.
	    	if (action == ControlPanel.CHECK) {
	        	player.check();
	        } else if (action == ControlPanel.CALL) {
	        	player.call(bet);
	        } else if (action == ControlPanel.BET) {
	        	player.bet(minBet);
	        } else if (action == ControlPanel.RAISE) {
	        	player.raise(bet, minBet);
	        } else {
	        	player.fold();
	        }
	    	
	    	// Update screen.
        	playerPanels[actor].update();
            boardPanel.update(hand, bet, pot);
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
    private int getInput(final int actions) {
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
    
    /**
     * Notification that a human player has selected an action.
     */
    public void playerActed() {
        waitingForPlayer = false;
    }
    
    /**
     * Prepares game for a new hand.
     */
    private void resetHand() {
    	hand++;
    	noOfBoardCards = 0;
    	pot = 0;
    	bet = 0;
        boardPanel.update(hand, bet, pot);
        for (Player player : players) {
        	player.reset();
        }
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
        	playerPanels[i].update();
        }
    	deck.shuffle();
    }
    
    /**
     * Rotates the dealer button one position clockwise.
     */
    private void rotateDealer() {
        if (dealer != -1) {
            playerPanels[dealer].setDealer(false);
        }
        dealer = (dealer + 1) % NO_OF_PLAYERS;
        playerPanels[dealer].setDealer(true);
    }
    
    /**
     * Rotates the acting player by one position clockwise.
     */
    private void rotateActor() {
        if (actor != -1) {
            playerPanels[actor].setInTurn(false);
        }
        actor = (actor + 1) % NO_OF_PLAYERS;
        playerPanels[actor].setInTurn(true);
    }
    
}
