package ozmud.world;


import junit.framework.Assert;

import org.junit.Test;


public class RoomTest {
	
	
	@Test
	public void broadcast() {
		World.getInstance().init();
		
		Room room = new Room();
		room.setId(0);
		room.setShortName("Test Room");
		room.setDescription("A room for testing.");

		Creature sender = new Player();
		sender.setShortName("Frodo");
		sender.setFullName("Frodo the Lord of the Rings");
		sender.setGender(Gender.MALE);
		
		Creature target = new NPC();
		target.setShortName("Guard");
		target.setFullName("A strong female guard");
		target.setGender(Gender.FEMALE);

		String message = "${CYAN}${sender} look${s} at ${target}.";
		Assert.assertEquals("${CYAN}You look at yourself.",
				room.format(message, sender, sender, Perspective.SELF));
		Assert.assertEquals("${CYAN}You look at Guard.",
				room.format(message, sender, target, Perspective.SELF));
		Assert.assertEquals("${CYAN}Frodo looks at you.",
				room.format(message, sender, target, Perspective.TARGET));
		Assert.assertEquals("${CYAN}Frodo looks at himself.",
				room.format(message, sender, sender, Perspective.OTHERS));
		Assert.assertEquals("${CYAN}Frodo looks at Guard.",
				room.format(message, sender, target, Perspective.OTHERS));
		Assert.assertEquals("${CYAN}Guard looks at herself.",
				room.format(message, target, target, Perspective.OTHERS));
	}
	

}
