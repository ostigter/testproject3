package org.ozsoft.jgrep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Console application to search for specific text in a single file or
 * directory tree.
 * 
 * @author Oscar Stigter
 */
public abstract class Main {
	
	/** Linefeed (LF) character. */
	private static final char LF = '\n';
	
	/** Carriage return (CR) character. */
	private static final char CR = '\r';
	
	/** Text pattern to search for. */
	private static char[] pattern;
	
	/** Current line number. */
	private static int lineNumber;
	
	/** Total number of matches found. */
	private static int totalMatches;
	
	
	/**
	 * Test driver.
	 * 
	 * @param  args  command line arguments
	 */
	public static void main(String[] args) {
	    if (args.length == 1) {
	        search(".", args[0]);
	    } else if (args.length == 2) {
	        search(args[1], args[0]);
	    } else {
	        System.out.println("Usage:  jgrep <text> [<file> | <dir>]");
	    }
	}
	
	/**
	 * Searches a file or directory looking for a specific text.
	 *  
	 * @param  path  the path to the file or directory
	 * @param  text  the text to search for
	 */
	public synchronized static void search(String path, String text) {
		File file = new File(path);
		if (file.exists()) {
			pattern = text.toCharArray();
			totalMatches = 0;
			search(file);
//	        System.out.println("\nMatches found: " + totalMatches);
		} else {
			System.err.println("File or directory not found: " + path);
		}
	}
	
	
	/**
	 * Searches a file or directory.
	 * 
	 * @param  dir  the file or directory
	 */
	private static void search(File file) {
		if (file.isDirectory()) {
		    String name = file.getName();
		    if (!name.equals("SCCS") && !name.equals(".svn")) {
	            // Process files first...
	            for (File f : file.listFiles()) {
	                if (f.isFile()) {
	                    search(f);
	                }
	            }
	            // ...then recurse into subdirectories.
	            for (File f : file.listFiles()) {
	                if (f.isDirectory()) {
	                    search(f);
	                }
	            }
		    }
		} else {
			searchFile(file);
		}
	}
	
	
	/**
	 * Searches in a single file.
	 * 
	 * @param  file  the file
	 */
	private static void searchFile(File file) {
		try {
			FileInputStream is = new FileInputStream(file);
			lineNumber = 1;
			int pos = 0;
			boolean lineMatches = false;
			StringBuilder sb = new StringBuilder();
			while (is.available() > 0) {
				char c = (char) is.read();
				if (c == pattern[pos]) {
					// Partial match at least so far...
					pos++;
					if (pos == pattern.length) {
						// ...and a full match!
						totalMatches++;
						lineMatches = true;
						pos = 0;
					}
				} else {
					// No match; reset any partial match.
					pos = 0;
				}
				if (c == LF) {
					// Linefeed: reached the end of the current line.
					if (lineMatches) {
						// Show matching line.
						printLine(file, lineNumber, sb.toString());
						lineMatches = false;
					}
					sb.delete(0, sb.length());
					lineNumber++;
				} else if (c == CR) {
					// Carriage return: ignore (Windows only).
				} else {
					// Just copy any other character.
					sb.append(c);
				}
			}
			is.close();
		
			// Show any pending matching line left (no last LF at end of file).
			if (lineMatches) {
				printLine(file, lineNumber, sb.toString());
			}
		} catch (IOException e) {
			System.err.println("Could not read file: " + e.getMessage());
		}
	}
	
	
	/**
	 * Prints a matching line.
	 * 
	 * @param  lineNumber  the line number
	 * @param  line        the line text
	 */
	private static void printLine(File file, int lineNumber, String text) {
//		System.out.println(String.format(
//				"\n%s, line %d:\n    %s",
//				getPath(file), lineNumber, text.trim()));
        System.out.println(String.format("%s:%d:%s",
                getPath(file), lineNumber, text.trim()));
	}
	

	/**
	 * Returns clean, relative path of a file.
	 * 
	 * @param  file  the file
	 * 
	 * @return  the path
	 */
	private static String getPath(File file) {
//        String path = file.getPath().replace('\\', '/');
//        if (path.startsWith("./")) {
//            path = path.substring(2);
//        }
//	    return path;
	    return file.getPath();
	}

}
