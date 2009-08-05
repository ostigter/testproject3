package org.ozsoft.jdiff;

/**
 * Contains diff information for a single text line.
 *  
 * @author Oscar Stigter
 */
public class DiffLine {
    
    private final DiffType type;
    private final String sourceLine;
    private final int sourceLineNumber;
    private final String destLine;
    private final int destLineNumber;
    
    public DiffLine(DiffType type, String sourceLine, int sourceLineNumber,
            String destLine, int destLineNumber) {
        this.type = type;
        this.sourceLine = sourceLine;
        this.sourceLineNumber = sourceLineNumber;
        this.destLine = destLine;
        this.destLineNumber = destLineNumber;
    }

    @Override
    public String toString() {
        String s = null;
        switch (type) {
            case IDENTICAL: {
                s = sourceLineNumber + ": " + sourceLine + "\t" + destLineNumber + ": " + destLine;
                break;
            }
            case ADDED: {
                s = "\t" + destLineNumber + ": " + destLine + "\t(Added)";
                break;
            }
            case DELETED: {
                s = sourceLineNumber + ": " + sourceLine + "\t\t(Deleted)";
                break;
            }
            case MODIFIED: {
                s = sourceLineNumber + ": " + sourceLine + "\t" + destLineNumber + ": " + destLine + "\t(Modified)";
                break;
            }
            default: {
                // Should never happen.
                s = "<Invalid DiffType>";
                break;
            }
        }
        return s;
    }

}
