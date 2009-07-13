package org.ozsoft.xpathevaluator;

/**
 * Test driver.
 * 
 * @author Oscar Stigter
 */
public class Main {

	/**
	 * The application's entry point.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		XPathEvaluator xpe = new XPathEvaluator();
		xpe.setDocument("document.xml");
		xpe.evaluate("/ft1:Bericht/Id/text()");
	}

}
