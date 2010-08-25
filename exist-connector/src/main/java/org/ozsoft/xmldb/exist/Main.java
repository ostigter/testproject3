package org.ozsoft.xmldb.exist;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.ozsoft.xmldb.Collection;
import org.ozsoft.xmldb.Resource;
import org.ozsoft.xmldb.XmldbConnector;
import org.ozsoft.xmldb.XmldbException;

/**
 * Command line interface for the <code>ExistConnector</code>.
 * 
 * @author Oscar Stigter
 */
public class Main {
    
    /** Default hostname of eXist instance. */
    private static final String DEFAULT_HOSTNAME = "localhost";

    /** Default port number of eXist instance. */
    private static final int DEFAULT_PORT = 8080;

    /** Default username of eXist user account. */
    private static final String DEFAULT_USERNAME = "guest";

    /** Default password of eXist user account. */
    private static final String DEFAULT_PASSWORD = "";
    
    /** Database connector. */
    private XmldbConnector connector;
    
    /**
     * Application entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        new Main(args);
    }
    
    public Main(String[] args) {
        parseArguments(args);
    }
    
    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -jar ExistConnector.jar [<options>] <command>");
        System.out.println("where");
        System.out.println("  <options> :");
        System.out.println("    -h <host>          : eXist hostname (default: 'localhost')");
        System.out.println("    -port <port>       : port number the eXist instance (default: 8080)");
        System.out.println("    -u <username>      : username of the eXist user account to use (default: 'guest')");
        System.out.println("    -pwd <password>    : password of the eXist user account to use (default: empty)");
        System.out.println("  <command> :");
        System.out.println("    list <uri>         : retrieves the listing of a collection");
        System.out.println("    get <uri> [<path>] : retrieves the content of a resource, optionally exporting it to a file or directory");
        System.out.println("    put <uri> <path>   : stores a resource from a file or directory");
        System.out.println("    delete <uri>       : deletes a resource (recursively)");
        System.out.println("    query <text>       : executes an ad-hoc query");
    }
    
    private void parseArguments(String[] args) {
        String hostname = DEFAULT_HOSTNAME;
        int port = DEFAULT_PORT;
        String username = DEFAULT_USERNAME;
        String password = DEFAULT_PASSWORD;
        String command = null;
        List<String> params = new ArrayList<String>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-h")) {
                i++;
                if (i < args.length) {
                    hostname = args[i];
                } else {
                    System.err.println("ERROR: Missing hostname with '-h' option");
                    System.exit(1);
                }
            } else if (arg.equals("-port")) {
                i++;
                if (i < args.length) {
                    try {
                        port = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        System.err.println("ERROR: Invalid port number: " + args[i]);
                        System.exit(1);
                    }
                } else {
                    System.err.println("ERROR: Missing port number with '-port' option");
                    System.exit(1);
                }
            } else if (arg.equals("-u")) {
                i++;
                if (i < args.length) {
                    username = args[i];
                } else {
                    System.err.println("ERROR: Missing username with '-u' option");
                    System.exit(1);
                }
            } else if (arg.equals("-pwd")) {
                i++;
                if (i < args.length) {
                    password = args[i];
                } else {
                    System.err.println("ERROR: Missing password with '-pwd' option");
                    System.exit(1);
                }
            } else {
                if (command == null) {
                    command = args[i];
                } else {
                    params.add(args[i]);
                }
            }
        }
        
        if (command == null) {
            printUsage();
            System.exit(0);
        }
        
        connector = new ExistConnector(hostname, port, username, password);
        
        try {
            if (command.equalsIgnoreCase("list")) {
                doList(params);
            } else if (command.equalsIgnoreCase("get")) {
                doGet(params);
            } else if (command.equalsIgnoreCase("put")) {
                doPut(params);
            } else if (command.equalsIgnoreCase("delete")) {
                doDelete(params);
            } else if (command.equalsIgnoreCase("query")) {
                doQuery(params);
            } else {
                System.err.println("ERROR: Unknown command: " + command);
                System.exit(1);
            }
        } catch (XmldbException e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(99);
        }
    }
    
    private void doList(List<String> params) throws XmldbException {
        if (params.isEmpty()) {
            System.err.println("ERROR: Missing resource URI");
            System.exit(1);
        }
        String uri = params.get(0);
        Collection col = connector.retrieveCollection(uri);
        if (col != null) {
            for (Resource res : col.getResources()) {
                System.out.println(res.getName());
            }
        } else {
            System.err.println("ERROR: Collection not found: " + uri);
            System.exit(1);
        }
    }
    
    private void doGet(List<String> params) throws XmldbException {
        if (params.size() == 0) {
            System.err.println("ERROR: Missing resource URI");
            System.exit(2);
        }
        String uri = params.get(0);
        String path = null;
        if (params.size() > 1) {
            path = params.get(1);
        }
        
        if (path != null) {
            // Export resource to file.
            connector.exportResource(uri, new File(path));
        } else {
            // Write resource content to console.
            byte[] data = connector.retrieveResource(uri);
            try {
                String content = new String(data, "UTF-8");
                System.out.println(content);
            } catch (UnsupportedEncodingException e) {
                // This should never happen since UTF-8 is a standard charset.
                System.err.println("ERROR: " + e.getMessage());
            }
        }
    }
    
    private void doPut(List<String> params) throws XmldbException {
        if (params.size() < 1) {
            System.err.println("ERROR: Missing resource URI");
            System.exit(1);
        }
        if (params.size() < 2) {
            System.err.println("ERROR: Missing path");
            System.exit(1);
        }
        String uri = params.get(0);
        String path = params.get(1);
        connector.exportResource(uri, new File(path));
    }
    
    private void doDelete(List<String> params) throws XmldbException {
        if (params.size() == 0) {
            System.err.println("ERROR: Missing resource URI");
            System.exit(1);
        }
        String uri = params.get(0);
        connector.deleteResource(uri);
    }
    
    private void doQuery(List<String> params) throws XmldbException {
        if (params.size() == 0) {
            System.err.println("ERROR: Missing query text");
            System.exit(1);
        }
        String query = params.get(0);
        String result = connector.executeQuery(query);
        System.out.println(result);
    }
    
}
