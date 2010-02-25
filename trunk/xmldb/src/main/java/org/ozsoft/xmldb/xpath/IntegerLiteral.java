package org.ozsoft.xmldb.xpath;

public class IntegerLiteral implements Expression {
    
    private final int value;
    
    public IntegerLiteral(int value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Object context) {
        return value;
    }

}
