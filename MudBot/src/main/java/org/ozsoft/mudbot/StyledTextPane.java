package org.ozsoft.mudbot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Customized JTextPane for easy text appending, optionally in color.
 * 
 * @author Oscar Stigter
 */
public class StyledTextPane extends JPanel {

    private static final long serialVersionUID = 5311151074593998126L;

    private final JTextPane textPane;

    private final StyledDocument document;

    private final Style style;

    public StyledTextPane() {
        super();

        setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setBackground(Color.BLACK);
        textPane.setForeground(Color.LIGHT_GRAY);
        textPane.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        textPane.setEditable(false);
        add(BorderLayout.CENTER, new JScrollPane(textPane));

        document = (StyledDocument) textPane.getDocument();
        style = document.addStyle(null, null);
    }

    public void setColor(Color color) {
        if (color != null) {
            style.addAttribute(StyleConstants.Foreground, color);
        } else {
            // Reset to default color.
            style.removeAttribute(StyleConstants.Foreground);
        }
    }

    public void append(String text) {
        try {
            document.insertString(document.getLength(), text, style);
            textPane.setCaretPosition(document.getLength() - 1);
        } catch (BadLocationException e) {
            System.err.println("StyledTextPane: ERROR appending text: " + e);
        }
    }
    
    public void clear() {
        textPane.setText("");
    }

}
