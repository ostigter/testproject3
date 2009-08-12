package th;

import java.util.ArrayList;
import java.util.List;

public class Engine {
	
	private final int blind;
	
	private final Player[] players;
	
	private final List<Player> activePlayers;
	
	private final Deck deck;
	
	private final List<Card> board;
	
	private int minBet;
	
	private int bet;
	
	private int pot;
	
	private boolean gameOver;
	
	public Engine(int blind, Player[] players) {
		this.blind = blind;
		this.players = players;
		activePlayers = new ArrayList<Player>();
		deck = new Deck();
		board = new ArrayList<Card>();
	}
	
	public void run() {
		resetGame();
		while (!gameOver) {
			
			
			gameOver = true;
		}
		System.out.println("Game over.");
	}
	
	private void resetGame() {
		activePlayers.clear();
		for (Player player : players) {
			activePlayers.add(player);
		}
		gameOver = false;
	}

}
