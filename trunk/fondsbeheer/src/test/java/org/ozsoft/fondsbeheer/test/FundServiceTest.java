package org.ozsoft.fondsbeheer.test;

import org.junit.Test;
import org.ozsoft.fondsbeheer.FundService;
import org.ozsoft.fondsbeheer.FundServiceImpl;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.entities.FundValue;
import org.ozsoft.fondsbeheer.entities.SmallDate;

/**
 * Test suite for the Fund service.
 * 
 * @author Oscar Stigter
 */
public class FundServiceTest {
	
	private static final String DATA_DIR = "target/test-data"; 
	
	@Test
	public void test() {
		FundService fundService = new FundServiceImpl();
		fundService.setDataDirectory(DATA_DIR);
		fundService.start();
		
		Category cat = new Category("c1", "Category 1");
		Fund fund = new Fund("f1", "Fund 1");
		fund.addValue(new FundValue(new SmallDate(1, 1, 2009), 10.00f));
		fund.addValue(new FundValue(new SmallDate(2, 1, 2009), 10.25f));
		fund.addValue(new FundValue(new SmallDate(3, 1, 2009), 10.75f));
		fund.addValue(new FundValue(new SmallDate(4, 1, 2009), 11.50f));
		fund.addValue(new FundValue(new SmallDate(5, 1, 2009), 12.25f));
		cat.addFund(fund);
		fundService.storeFund(fund);
		
		fundService.stop();
	}

}
