package org.ozsoft.xmldb.xpath;

public class StringLiteral implements Expression {
    
    private final String value;
    
    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Object context) {
        return value;
    }

}
