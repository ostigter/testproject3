package th;

import java.util.List;
import java.util.Set;

public interface Client {
	
	void messageReceived(String message);

	void joinedTable(int bigBlind, List<Player> players);
	
	void dealerRotated(Player dealer);
	
	void actorRotated(Player actor);
	
	void holeCardsUpdated(Card[] cards);
	
	void boardUpdated(List<Card> cards, int bet, int pot);
	
	void playerActed(Player player);

    Action act(Set<Action> allowedActions);

}
