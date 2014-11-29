package org.ozsoft.freecap;

/**
 * Business types.
 * 
 * @author Oscar Stigter
 */
public enum BusinessType {

    WHEAT_FARM("Wheat Farm", "WF", Product.WHEAT),

    FLOUR_MILL("Flour Mill", "FM", Product.FLOUR),

    BAKERY("Bakery", "BK", Product.BREAD),

    GROCERY_SHOP("Grocery Shop", "GS", Product.BREAD),

    ;

    private final String name;

    private final String prefix;

    private final Product product;

    BusinessType(String name, String prefix, Product product) {
        this.name = name;
        this.prefix = prefix;
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public Product getProduct() {
        return product;
    }
}
