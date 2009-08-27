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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

/**
 * Command line Find & Replace tool to process text files.
 * 
 * Combines the power of 'find', 'grep' and inline text replacement tools.
 *
 * TODO: Add text replacing
 * TODO: Add case sensitivity toggling
 * TODO: Add file renaming
 * TODO: Add file deletion
 * TODO: Add custom command execution
 * TODO: Add ignored directories toggling
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
    
    /** Recurse into subdirectories. */
    private boolean recursion = true;
    
    /** Starting directory. */
    private File dir = new File(".");
    
    /** The file name pattern. */
    private Pattern filePattern;
    
    /** Search text pattern. */
    private Pattern searchPattern;
    
    /** Replace text pattern. */
    private Pattern replacePattern;
    
    /**
     * Constructor.
     * 
     * @param args
     *            the command line arguments
     */
    public Main(String[] args) {
        parseArguments(args);
        
        // Define ignored directories.
        ignoredDirs = new HashSet<String>();
        ignoredDirs.add(".svn");
        ignoredDirs.add(".cvs");
        ignoredDirs.add("SCCS");
        
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
     * Parses the command line arguments.
     * 
     * @param args
     *            The command line arguments
     */
    private void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help") || args[i].equals("-?")) {
                printUsage();
                System.exit(0);
            } else if (args[i].equals("-R")) {
                recursion = false;
            } else if (args[i].equals("-d")) {
                i++;
                if (i < args.length) {
                    dir = new File(args[i]);
                    if (!dir.exists() || !dir.isDirectory()) {
                        System.err.println("Directory not found: " + dir);
                        System.exit(2);
                    }
                } else {
                    System.err.println("No directory specified.");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-f")) {
                i++;
                if (i < args.length) {
                    filePattern = Pattern.compile(args[i]);
                } else {
                    System.err.println("No file pattern specified.");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-s")) {
                i++;
                if (i < args.length) {
                    searchPattern = Pattern.compile(args[i]);
                } else {
                    System.err.println("No search pattern specified.");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-r")) {
                i++;
                if (i < args.length) {
                    replacePattern = Pattern.compile(args[i]);
                } else {
                    System.err.println("No replace pattern specified.");
                    printUsage();
                    System.exit(1);
                }
            } else {
                System.err.println("Unknown argument: " + args[i]);
                printUsage();
                System.exit(1);
            }
        }
    }
    
    /**
     * Prints the program's usage.
     */
    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  jfind [<options>]");
        System.out.println("where <options> are:");
        System.out.println("  -h, --help or -?      = Shows this usage information");
        System.out.println("  -d                    = Start in directory <dir>");
        System.out.println("  -f <file_pattern>     = Process only files with a name matching <file_pattern>");
        System.out.println("  -s <search_pattern>   = Process only files containing <search_pattern>");
        System.out.println("  -r <replace_pattern>  = Replace <search_text> with <replace_pattern>");
        System.out.println("  -R                    = Do not recurse into subdirectories");
        System.out.println("  -i                    = Interactive mode (ask user confirmation)");
//        System.out.println("  -rename <extention>   = Rename files found with <extention>");
//        System.out.println("  -delete               = Delete files found");
//        System.out.println("  -command <command>    = Execute <command> on files found");
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
        // First process files...
        for (File file : dir.listFiles()) {
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
                    if (!ignoredDirs.contains(name)) {
                        path += name + File.separatorChar;
                        processDirectory(file, path);
                    }
                }
            }
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
        if (searchPattern != null && isTextFile(file)) {
            if (file.isFile()) {
                BufferedReader reader = null;
                BufferedWriter writer = null;
                File tempFile = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String line = null;
                    int lineNumber = 0;
                    boolean replaced = false;
                    if (replacePattern != null) {
                        tempFile = new File(file.getPath() + ".temp");
                        writer = new BufferedWriter(new FileWriter(tempFile));
                    }
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        if (replacePattern != null) {
                            // Search and replace.
//                            if (matcher.find()) {
//                                line = matcher.replaceAll(replacePattern);
//                                replaced = true;
//                                // Do not write line if it became empty.
//                                if (line.trim().length() != 0) {
//                                    writer.write(line + '\n');
//                                }
//                            } else {
//                                writer.write(line + '\n');
//                            }
                        } else {
                            // Search only.
                            if (searchPattern.matcher(line).find()) {
                                System.out.println(String.format(
                                        "%s/%s:%d %s", path, file.getName(), lineNumber, line));
                            }
                        }
                    }
                    reader.close();
                    if (replacePattern != null) {
                        writer.close();
                        if (replaced) {
                            file.delete();
                            tempFile.renameTo(file);
                            System.out.println(path + file.getName());
                        } else {
                            tempFile.delete();
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        } else {
            System.out.println(path + file.getName());
        }
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
            System.err.println(e);
        }
        return isTextFile;
    }
    
}
