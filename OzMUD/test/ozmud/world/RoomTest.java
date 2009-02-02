package ozmud.world;


import junit.framework.Assert;

import org.junit.Test;


public class RoomTest {
	
	
	@Test
	public void broadcast() {
		World world = new World();
		
		Room room = new Room();
		room.setId(0);
		room.setShortName("Test Room");
		room.setDescription("A room for testing.");
		room.setWorld(world);

		Creature sender = new Player();
		sender.setShortName("Frodo");
		sender.setFullName("Frodo the Lord of the Rings");
		sender.setGender(Gender.MALE);
		sender.setWorld(world);
		
		Creature target = new NPC();
		target.setShortName("Goblin");
		target.setFullName("A nasty goblin skirmisher");
		target.setGender(Gender.MALE);
		target.setWorld(world);

		String message = "${sender} hit${s} ${target} very hard.";
		Assert.assertEquals("You hit Goblin very hard.",
				room.format(message, sender, target, Perspective.SELF));
		Assert.assertEquals("Frodo hits you very hard.",
				room.format(message, sender, target, Perspective.TARGET));
		Assert.assertEquals("Frodo hits Goblin very hard.",
				room.format(message, sender, target, Perspective.OTHERS));
	}
	

}
