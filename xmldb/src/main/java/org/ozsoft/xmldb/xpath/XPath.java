package org.ozsoft.xmldb.xpath;

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.xmldb.Node;

public class XPath {
    
    private XPath() {
        // Empty implementation.
    }
    
    public static Expression compile(String xpath) throws XPathException {
        if (xpath == null || xpath.length() == 0) {
            throw new IllegalArgumentException("Null or empty xpath");
        }
        
        Expression expr = null;
        
        List<Expression> tokens = new ArrayList<Expression>();
        
        int length = xpath.length();
        int i = 0;
        int left = length;
        TokenType type = null;
        StringBuilder sb = new StringBuilder();
        while (left > 0) {
            if (left >= 2) {
                String s = xpath.substring(i, i + 2);
                if (s.equals("//")) {
                    tokens.add(new DescendantExpression());
                    i += 2;
                    left -= 2;
                    type = null;
                    continue;
                }
            }
            char c = xpath.charAt(i);
            if (c == '/') {
                tokens.add(new ChildExpression());
                type = null;
            } else if (c == '(') {
                if (type == TokenType.FUNCTION) {
                    throw new XPathException("Unexpected character '('");
                }
                type = TokenType.FUNCTION;
            } else if (c == ')') {
                if (type != TokenType.FUNCTION) {
                    throw new XPathException("Unexpected character ')'");
                }
                type = null;
            } else if (c == '\"') {
                if (type == TokenType.STRING_LITERAL) {
                    tokens.add(new StringLiteral(xpath));
                }
            } else {
                type = TokenType.IDENTIFIER;
                sb.append(c);
            }
            i++;
            left--;
        }
        
        return expr;
    }
    
    public static Object evaluate(String xpath, Node context) throws XPathException {
        Object result = null;
        return result;
    }

}
