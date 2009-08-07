package org.ozsoft.texasholdem.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import org.ozsoft.texasholdem.Card;
import org.ozsoft.texasholdem.ComputerPlayer;
import org.ozsoft.texasholdem.Deck;
import org.ozsoft.texasholdem.Player;
import org.ozsoft.texasholdem.actions.Action;
import org.ozsoft.texasholdem.actions.BetAction;
import org.ozsoft.texasholdem.actions.CallAction;
import org.ozsoft.texasholdem.actions.FoldAction;
import org.ozsoft.texasholdem.actions.RaiseAction;

/**
 * The game's main frame.
 * 
 * @author Oscar Stigter
 */
public class MainFrame extends JFrame {
    
	private static final long serialVersionUID = 1L;
	
	private static final int NO_OF_PLAYERS = 4;
	
    private static final int INITIAL_CASH = 100;
    
    private static final int MIN_BET = 2;
    
    public  static final Color TABLE_COLOR = new Color(0, 128, 0);
    
    private static final GridBagConstraints gc = new GridBagConstraints();

    private final Deck deck = new Deck();
    
    private final Card[] board = new Card[5];
    
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
        super("Texas Hold'em Limit Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(TABLE_COLOR);
        setLayout(new GridBagLayout());
        players = new Player[] {
            new ComputerPlayer("Buffy",    INITIAL_CASH),
            new ComputerPlayer("Willow",   INITIAL_CASH),
            new ComputerPlayer("Xander",   INITIAL_CASH),
            new ComputerPlayer("Anya",     INITIAL_CASH),
//            new ComputerPlayer("Giles",    INITIAL_CASH),
//            new ComputerPlayer("Wesley",   INITIAL_CASH),
//            new ComputerPlayer("Cordelia", INITIAL_CASH),
//            new ComputerPlayer("Angel",    INITIAL_CASH),
//            new ComputerPlayer("Spike",    INITIAL_CASH),
//            new ComputerPlayer("Drusilla", INITIAL_CASH),
//            new ComputerPlayer("Darla",    INITIAL_CASH),
        };
        playerPanels = new PlayerPanel[NO_OF_PLAYERS];
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
            playerPanels[i] = new PlayerPanel(players[i]);
        }
        
        // 4 player table.
        addComponent(boardPanel, 1, 1, 1, 1);
        addComponent(playerPanels[0], 1, 0, 1, 1);
        addComponent(playerPanels[1], 2, 1, 1, 1);
        addComponent(playerPanels[2], 1, 2, 1, 1);
        addComponent(playerPanels[3], 0, 1, 1, 1);
        
//        // 10 player table.
//        addComponent(boardPanel, 1, 1, 3, 3);
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

        showFrame();
        
        start();
    }
    
    public static void main(String[] args) {
        new MainFrame();
    }
    
    private void addComponent(Component component, int x, int y, int width, int height) {
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        getContentPane().add(component, gc);
    }
    
    private void showFrame() {
//        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//        setSize(dimension.width, dimension.height - 30);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void start() {
    	handNumber = 1;
    	dealer = -1;
    	actor = -1;
    	
        // Main game loop.
    	while (!gameOver) {
        	
            resetHand();
            
            rotateDealer();
            rotateActor();
            boardPanel.setMessage(String.format("New hand. %s is the dealer.", players[dealer].getName()));
            waitForHumanInput(ControlPanel.CHOICE_CONTINUE);
            
            // Post the small blind.
            rotateActor();
            players[actor].postSmallBlind(MIN_BET / 2);
            pot += MIN_BET / 2;
            boardPanel.setPot(pot);
            boardPanel.setMessage(players[actor].getName() + " posts the small blind.");
            playerPanels[actor].update();
            waitForHumanInput(ControlPanel.CHOICE_CONTINUE);
            
            // Post the big blind.
            rotateActor();
            players[actor].postBigBlind(MIN_BET);
            pot += MIN_BET;
            boardPanel.setPot(pot);
            boardPanel.setMessage(players[actor].getName() + " posts the big blind.");
            playerPanels[actor].update();
            waitForHumanInput(ControlPanel.CHOICE_CONTINUE);
            
            // Deal the hole cards.
            rotateActor();
            for (Player player : players) {
                player.setCards(deck.deal(2));
            }
            for (int i = 0; i < NO_OF_PLAYERS; i++) {
            	playerPanels[i].update();
            }
            boardPanel.setMessage(players[dealer].getName() + " deals the hole cards.");
            waitForHumanInput(ControlPanel.CHOICE_CONTINUE);
            
            // Pre-flop betting round.
            doBettingRound(1);

            gameOver = true;
        }
        boardPanel.setMessage("Game over.");
    }
    
    private void doBettingRound(int round) {
        bet = 0;
    	// Determine starting actor. Normally position 2 (1 left of dealer),
    	// but two positons further at Pre-Flop (because of blinds). 
        int offset = (round == 1) ? 2 : 0;
        int actor = (dealer + offset) % NO_OF_PLAYERS;
        // Cound the number of active players left in this hand.
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
        	// Rotate actor.
            actor = (actor + 1) % NO_OF_PLAYERS;
            Player player = players[actor];
            // Only allow active players.
            if (!player.hasFolded() && !player.isBroke()) {
            	// Ask player to act. 
                player.performAction(board, noOfBoardCards, MIN_BET, bet);
                playersToAct--;
                Action action = player.getAction();
                System.out.format("%s %s.\n", player, action.getVerb());
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
                    playersToAct = activePlayers - 1;
                } else if (action instanceof RaiseAction) {
                	int amount = ((RaiseAction) action).getAmount();
                    bet += amount;
                    pot += amount;
                    System.out.format("The bet is now $ %d.\n", bet);
                    // Make sure other players must react.
                    playersToAct = activePlayers - 1;
                } else {
                	// Player checked.
                }
            }
        }
        for (Player player : players) {
        	player.reset();
        }
    }
    
    private void resetHand() {
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
    
    private void play() {
        if (actor == 0) {
            playHumanPlayer();
        } else {
            playComputerPlayer();
        }
    }
    
    private void playHumanPlayer() {
    	waitForHumanInput(ControlPanel.CHOICE_CONTINUE);
    }
    
    private void waitForHumanInput(int choices) {
        boardPanel.setChoices(choices);
        waitingForPlayer = true;
        try {
            while (waitingForPlayer) {
                Thread.sleep(10);
            }
        } catch (Exception ex) {
            // Ignore.
        }
    }
    
    private void playComputerPlayer() {
    	//TODO
    }
    
    public void playerActed() {
        waitingForPlayer = false;
    }
    
}
