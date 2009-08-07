package cards.poker.texasholdem.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import cards.poker.texasholdem.Card;
import cards.poker.texasholdem.ComputerPlayer;
import cards.poker.texasholdem.Deck;
import cards.poker.texasholdem.Player;


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
        super("Texas Hold'em Poker");
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
            playerPanels[actor].update();
            boardPanel.setPot(pot);
            boardPanel.setMessage(players[actor].getName() + " posts the small blind.");
            waitForHumanInput(ControlPanel.CHOICE_CONTINUE);
            
            // Post the big blind.
            rotateActor();
            players[actor].postSmallBlind(MIN_BET);
            pot += MIN_BET;
            playerPanels[actor].update();
            boardPanel.setPot(pot);
            boardPanel.setMessage(players[actor].getName() + " posts the big blind.");
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
            bet = MIN_BET;
            
            play();
            
            gameOver = true;
        }
        boardPanel.setMessage("Game over.");
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
