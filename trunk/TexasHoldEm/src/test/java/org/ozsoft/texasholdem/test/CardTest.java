package org.ozsoft.texasholdem.test;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.texasholdem.Card;

public class CardTest {
	
	@Test
	public void sortOrder() {
		// Diamond is lower, Clubs is higher
		Card _2d = Card.parseCard("2d");
		Card _3d = Card.parseCard("3d");
		Card _2c = Card.parseCard("2c");
		Card _3c = Card.parseCard("3c");
		Assert.assertEquals(_2d, _2d);
		Assert.assertFalse(_2d.equals(_3d));
		Assert.assertFalse(_2d.equals(_2c));
		Assert.assertEquals(0, _2d.hashCode());
		Assert.assertEquals(1, _2c.hashCode());
		Assert.assertEquals(4, _3d.hashCode());
		Assert.assertEquals(5, _3c.hashCode());
		Assert.assertTrue(_2d.compareTo(_2d) == 0);
		Assert.assertTrue(_2d.compareTo(_3d) < 0);
		Assert.assertTrue(_3d.compareTo(_2d) > 0);
		Assert.assertTrue(_2d.compareTo(_2c) < 0);
		Assert.assertTrue(_2c.compareTo(_2d) > 0);
	}

}
