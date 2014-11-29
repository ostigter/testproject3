package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public class Game implements GameListener {

    private final Map<String, Product> products;

    private final Map<String, City> cities;

    private final Map<String, Company> companies;

    public Game() {
        products = new HashMap<String, Product>();
        cities = new HashMap<String, City>();
        companies = new HashMap<String, Company>();
        initProducts();
        initCities();
        initCompanies();
        initBusinesses();
    }

    public static void main(String[] args) {
        new Game().run();
    }

    public void run() {
        doNextTurn();
    }

    public void addCompany(Company company) {
        companies.put(company.getName(), company);
    }

    public Company getCompany(String name) {
        return companies.get(name);
    }

    public City getCity(String name) {
        return cities.get(name);
    }

    public Product getProduct(String name) {
        return products.get(name);
    }

    @Override
    public void doNextTurn() {
        for (Company company : companies.values()) {
            company.doNextTurn();
        }
        for (City city : cities.values()) {
            city.doNextTurn();
        }
    }

    private void initProducts() {
        Product wheat = new Product("wheat", ProductLevel.RAW, 1000, 0);
        products.put(wheat.getName(), wheat);

        Product flour = new Product("flour", ProductLevel.INTERMEDIATE, 1000, 0);
        flour.addIngredient(wheat, 1);
        products.put(flour.getName(), flour);

        Product bread = new Product("bread", ProductLevel.FINISHED, 1000, 1);
        flour.addIngredient(flour, 1);
        products.put(bread.getName(), bread);
    }

    private void initCities() {
        City smallville = new City("Smallville", 200);
        cities.put(smallville.getName(), smallville);
    }

    private void initCompanies() {
        Company ozCorp = new Company("OzCorp");
        addCompany(ozCorp);
    }

    private void initBusinesses() {
        Company ozCorp = getCompany("OzCorp");
        City smallville = getCity("Smallville");
        Product wheat = getProduct("wheat");
        Product flour = getProduct("flour");
        Product bread = getProduct("bread");

        Factory wheatFarm = new Factory(BusinessType.WHEAT_FARM, 1, wheat, ozCorp, smallville);
        smallville.addBusiness(wheatFarm);
        ozCorp.addBusiness(wheatFarm);
        System.out.println("\nBusiness:   " + wheatFarm.getName());
        System.out.println("Company:    " + wheatFarm.getCompany());
        System.out.println("City:       " + wheatFarm.getCity());
        System.out.println("Produces:   " + wheatFarm.getProduct());
        System.out.println("Production: " + wheatFarm.getProduction());
        // System.out.println("Inventory:  " + wheatFarm.getInventoryItem(wheat));

        Factory flourMill = new Factory(BusinessType.FLOUR_MILL, 1, flour, ozCorp, smallville);
        smallville.addBusiness(flourMill);
        ozCorp.addBusiness(flourMill);
        System.out.println("\nBusiness:   " + flourMill.getName());
        System.out.println("Company:    " + flourMill.getCompany());
        System.out.println("City:       " + flourMill.getCity());
        System.out.println("Produces:   " + flourMill.getProduct());
        System.out.println("Production: " + flourMill.getProduction());
        // System.out.println("Inventory:  " + flourMill.getInventoryItem(wheat));

        Factory bakery = new Factory(BusinessType.BAKERY, 1, bread, ozCorp, smallville);
        smallville.addBusiness(bakery);
        ozCorp.addBusiness(bakery);
        System.out.println("\nBusiness:   " + bakery.getName());
        System.out.println("Company:    " + bakery.getCompany());
        System.out.println("City:       " + bakery.getCity());
        System.out.println("Produces:   " + bakery.getProduct());
        System.out.println("Production: " + bakery.getProduction());
        // System.out.println("Inventory:  " + bakery.getInventoryItem(wheat));
    }
}
