package org.ozsoft.jdiff;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds differences between two text files.
 * 
 * @author Oscar Stigter
 */
public class JDiff {
    
	private List<DiffLine> diffLines;
    
    public JDiff(String[] source, String[] dest) {
        createDiffLines(source, dest);
    }
    
    public String[] getDiffLines() {
        String[] array = new String[diffLines.size()];
        for (int i = 0; i < diffLines.size(); i++) {
            array[i] = diffLines.get(i).toString();
        }
        return array;
    }
    
    private void createDiffLines(String[] source, String[] dest) {
        int sourceLineNumber = 0;
        int destLineNumber   = 0;
        String sourceLine;
        String destLine;
        boolean found;
        int i;

        diffLines = new ArrayList<DiffLine>();
        
        // Process source lines
        while (sourceLineNumber < source.length) {
            sourceLine = source[sourceLineNumber];
            if (destLineNumber < dest.length) {
                // We have destination lines left --> compare
                destLine = dest[destLineNumber];
                if (sourceLine.equals(destLine)) {
                    // Lines identical
                    diffLines.add(new DiffLine(DiffType.IDENTICAL, sourceLine,
                            sourceLineNumber + 1, destLine,
                            destLineNumber + 1));
                    sourceLineNumber++;
                    destLineNumber++;
                } else {
                    // Lines differ.
                    // Does a matching destination line exist?
                    found = false;
                    for (i = destLineNumber + 1; i < dest.length; i++) {
                        if (dest[i].equals(sourceLine)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        // Matching destination line found.
                        // Does a matching source line exists too?
                        found = false;
                        for (i = sourceLineNumber + 1; i < source.length; i++) {
                            if (source[i].equals(destLine)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            // Matching source line found --> line replaced
                            diffLines.add(new DiffLine(
                                    DiffType.DELETED, sourceLine,
                                    sourceLineNumber + 1, null, 0));
                            diffLines.add(
                                    new DiffLine(DiffType.ADDED, null, 0,
                                            destLine, destLineNumber + 1));
                            sourceLineNumber++;
                            destLineNumber++;
                        } else {
                            // No matching source line found --> line added
                            diffLines.add(new DiffLine(
                                    DiffType.ADDED, null, 0, destLine,
                                    destLineNumber + 1));
                            destLineNumber++;
                        }
                    } else {
                        // No matching destination line --> line deleted
                        diffLines.add(new DiffLine(
                                DiffType.DELETED, sourceLine,
                                sourceLineNumber + 1, null, 0));
                        sourceLineNumber++;
                    }
                }
            } else {
                // No destination lines remaining --> deleted
                diffLines.add(new DiffLine(
                        DiffType.DELETED, sourceLine,
                        sourceLineNumber + 1, null, 0));
                sourceLineNumber++;
            }
        }

        // Process remaining (added) destination lines
        while (destLineNumber < dest.length) {
            // Line added
            diffLines.add(new DiffLine(
                    DiffType.ADDED, null, 0, dest[destLineNumber],
                    destLineNumber + 1));
            destLineNumber++;
        }
    }
    
}
