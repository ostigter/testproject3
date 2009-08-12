package th;

public class Main {
	
	public static void main(String[] args) {
		
		int blind = 2;
		int playerCash = 100;
		Player[] players = {
			new ConsolePlayer("Player", playerCash),
			new Bot("Joe", playerCash),
			new Bot("Mike", playerCash),
			new Bot("Wendy", playerCash),
		};
		Engine engine = new Engine(2, players);
		engine.run();
	}

}
