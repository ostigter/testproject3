package org.ozsoft.xmldb.xpath;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Token {
    
    private final TokenType type;
    
    private final List<Object> arguments;
    
    public Token(TokenType type) {
        this.type = type;
        arguments = new LinkedList<Object>();
    }
    
    public TokenType getType() {
        return type;
    }
    
    public List<Object> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
    
    public void addArgument(Object argument) {
        arguments.add(argument);
    }

}
