package xquery;


/**
 * Exception thrown by the XQuery processor.
 *  
 * @author Oscar Stigter
 */
class XQueryException extends Exception {

    
    private static final long serialVersionUID = 1L;
    

    public XQueryException(String message) {
        super(message);
    }


}
