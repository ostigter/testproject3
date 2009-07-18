package org.ozsoft.genie.example;

/**
 * A very simple calculator service.
 * 
 * Note that this is just a POJO, without any Genie specific code.
 *  
 * @author Oscar
 */
public class Calculator {
	
	public int add(int i, int j) {
		return i + j;
	}

	public int subtract(int i, int j) {
		return i - j;
	}

	public int multiply(int i, int j) {
		return i * j;
	}

	public int divide(int i, int j) {
		if (j == 0) {
			throw new IllegalArgumentException("Division by zero");
		}
		return i / j;
	}

}
