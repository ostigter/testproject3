package org.ozsoft.xmldb.xpath;

public class IntegerLiteral implements XPathExpression {
    
    private final int value;
    
    public IntegerLiteral(int value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Object context) {
        return value;
    }

}
