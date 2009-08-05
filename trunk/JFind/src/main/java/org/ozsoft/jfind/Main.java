package org.ozsoft.jfind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Command line Find & Replace tool to process text files.
 * Combines the power of 'find', 'grep' and inline text replacement tools.
 *
 * TODO: Add text replacing
 * TODO: Add case sensitivity toggle
 * TODO: Add file renaming
 * TODO: Add file deletion
 * TODO: Add custom command execution
 */
public class Main {
	
    /** Recurse into subdirectories. */
    private boolean recursion = true;
    
    /** Starting directory. */
    private File dir = new File(".");
	
    /** File mask. */
    private String fileMask = null;
    
    /** Search text. */
    private String searchText = null;
    
    /** Replace text. */
    private String replaceText = null;
    
    /**
	 * Constructor.
	 * 
	 * @param args
	 *            the command line arguments
	 */
    public Main(String[] args) {
        parseArguments(args);
        
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
            if (args[i].equals("-h") || args[i].equals("-?")) {
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
                    fileMask = args[i];
                } else {
                    System.err.println("No filemask specified.");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-s")) {
                i++;
                if (i < args.length) {
                    searchText = args[i];
                } else {
                    System.err.println("No search text specified.");
                    printUsage();
                    System.exit(1);
                }
            } else if (args[i].equals("-r")) {
                i++;
                if (i < args.length) {
                    replaceText = args[i];
                } else {
                    System.err.println("No replace text specified.");
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
        System.out.println("  -h or -?            = Shows this usage information");
        System.out.println("  -d                  = Start in directory <dir>");
        System.out.println("  -f <file_mask>      = Process only files with a name matching <file_mask>");
        System.out.println("  -s <search_text>    = Process only files containing <search_text>");
        System.out.println("  -r <replace_text>   = Replace <search_text> with <replace_text>");
        System.out.println("  -R                  = Do not recurse into subdirectories");
        System.out.println("  -i                  = Interactive mode (ask user confirmation)");
//        System.out.println("  -rename <extention>      = Rename files found with <extention>");
//        System.out.println("  -delete                  = Delete files found");
//        System.out.println("  -command <command>       = Execute <command> on files found");
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
        // Process files first.
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                String name = file.getName();
                if (fileMask != null) {
                    if (name.indexOf(fileMask) != -1) {
                        processFile(file, path);
                    }
                } else {
                    processFile(file, path);
                }
            }
        }
        // Recurse into subdirectories.
        if (recursion) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    path += file.getName() + File.separatorChar;
                    processDirectory(file, path);
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
        if (searchText != null) {
            if (file.isFile()) {
                BufferedReader reader = null;
                BufferedWriter writer = null;
                File tempFile = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String line = null;
                    int lineNumber = 0;
                    boolean replaced = false;
                    if (replaceText != null) {
                        tempFile = new File(file.getPath() + ".temp");
                        writer = new BufferedWriter(new FileWriter(tempFile));
                    }
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;
                        if (replaceText != null) {
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
                            if (line.indexOf(searchText) != -1) {
                                System.out.println(String.format(
                                		"%s/%s:%d %s", path, file.getName(), lineNumber, line));
                            }
                        }
                    }
                    reader.close();
                    if (replaceText != null) {
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
    
}
