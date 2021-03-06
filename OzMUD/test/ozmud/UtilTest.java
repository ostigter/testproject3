package ozmud;


import junit.framework.Assert;

import org.junit.Test;

import ozmud.Util;


public class UtilTest {
	

	@Test
	public void replace() {
		Assert.assertEquals("aXc", Util.replace("abc", "b", "X"));
		Assert.assertEquals("XbXXXcX", Util.replace("abaaaca", "a", "X"));
		Assert.assertEquals("abcXYZdef", Util.replace("abc${xyz}def", "${xyz}", "XYZ"));
		Assert.assertEquals("abcXYZdefXYZ", Util.replace("abc${xyz}def${xyz}", "${xyz}", "XYZ"));
		Assert.assertEquals("", Util.replace("", "", ""));
		Assert.assertEquals("", Util.replace("", "", "X"));
		Assert.assertEquals("", Util.replace("a", "a", ""));
		Assert.assertEquals("", Util.replace("a", "a", null));
		Assert.assertEquals("ab", Util.replace("XaXbX", "X", null));
		Assert.assertEquals(null, Util.replace(null, "", ""));
		Assert.assertEquals(null, Util.replace(null, "a", "X"));
	}
	

	@Test
	public void capitalize() {
		Assert.assertEquals(null, Util.capitalize(null));
		Assert.assertEquals("", Util.capitalize(""));
		Assert.assertEquals("A", Util.capitalize("a"));
		Assert.assertEquals("A", Util.capitalize("A"));
		Assert.assertEquals("Abc", Util.capitalize("abc"));
		Assert.assertEquals("ABc", Util.capitalize("ABc"));
		Assert.assertEquals("123 abc", Util.capitalize("123 abc"));
		Assert.assertEquals("${}", Util.capitalize("${}"));
		Assert.assertEquals("${}${}", Util.capitalize("${}${}"));
		Assert.assertEquals("Abc${xyz}", Util.capitalize("abc${xyz}"));
		Assert.assertEquals("${xyz}", Util.capitalize("${xyz}"));
		Assert.assertEquals("${xyz}Abc", Util.capitalize("${xyz}abc"));
		Assert.assertEquals("${xyz}Abc${def}", Util.capitalize("${xyz}abc${def}"));
	}


}
