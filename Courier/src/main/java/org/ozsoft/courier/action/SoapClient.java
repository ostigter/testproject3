package org.ozsoft.courier.action;

import org.apache.log4j.Logger;
import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;

/**
 * Action that sends  the current message to a file.
 * 
 * @author Oscar Stigter
 */
public class SoapClient implements Action {
    
	/** The log. */
	private static final Logger LOG = Logger.getLogger(SoapClient.class);
    
    /** The endpoint URL. */
	private final String url;
	
    /**
	 * Constructor.
	 * 
	 * @param url
	 *            The endpoint URL.
	 */
    public SoapClient(String url) throws CourierException {
        this.url = url;
        LOG.debug(String.format("Configuring soap-out action with url '%s'", url));
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.courier.Action#execute(org.ozsoft.courier.Context)
     */
    public void execute(Context context) {
    	//TODO
    }
    
}
