package ozmud.world;


import junit.framework.Assert;

import org.junit.Test;


public class RoomTest {
	
	
	@Test
	public void broadcast() {
		World world = new World();
		Room room = new Room(0, "Test room", "A room for testing.");
		Creature sender = new Player("frodo", Gender.MALE, "secret", world);
		Creature target = new NPC("Goblin", Gender.MALE, "A nasty goblin.", world);
		String message = "${sender} hit${s} ${target} very hard.";
		Assert.assertEquals("You hit Goblin very hard.",
				room.format(message, sender, target, Perspective.SELF));
		Assert.assertEquals("Frodo hits you very hard.",
				room.format(message, sender, target, Perspective.TARGET));
		Assert.assertEquals("Frodo hits Goblin very hard.",
				room.format(message, sender, target, Perspective.OTHERS));
	}
	

}
