package org.ozsoft.xmldb.xpath;

public class BooleanLiteral implements Expression {
    
    private final boolean value;
    
    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Object context) {
        return new Boolean(value);
    }

}
