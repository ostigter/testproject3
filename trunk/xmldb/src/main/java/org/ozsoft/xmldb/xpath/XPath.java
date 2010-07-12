package org.ozsoft.xmldb.xpath;

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.xmldb.Node;

public class XPath {
    
    /** Forward slash character. */
    private static final char SLASH = '/';

    /** 'at' character (@). */
    private static final char AT = '@';

//    /** Plus character. */
//    private static final char PLUS = '+';
//
//    /** Minus character. */
//    private static final char MINUS = '-';
//
//    /** Star character. */
//    private static final char STAR = '*';
//
//    /** Equals character. */
//    private static final char EQ = '=';
//
//    /** Greater-Than character. */
//    private static final char GT = '>';
//
//    /** Less-Than character. */
//    private static final char LT = '<';
//
//    /** Apostroph character. */
//    private static final char APOS = '\'';
//
//    /** Quote character. */
//    private static final char QUOT = '"';
//
//    /** Left parenthises character. */
//    private static final char LPAREN = '(';
//    
//    /** Right parenthises character. */
//    private static final char RPAREN = ')';
//    
//    /** Left square bracket character. */
//    private static final char LBRACKET = '[';
//    
//    /** Right square bracket character. */
//    private static final char RBRACKET = ']';
    
    /**
     * Private constructor to deny instantiation.
     */
    private XPath() {
        // Empty implementation.
    }
    
    public static XPathExpression compile(String xpath) throws XPathException {
        if (xpath == null || xpath.length() == 0) {
            throw new IllegalArgumentException("Null or empty xpath");
        }
        
        List<Token> tokens = tokenize(xpath);
        
        XPathExpression expr = null;
        
        return expr;
    }
    
    private static List<Token> tokenize(String text) throws XPathException {
        List<Character> buffer = new ArrayList<Character>();
        for (Character ch : text.toCharArray()) {
            buffer.add(ch);
        }

        List<Token> tokens = new ArrayList<Token>();
        
        while (buffer.size() > 0) {
            char ch = buffer.remove(0);
            if (ch == SLASH) {
                if (buffer.isEmpty()) {
                    throw new XPathException("Unexpected EOF after '/', expected node identifier");
                }
                // Determine child or descendant selector.
                boolean descendant = false;
                if (buffer.get(0).equals(SLASH)) {
                    descendant = true;
                    buffer.remove(0);
                    if (buffer.isEmpty()) {
                        throw new XPathException("Unexpected EOF after '//', expected node identifier");
                    }
                }
                // Determine element or attribute selector.
                boolean attribute = false;
                if (buffer.get(0).equals(AT)) {
                    attribute = true;
                    buffer.remove(0);
                    if (buffer.isEmpty()) {
                        throw new XPathException("Unexpected EOF after '@', expected attribute identifier");
                    }
                }
                // Get node identifier.
                if (!descendant && !attribute) {
                    tokens.add(new Token(TokenType.CHILD_ELEMENT));
                }
            } else {
                //TODO
            }
        }
        
        
        return tokens;
    }

//    public static XPathExpression compile(String xpath) throws XPathException {
//        List<Expression> tokens = new ArrayList<Expression>();
//        
//        int length = xpath.length();
//        int i = 0;
//        int left = length;
//        TokenType type = null;
//        StringBuilder sb = new StringBuilder();
//        while (left > 0) {
//            if (left >= 2) {
//                String s = xpath.substring(i, i + 2);
//                if (s.equals("//")) {
//                    tokens.add(new DescendantElement());
//                    i += 2;
//                    left -= 2;
//                    type = null;
//                    continue;
//                }
//            }
//            char c = xpath.charAt(i);
//            if (c == '/') {
//                tokens.add(new ChildElement());
//                type = null;
//            } else if (c == '(') {
//                if (type == TokenType.FUNCTION) {
//                    throw new XPathException("Unexpected character '('");
//                }
//                type = TokenType.FUNCTION;
//            } else if (c == ')') {
//                if (type != TokenType.FUNCTION) {
//                    throw new XPathException("Unexpected character ')'");
//                }
//                type = null;
//            } else if (c == '\"') {
//                if (type == TokenType.STRING_LITERAL) {
//                    tokens.add(new StringLiteral(xpath));
//                }
//            } else {
//                type = TokenType.IDENTIFIER;
//                sb.append(c);
//            }
//            i++;
//            left--;
//        }
//        
//        return expr;
//    }
    
    public static Object evaluate(String xpath, Node context) throws XPathException {
        return XPath.compile(xpath).evaluate(context);
    }

}
