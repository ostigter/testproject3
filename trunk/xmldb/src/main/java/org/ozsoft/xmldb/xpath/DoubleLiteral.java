package org.ozsoft.xmldb.xpath;

public class DoubleLiteral implements XPathExpression {
    
    private final double value;
    
    public DoubleLiteral(double value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Object context) {
        return value;
    }

}
