// This file is part of the exist-connector project.
//
// Copyright 2010 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
    private static final String DEFAULT_PASSWORD = "guest";
    
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
    
    /**
     * Constructor.
     * 
     * @param args
     *            The command line arguments.
     */
    public Main(String[] args) {
        parseArguments(args);
    }
    
    /**
     * Prints the command line usage.
     */
    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -jar ExistConnector.jar [<options>] <command>");
        System.out.println("where");
        System.out.println("  <options> :");
        System.out.println("    -h <host>                  : eXist hostname (default: 'localhost')");
        System.out.println("    -p <port>                  : eXist port number (default: 8080)");
        System.out.println("    -u <username>:[<password>] : eXist user account to use (default: 'guest' account");
        System.out.println("  <command> :");
        System.out.println("    list <uri>                 : retrieves a (textual) listing of a collection");
        System.out.println("    get <uri>                  : retrieves the content of a resource");
        System.out.println("    import <uri> <path>        : stores a resource from a file or directory");
        System.out.println("    export <uri> <path>        : exports a resource to a file or directory");
        System.out.println("    delete <uri>               : deletes a resource (recursively)");
        System.out.println("    query <text>               : executes an ad-hoc query");
    }
    
    /**
     * Parses the command line arguments.
     * 
     * @param args
     *            The command line arguments.
     */
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
            } else if (arg.equals("-p")) {
                i++;
                if (i < args.length) {
                    try {
                        port = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        System.err.println("ERROR: Invalid port number: " + args[i]);
                        System.exit(1);
                    }
                } else {
                    System.err.println("ERROR: Missing port number with '-p' option");
                    System.exit(1);
                }
            } else if (arg.equals("-u")) {
                i++;
                if (i < args.length) {
                    String[] parts = args[i].split(":");
                    username = parts[0];
                    if (parts.length > 1) {
                        password = parts[1];
                    } else {
                        password = "";
                    }
                } else {
                    System.err.println("ERROR: Missing user account with '-u' option");
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
            } else if (command.equalsIgnoreCase("import")) {
                doImport(params);
            } else if (command.equalsIgnoreCase("export")) {
                doExport(params);
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
    
    /**
     * Retrieves a textual listing of a collection.
     * 
     * @param params
     *            The command parameters with the collection URI.
     * 
     * @throws XmldbException
     *             If the collection could not be retrieved.
     */
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
    
    /**
     * Retrieves a resource. <br />
     * <br />
     * 
     * The resource content is retrieved as an UTF-8 encoded String and written
     * to stdout.
     * 
     * @param params
     *            The command parameters with the resource URI.
     * 
     * @throws XmldbException
     *             If the resource could not be retrieved.
     */
    private void doGet(List<String> params) throws XmldbException {
        if (params.size() == 0) {
            System.err.println("ERROR: Missing resource URI");
            System.exit(2);
        }
        String uri = params.get(0);
        byte[] data = connector.retrieveResource(uri);
        try {
            String content = new String(data, "UTF-8");
            System.out.println(content);
        } catch (UnsupportedEncodingException e) {
            // This should never happen since UTF-8 is a standard charset.
            System.err.println("ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Imports a resource from a file or directory.
     * 
     * @param params
     *            The command parameters with the resource URI and the file or
     *            directory path.
     * 
     * @throws XmldbException
     *             If a resource could not be imported.
     */
    private void doImport(List<String> params) throws XmldbException {
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
        connector.importResource(uri, new File(path));
    }
    
    /**
     * Exports a resource to a file or directory.
     * 
     * @param params
     *            The command parameters with the resource URI and the file or
     *            directory path.
     * 
     * @throws XmldbException
     *             If a resource could not be exported.
     */
    private void doExport(List<String> params) throws XmldbException {
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
    
    /**
     * Deletes a resource.
     * 
     * @param params
     *            The command parameters with the resource URI.
     * 
     * @throws XmldbException
     *             If the resource could not be deleted.
     */
    private void doDelete(List<String> params) throws XmldbException {
        if (params.size() == 0) {
            System.err.println("ERROR: Missing resource URI");
            System.exit(1);
        }
        String uri = params.get(0);
        connector.deleteResource(uri);
    }
    
    /**
     * Executes an ad-hoc query.
     * 
     * @param params
     *            The command parameters with the query text.
     * 
     * @throws XmldbException
     *             If the query could not be executed successfully.
     */
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
