package org.ozsoft.fondsbeheer.test;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.AfterClass;
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
	
	private static final String DATA_DIR = "target/test-data";
	
	/**
	 * Prepares the test environment before a test is run.
	 */
	@Before
	public void before() {
		Util.deleteFile(DATA_DIR);
	}
	
	/**
	 * Cleans up the test environment after all tests have run.
	 */
	@AfterClass
	public static void afterClass() {
//		Util.deleteFile(DATA_DIR);
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
		fund.addValue(new FundValue(new SmallDate(1, 1, 2009), 10.00f));
		fund.addValue(new FundValue(new SmallDate(2, 1, 2009), 10.25f));
		fund.addValue(new FundValue(new SmallDate(3, 1, 2009), 10.50f));
		category.addFund(fund);
		fundService.addCategory(category);
		fundService.storeFund(fund);
		
		fundService.stop();
		fundService.start();

		Assert.assertEquals(1, fundService.getNoOfCategories());
		Assert.assertEquals(1, fundService.getNoOfFunds());
		category = fundService.getCategory("c1");
		Assert.assertNotNull(category);
		Assert.assertEquals("Category 1", category.getName());
		Assert.assertEquals(1, category.getNoOfFunds());
		fund = category.getFund("f1");
		Assert.assertNotNull(fund);
		Assert.assertEquals("Fund 1", fund.getName());
		fundService.retrieveFund(fund);
		Assert.assertEquals(3, fund.getNoOfValues());
		FundValue[] values = fund.getValues().toArray(new FundValue[0]);
		Assert.assertEquals("01-Jan-2009", values[0].getDate().toString());
		Assert.assertEquals(10.00f, values[0].getPrice());
		Assert.assertEquals("02-Jan-2009", values[1].getDate().toString());
		Assert.assertEquals(10.25f, values[1].getPrice());
		Assert.assertEquals("03-Jan-2009", values[2].getDate().toString());
		Assert.assertEquals(10.50f, values[2].getPrice());

		fundService.stop();
	}

}
