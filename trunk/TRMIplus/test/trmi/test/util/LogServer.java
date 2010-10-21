package trmi.test.util;

import org.apache.log4j.net.SimpleSocketServer;

/**
 * Remote Log4j server. <br />
 * <br />
 * 
 * Implemented as a wrapper around Log4j's SimpleSocketServer.
 * 
 * @author Chris Offerman
 */
public class LogServer {

    private static final String PORT = "4560"; // log4j default

    private static final String CONFIG_FILE = "log4j.xml";

    public static void main(String[] args) {
        SimpleSocketServer.main(new String[] { PORT, CONFIG_FILE });
    };

}
