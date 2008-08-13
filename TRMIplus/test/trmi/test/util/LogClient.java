package trmi.test.util;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketAppender;


/**
 * Remote log4j client.
 * 
 * @author Chris Offerman
 */
public class LogClient {

	
	private final static String SERVER_HOST = "127.0.0.1";
	
    private final static int SERVER_PORT = 4560;
    
    private static SocketAppender appender;
        

    /**
     * Returns the logger for the specified class.
     * 
     * @param   c  the class
     * 
     * @return  the logger
     */
    @SuppressWarnings("unchecked")
	public static Logger getLogger(Class c) {
        String className = c.getName();
        
        if (appender == null) {
            appender = new SocketAppender(SERVER_HOST, SERVER_PORT);
            appender.setLocationInfo(true);
            BasicConfigurator.configure(appender);
        }
        
        return Logger.getLogger(className);
    }

    
    /**
	 * Frees all resources.
	 * 
	 * Typically redundant because when main exits they are freed anyway.
	 * 
	 * Multiple calls will not do any harm.
	 */    
    public static void close() {
        if (appender != null) {
            appender.close();
            appender = null;
            BasicConfigurator.resetConfiguration();
        }
    }
    

}
