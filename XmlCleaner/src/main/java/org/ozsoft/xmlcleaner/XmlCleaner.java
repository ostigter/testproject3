package org.ozsoft.xmlcleaner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command line tool to clean invalid XML files.
 * 
 * Usage:
 * 	java -jar XmlCleaner <input_file>
 * 
 * It performes the following steps:
 * <ul>
 *   <li>convert to UTF-8 encoding</li>
 *   <li>resolve DTD-style symbols (e.g. "&#40;") to literal characters (e.g. "(")</li>
 *   <li>escape XML-reserved characters (e.g. "<" to "&gt;")</li>
 *   <li>filter out non-visual characters (e.g. BACKSPACE, ESC)</li>
 * </ul>
 * 
 * @author Oscar Stigter
 */
public class XmlCleaner {
    
    /** Default character encoding. */ 
    private static final String ENCODING = "UTF-8";
    
    /** Backspace character. */
    private static final int BACKSPACE = 8;
    
    /** Escape (ESC) character. */
    private static final int ESCAPE = 27;
    
    /** Regex pattern to find DTD-style symbols. */
    private static final Pattern PATTERN = Pattern.compile("&#(\\d+);");

    /** Platform dependent newline character. */
    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * Constructor.
     * 
     * @param args
     *            Command line arguments.
     */
    public XmlCleaner(String[] args) {
	if (args.length == 0) {
	    System.err.println("ERROR: Missing input filename argument");
	    System.exit(1);
	}
	cleanXmlFile(args[0]);
    }

    /**
     * Application's entry point.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
	new XmlCleaner(args);
    }
    
    /**
     * Cleans an XML file.
     * 
     * @param inFilename
     *            The filename of the XML file to clean.
     */
    private void cleanXmlFile(String inFilename) {
	// Check input file.
	File inFile = new File(inFilename);
	if (!inFile.isFile()) {
	    System.err.println("ERROR: Input file not found: " + inFilename);
	    System.exit(2);
	}

	// Prepare output file based.
	String outFilename = null;
	int pos = inFilename.lastIndexOf('.');
	if (pos != -1) {
	    String basename = inFilename.substring(0, pos);
	    String extention = inFilename.substring(pos);
	    outFilename = basename + "_cleaned" + extention;
	} else {
	    outFilename = inFilename + "_cleaned";
	}
	File outFile = new File(outFilename);
	
	// Start actual cleaning. 
	System.out.println("Cleaning file...");
	long replacements = 0L;
	BufferedReader reader = null;
	BufferedWriter writer = null;
	try {
	    reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), ENCODING));
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), ENCODING));
	    String line = null;
	    while ((line = reader.readLine()) != null) {
		// Look for DTD-style symbols.
		Matcher matcher = PATTERN.matcher(line);
		while (matcher.find()) {
		    // Grab the symbol as a whole (e.g. "&#40;") 
		    String symbol = matcher.group();
		    // Extract the symbol's numeric encoding (e.g. "40").
		    String asciiNumber = matcher.group(1);
		    try {
			// Convert numeric encoding to its character representation.
			char ch = (char) Integer.parseInt(asciiNumber);
			// Replace XML-reserved characters.
			String replaced = null;
			if (ch == '<') {
			    replaced = "&lt;";
			} else if (ch == '>') {
			    replaced = "&gt;";
			} else if (ch == '&') {
			    replaced = "&amp;";
//			} else if (ch == '\'') {  // Literal single quotes are allowed.
//			    replaced = "&apos;";
//			} else if (ch == '\"') {
//			    replaced = "&quot;";  // Literal double quotes are allowed.
			} else {
			    // Filter out non-visual characters (Oracle export bug).
			    if (ch == BACKSPACE || ch == ESCAPE) {
				replaced = "";
			    } else {
				replaced = String.valueOf(ch);
			    }
			} 
			line = line.replace(symbol, replaced);
			replacements++;
		    } catch (NumberFormatException e) {
			System.err.println("ERROR: Could not convert symbol with numeric code: " + asciiNumber);
		    }
		}
		writer.write(line);
		writer.write(NEWLINE);
	    }
	    System.out.format("Done.\nReplaced %d symbols.\n", replacements);
	} catch (IOException e) {
	    System.err.println("ERROR: %s" + e.getMessage());
	    e.printStackTrace(System.err);
	} finally {
	    if (writer != null) {
		try {
		    writer.close();
		} catch (IOException e) {
		    System.err.println("WARNING: Could not close writer");
		}
	    }
	    if (reader != null) {
		try {
		    reader.close();
		} catch (IOException e) {
		    System.err.println("WARNING: Could not close reader");
		}
	    }
	}
    }

}
