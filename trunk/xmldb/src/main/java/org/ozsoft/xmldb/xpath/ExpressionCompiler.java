package org.ozsoft.xmldb.xpath;

import java.util.ArrayList;
import java.util.List;

public class ExpressionCompiler {
    
    private static final char SLASH = '/';

    private static final char LPAREN = '(';
    
    private static final char RPAREN = ')';
    
    public static Expression compile(String text) {
        List<Token> tokens = tokenize(text);
        
        Expression expr = null;
        
        return expr;
    }
    
    private static List<Token> tokenize(String text) {
        List<Character> buffer = new ArrayList();
        for (Character ch : text.toCharArray()) {
            buffer.add(ch);
        }

        List<Token> tokens = new ArrayList<Token>();
        
        while (buffer.size() > 0) {
            char ch = buffer.remove(0);
            if (ch == SLASH) {
                tokens.add(new Token(TokenType.CHILD_ELEMENT));
            } else if (ch == LPAREN) {
                
            } else if (ch == RPAREN) {
                
            } else {
                
            }
        }
        
        
        return tokens;
    }

}
