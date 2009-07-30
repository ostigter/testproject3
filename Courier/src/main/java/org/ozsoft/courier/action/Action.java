package org.ozsoft.courier.action;

import org.ozsoft.courier.Context;
import org.ozsoft.courier.CourierException;

/**
 * Interface for an action.
 * 
 * @author Oscar Stigter
 */
public interface Action {
    
    /**
     * Executes the action.
     * 
     * @param context
     *            The message context.
     * 
     * @throws CourierException
     *             If an error occurs during execution of the action.
     */
	void execute(Context context) throws CourierException ;

}
