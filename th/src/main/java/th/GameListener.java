package th;

public interface GameListener {
	
	void messageReceived(String message);
	
	void boardUpdated(int hand, Card[] cards, int noOfCards, int bet, int pot);
	
	void playerActed(PlayerInfo playerInfo);

}
