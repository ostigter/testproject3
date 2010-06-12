package org.ozsoft.genie.example;

/**
 * Calculator service implementation. <br />
 * <br />
 * 
 * Note that this is just a POJO, without any Genie-specific code.
 *  
 * @author Oscar
 */
public class CalculatorImpl implements Calculator {
	
	@Override
    public int add(int i, int j) {
		return i + j;
	}

    @Override
	public int subtract(int i, int j) {
		return i - j;
	}

    @Override
	public int multiply(int i, int j) {
		return i * j;
	}

    @Override
	public int divide(int i, int j) {
		if (j == 0) {
			throw new IllegalArgumentException("Division by zero");
		}
		return i / j;
	}

}
