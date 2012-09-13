package org.ozsoft.ssh;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream to handle incoming Telnet/SSH characters, separating ANSI escape
 * codes from normal characters.
 * 
 * @author Oscar Stigter
 */
public class AnsiFilterOutputStream extends OutputStream {

    private static final char ESC = 27;

    private final StringBuilder ansiCode;

    private boolean inAnsi;

    public AnsiFilterOutputStream() {
        ansiCode = new StringBuilder();
        inAnsi = false;
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        if (inAnsi) {
            if (c == '[') {
                // Skip
            } else if (Character.isLowerCase(b)) {
                // End ANSI code.
                ansiCode.append(c);
                handleAnsiCode(ansiCode.toString());
                ansiCode.delete(0, ansiCode.length());
                inAnsi = false;
            } else {
                // Collect part of ANSI code.
                ansiCode.append(c);
            }
        } else if (c == ESC) {
            // Begin ASNI code.
            inAnsi = true;
        } else {
            // Handles as normal text.
            handleCharacter(c);
        }
    }

    private void handleCharacter(char c) {
        // FIXME: Do something smarter than just write all text to the console.
        System.out.print(c);
    }

    private void handleAnsiCode(String ansiCode) {
        // Not implemented.
    }

}
