package org.ozsoft.xmldb.xpath;

public class IntegerLiteral implements Expression {
    
    private final long value;
    
    public IntegerLiteral(long value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Object context) {
        return new Long(value);
    }

}
