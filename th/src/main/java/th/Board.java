package th;

public class Board {
	
	private Card[] cards;
	
	private int noOfCards;
	
	private int hand;
	
	private int bet;
	
	private int pot;
	
	public void update(Card[] cards, int noOfCards, int hand, int bet, int pot) {
		this.cards = cards;
		this.noOfCards = noOfCards;
		this.hand = hand;
		this.bet = bet;
		this.pot = pot;
	}
	
}
