package org.ozsoft.freecap;

/**
 * Business types.
 * 
 * @author Oscar Stigter
 */
public enum BusinessType {

    WHEAT_FARM("Wheat Farm", "WF"),

    FLOUR_MILL("Flour Mill", "FM"),

    BAKERY("Bakery", "BK"),

    GROCERY_SHOP("Grocery Shop", "GS"),

    ;

    private final String name;

    private final String prefix;

    BusinessType(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }
}
