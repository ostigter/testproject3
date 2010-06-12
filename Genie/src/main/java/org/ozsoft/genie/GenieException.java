package org.ozsoft.genie;

/**
 * A Genie exception.
 * 
 * @author Oscar Stigter
 */
public class GenieException extends Exception {
	
    private static final long serialVersionUID = -6482967147190253428L;

    public GenieException(String msg) {
		super(msg);
	}
	
	public GenieException(String msg, Throwable t) {
		super(msg, t);
	}
	
}
