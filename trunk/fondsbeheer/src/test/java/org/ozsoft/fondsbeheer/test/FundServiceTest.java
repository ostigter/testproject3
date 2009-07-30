package org.ozsoft.fondsbeheer.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.entities.FundValue;
import org.ozsoft.fondsbeheer.entities.SmallDate;
import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.FundServiceImpl;

/**
 * Test suite for the Fund service.
 * 
 * @author Oscar Stigter
 */
public class FundServiceTest {
	
	/** The data directory. */
	private static final String DATA_DIR = "target/test-data";
	
	/**
	 * Prepares the test environment before a test is run.
	 */
	@Before
	public void before() {
		Util.deleteFile(DATA_DIR);
	}
	
	/**
	 * Tests the Fund service.
	 */
	@Test
	public void test() {
		FundService fundService = new FundServiceImpl();
		fundService.setDataDirectory(DATA_DIR);
		fundService.start();

		Category category = new Category("c1", "Category 1");

		Fund fund = new Fund("f1", "Fund 1");
		fund.addValue(new FundValue(new SmallDate(1, 6, 2009), 10.00f));
		fundService.storeFund(fund);
		category.addFund(fund);
		
		fund = new Fund("f2", "Fund 2");
		fund.addValue(new FundValue(new SmallDate(2, 6, 2009), 20.00f));
		fund.addValue(new FundValue(new SmallDate(3, 6, 2009), 20.25f));
		fund.addValue(new FundValue(new SmallDate(4, 6, 2009), 20.50f));
		fundService.storeFund(fund);
		category.addFund(fund);

		fundService.addCategory(category);
		
		category = new Category("c2", "Category 2");
		
		fund = new Fund("f3", "Fund 3");
		fund.addValue(new FundValue(new SmallDate(1, 6, 2009), 11.00f));
		fundService.storeFund(fund);
		category.addFund(fund);
		
		fund = new Fund("f4", "Fund 4");
		fund.addValue(new FundValue(new SmallDate(2, 6, 2009), 21.00f));
		fund.addValue(new FundValue(new SmallDate(3, 6, 2009), 21.25f));
		fund.addValue(new FundValue(new SmallDate(4, 6, 2009), 21.50f));
		fundService.storeFund(fund);
		category.addFund(fund);

		fundService.addCategory(category);
		
		fundService.stop();
		fundService.start();
		
		category = fundService.getCategory("c1");
		Assert.assertNotNull(category);
		fund = category.getFund("f1");
		Assert.assertNotNull(fund);
		fundService.retrieveFund(fund);
		Assert.assertEquals(1, fund.getNoOfValues());
		fund.addValue(new FundValue(new SmallDate(1, 5, 2009), 31.00f));
		fund.addValue(new FundValue(new SmallDate(2, 5, 2009), 32.00f));
		fund.addValue(new FundValue(new SmallDate(1, 7, 2009), 41.00f));
		fund.addValue(new FundValue(new SmallDate(2, 7, 2009), 42.00f));
		fundService.storeFund(fund);

		fundService.stop();
		fundService.start();
		
		Assert.assertEquals(2, fundService.getNoOfCategories());
		Assert.assertEquals(4, fundService.getNoOfFunds());
		
		category = fundService.getCategory("c1");
		Assert.assertNotNull(category);
		Assert.assertEquals("Category 1", category.getName());
		Assert.assertEquals(2, category.getNoOfFunds());
		
		fund = category.getFund("f1");
		Assert.assertNotNull(fund);
		Assert.assertEquals("Fund 1", fund.getName());
		fundService.retrieveFund(fund);
		Assert.assertEquals(5, fund.getNoOfValues());
		FundValue[] values = fund.getValues().toArray(new FundValue[0]);
		Assert.assertEquals("01-May-2009", values[0].getDate().toString());
		Assert.assertEquals(31.00f, values[0].getValue());
		Assert.assertEquals("02-May-2009", values[1].getDate().toString());
		Assert.assertEquals(32.00f, values[1].getValue());
		Assert.assertEquals("01-Jun-2009", values[2].getDate().toString());
		Assert.assertEquals(10.00f, values[2].getValue());
		Assert.assertEquals("01-Jul-2009", values[3].getDate().toString());
		Assert.assertEquals(41.00f, values[3].getValue());
		Assert.assertEquals("02-Jul-2009", values[4].getDate().toString());
		Assert.assertEquals(42.00f, values[4].getValue());

		fund = category.getFund("f2");
		Assert.assertNotNull(fund);
		Assert.assertEquals("Fund 2", fund.getName());
		fundService.retrieveFund(fund);
		Assert.assertEquals(3, fund.getNoOfValues());
		values = fund.getValues().toArray(new FundValue[0]);
		Assert.assertEquals("02-Jun-2009", values[0].getDate().toString());
		Assert.assertEquals(20.00f, values[0].getValue());
		Assert.assertEquals("03-Jun-2009", values[1].getDate().toString());
		Assert.assertEquals(20.25f, values[1].getValue());
		Assert.assertEquals("04-Jun-2009", values[2].getDate().toString());
		Assert.assertEquals(20.50f, values[2].getValue());
		
		category = fundService.getCategory("c2");
		Assert.assertNotNull(category);
		Assert.assertEquals("Category 2", category.getName());
		Assert.assertEquals(2, category.getNoOfFunds());
		
		fund = category.getFund("f4");
		Assert.assertNotNull(fund);
		Assert.assertEquals("Fund 4", fund.getName());
		fundService.retrieveFund(fund);
		Assert.assertEquals(3, fund.getNoOfValues());

		fundService.stop();
	}

}
