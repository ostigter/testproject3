package org.ozsoft.jfind;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command line Find & Replace tool to process text files.
 * 
 * Combines the power of 'find', 'grep' and inline text replacement tools.
 *
 * TODO: File renaming (--rename)
 * TODO: File deletion (--delete)
 * TODO: Custom command execution (-exec)
 */
public class Main {
    
    /** The maximum numbers of bytes to check when determining the file type. */
    private static final int CHECK_LENGTH = 1024;

    /** The TAB character. */
    private static final int TAB = '\t';

    /** The Line Feed character. */
    private static final int LF = '\n';
    
    /** The Carriage Return character. */
    private static final int CR = '\r';
    
    /** Directories to ignore while scanning. */
    private final Set<String> ignoredDirs;
    
    /** ASCII control characters normal for text files. */
    private final Set<Integer> allowedControlChars;
    
    /** The console reader. */
    private final BufferedReader consoleReader;
    
    /** Starting directory. */
    private File dir = new File(".");
    
    /** The file name pattern. */
    private Pattern filePattern;
    
    /** Search text pattern. */
    private Pattern searchPattern;
    
    /** Replace text. */
    private String replaceText;
    
    /** Recurse into subdirectories. */
    private boolean recursion = true;
    
    /** Case sensitivity. */
    private boolean caseSensitive = true;
    
    /** Ask user confirmation before any replacement or file action. */
    private boolean askConfirmation = false;
    
    /** Process all directories, even ignored ones. */
    private boolean processAllDirs = false;
    
    /**
     * Constructor.
     * 
     * @param args
     *            the command line arguments
     */
    public Main(String[] args) {
        parseArguments(args);
        
        // Get console reader.
        consoleReader = new BufferedReader(new InputStreamReader(System.in)); 
        
        // Define ignored directories.
        ignoredDirs = new HashSet<String>();
        ignoredDirs.add(".svn");
        ignoredDirs.add(".cvs");
        ignoredDirs.add("SCCS");
        ignoredDirs.add(".parent");
        
        // Define allowed ASCII control characters for text files.
        allowedControlChars = new HashSet<Integer>();
        allowedControlChars.add(TAB);
        allowedControlChars.add(LF);
        allowedControlChars.add(CR);
        
        // Get starting directory.
        String path = dir.getPath();
        if (path.equals(".")) {
            // Current directory (special case).
            path = "";
        } else if (!path.endsWith(File.separator)) {
            // Make sure a set path ends with a separator.
            path += File.separatorChar;
        }
        
        processDirectory(dir, path);
    }
    
    /**
     * The application's entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        new Main(args);
    }
    
    /**
     * Prints the program's usage.
     */
    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  jfind [<options>]");
        System.out.println("where <options> are:");
        System.out.println("  -h, --help            = Shows this usage information");
        System.out.println("  -d                    = Start in directory <dir>");
        System.out.println("  -f <file_pattern>     = Process only files with a name matching <file_pattern>");
        System.out.println("  -s <search_pattern>   = Process only files containing <search_pattern>");
        System.out.println("  -r <replace_pattern>  = Replace <search_pattern> with <replace_text>");
        System.out.println("  -R                    = Do not recurse into subdirectories");
        System.out.println("  -D                    = Process all directories, even normally ignored ones");
        System.out.println("  -i                    = Search text case-insensitive");
        System.out.println("  -c                    = Ask user confirmation per replacement or file action");
//        System.out.println("  -rename <extention>   = Rename files found with <extention>");
//        System.out.println("  -delete               = Delete files found");
//        System.out.println("  -command <command>    = Execute <command> on files found");
    }
    
    /**
     * Parses the command line arguments.
     * 
     * @param args
     *            The command line arguments
     */
    private void parseArguments(String[] args) {
        String searchText = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help")) {
                printUsage();
                System.exit(0);
            } else if (args[i].equals("-d")) {
                i++;
                if (i < args.length) {
                    dir = new File(args[i]);
                    if (!dir.exists() || !dir.isDirectory()) {
                        System.err.format("ERROR: Directory not found: '%s'\n", dir);
                        System.exit(2);
                    }
                } else {
                    System.err.println("ERROR: No directory specified with '-d' option");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-f")) {
                i++;
                if (i < args.length) {
                    filePattern = Pattern.compile(wildcardToRegex(args[i], false));
                } else {
                    System.err.println("ERROR: No file pattern specified with '-f' option");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-s")) {
                i++;
                if (i < args.length) {
                    searchText = args[i];
                } else {
                    System.err.println("ERROR: No search pattern specified with '-s' option");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-r")) {
                i++;
                if (i < args.length) {
                    replaceText = args[i];
                } else {
                    System.err.println("ERROR: No replace pattern specified with '-r' option");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-R")) {
                recursion = false;
            } else if (args[i].equals("-D")) {
                processAllDirs = true;
            } else if (args[i].equals("-i")) {
                caseSensitive = false;
            } else if (args[i].equals("-c")) {
                askConfirmation = true;
            } else {
                System.err.format("ERROR: Unknown argument: '%s'\n",  args[i]);
                printUsage();
                System.exit(1);
            }
        }
        if (searchText != null) {
            String regex = wildcardToRegex(searchText, true);
            if (caseSensitive) {
                searchPattern = Pattern.compile(regex);
            } else {
                searchPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            }
        }
    }
    
    /**
     * Recursively processes a directory.
     * 
     * @param dir
     *            The directory.
     * @param path
     *            The path.
     */
    private void processDirectory(File dir, String path) {
        if (dir != null && dir.isDirectory() && dir.canRead()) {
            if (replaceText != null) {
                if (!dir.canWrite()) {
                    System.err.format("WARN: Directory not writable: '%s'\n", dir);
                    return;
                }
            }
            // First process files...
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String name = file.getName();
                        if (filePattern != null) {
                            if (filePattern.matcher(name).find()) {
                                processFile(file, path);
                            }
                        } else {
                            processFile(file, path);
                        }
                    }
                }
                // ...then recurse into subdirectories.
                if (recursion) {
                    for (File file : dir.listFiles()) {
                        if (file.isDirectory()) {
                            String name = file.getName();
                            if (processAllDirs || !ignoredDirs.contains(name)) {
                                StringBuilder sb = new StringBuilder(path);
                                if (path.length() > 0) {
                                    sb.append(File.separatorChar);
                                }
                                sb.append(name);
                                processDirectory(file, sb.toString());
                            }
                        }
                    }
                }
            } else {
                System.err.format("WARN: Could not read directory: '%s'\n", dir);
            }
        } else {
            System.err.format("WARN: Could not read directory: '%s'\n", dir);
        }
    }
    
    /**
     * Processes a file.
     * 
     * @param file
     *            The file.
     * @param path
     *            The path.
     */
    private void processFile(File file, String path) {
        if (path.length() != 0) {
            path += File.separator;
        }
        if (searchPattern != null) {
            if (file.isFile() && isTextFile(file)) {
                BufferedReader reader = null;
                BufferedWriter writer = null;
                File tempFile = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String line = null;
                    int lineNumber = 0;
                    boolean replaced = false;
                    if (replaceText != null) {
                        tempFile = new File(file.getPath() + ".tmp");
                        writer = new BufferedWriter(new FileWriter(tempFile));
                    }
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        if (replaceText != null) {
                            // Search and replace.
                            Matcher matcher = searchPattern.matcher(line); 
                            if (matcher.find()) {
                                boolean replace = true;
                                if (askConfirmation) {
                                    System.out.format("%s%s:%d\t%s\n", path, file.getName(), lineNumber, line);
                                    Response response = getUserResponse("Replace");
                                    replace = (response == Response.YES);
                                }
                                if (replace) {
                                    line = matcher.replaceAll(replaceText);
                                    replaced = true;
                                    // Do not write line if it became empty.
                                    if (line.trim().length() != 0) {
                                        if (!askConfirmation) {
                                            System.out.format("%s%s:%d\t%s\n", path, file.getName(), lineNumber, line);
                                        }
                                        writer.write(line + '\n');
                                    }
                                }
                            } else {
                                writer.write(line + '\n');
                            }
                        } else {
                            // Search only.
                            if (searchPattern.matcher(line).find()) {
                                System.out.format("%s%s:%d\t%s\n", path, file.getName(), lineNumber, line);
                            }
                        }
                    }
                    reader.close();
                    if (replaceText != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            // Best effort; ignore.
                        }
                        if (replaced) {
                            file.delete();
                            tempFile.renameTo(file);
                        } else {
                            tempFile.delete();
                        }
                    }
                } catch (IOException e) {
                    System.err.format("ERROR: Could not process file '%s': %s\n", file, e.getMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // Best effort; ignore.
                        }
                    }
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            // Best effort; ignore.
                        }
                    }
                }
            }
        } else {
            System.out.println(path + file.getName());
        }
    }
    
    private Response getUserResponse(String message) {
        List<Response> allowedResponses = new ArrayList<Response>();
        allowedResponses.add(Response.YES);
        allowedResponses.add(Response.NO);
        allowedResponses.add(Response.ALL);
        allowedResponses.add(Response.QUIT);
        Response response = null;
        try {
            while (response == null) {
                System.out.format("%s? (Yes/No/All/Quit) ", message);
                String input = consoleReader.readLine();
                if (input != null) {
                    input = input.trim().toLowerCase();
                    for (Response r : allowedResponses) {
                        if (r.getCommand().startsWith(input)) {
                            response = r;
                            break;
                        }
                    }
                }
                if (response == null) {
                    System.out.println("Invalid response -- please try again.");
                }
            }
            if (response == Response.ALL) {
                response = Response.YES;
                askConfirmation = false;
            } else if (response == Response.QUIT) {
                response = Response.NO;
                replaceText = null;
            }
        } catch (IOException e) {
            // Ignore.
        }
        return response;
    }
    
    /**
     * Returns true if a file is a text file (instead of a binary file).
     * 
     * @param file
     *            The file.
     * 
     * @return True if a text file, otherwise false.
     */
    private boolean isTextFile(File file) {
        boolean isTextFile = true;
        int count = 0;
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            while (is.available() > 0 && count < CHECK_LENGTH) {
                int b = is.read();
                if (b < 32 && !allowedControlChars.contains(b)) {
                    isTextFile = false;
                    break;
                }
            }
            is.close();
        } catch (IOException e) {
            System.err.format("ERROR: Could not read file '%s': %s\n", file, e.getMessage());
        }
        return isTextFile;
    }
    
    /**
     * Converts a wildcard expression to a Java regex.
     * 
     * @param wildcard
     *            The wildcard expression.
     * @param openEnded
     *            True if the expression is open-ended, otherwise false.
     * 
     * @return The Java regex.
     */
    private static String wildcardToRegex(String wildcard, boolean openEnded){
        StringBuilder s = new StringBuilder();
        if (openEnded) {
            s.append('^');
        }
        final int length = wildcard.length();
        for (int i = 0; i < length; i++) {
            final char ch = wildcard.charAt(i);
            switch(ch) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                    // escape special regexp-characters
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append("\\");
                    s.append(ch);
                    break;
                default:
                    s.append(ch);
            }
        }
        if (openEnded) {
            s.append('$');
        }
        return s.toString();
    }
    
    /**
     * The user responses when asked for confirmation.
     * 
     * @author Oscar Stigter
     */
    enum Response {
        
        YES("yes"),

        NO("no"),
        
        ALL("all"),
        
        QUIT("quit"),
        
        ;
        
        private String command;
        
        Response(String command) {
            this.command = command;
        }
        
        public String getCommand() {
            return command;
        }
        
    }

}
