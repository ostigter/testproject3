package org.ozsoft.freecap;

public enum BusinessType {

    WHEAT_FARM("Wheat Farm", "WF", Product.WHEAT, 100000, 1000),

    FLOUR_MILL("Flour Mill", "FM", Product.FLOUR, 100000, 1000),

    BAKERY("Bakery", "BK", Product.BREAD, 20000, 1000),

    GROCERY_SHOP("Grocery Shop", "GS", Product.BREAD, 100000, 1000),

    ;

    private final String name;

    private final String prefix;

    private final Product product;

    private final double buildingCost;

    private final double weeklyCost;

    BusinessType(String name, String prefix, Product product, double buildingCost, double weeklyCost) {
        this.name = name;
        this.prefix = prefix;
        this.product = product;
        this.buildingCost = buildingCost;
        this.weeklyCost = weeklyCost;
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

    public double getBuildingCost() {
        return buildingCost;
    }

    public double getWeeklyCost() {
        return weeklyCost;
    }
}
