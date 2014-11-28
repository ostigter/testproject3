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
    }

    public static void main(String[] args) {
        new Game().run();
    }

    public void run() {
        Company ozCorp = new Company("OzCorp");
        addCompany(ozCorp);

        Product wheat = products.get("Wheat");
        City smallville = cities.get("Smallville");
        Factory wheatFarm = new Factory(BusinessType.WHEAT_FARM, 1, wheat, ozCorp, smallville);
        ozCorp.addBusiness(wheatFarm);

        System.out.println("Enterprise: " + wheatFarm.getName());
        System.out.println("Company:    " + wheatFarm.getCompany());
        System.out.println("City:       " + wheatFarm.getCity());
        System.out.println("Produces:   " + wheatFarm.getProduct());
        System.out.println("Production: " + wheatFarm.getProduction());
        System.out.println("Inventory:  " + wheatFarm.getInventoryItem(wheat));

        doNextTurn();

        System.out.println("Inventory:  " + wheatFarm.getInventoryItem(wheat));
    }

    public void addCompany(Company company) {
        companies.put(company.getName(), company);
    }

    public Company getCompany(String name) {
        return companies.get(name);
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
        Product wheat = new Product("Wheat", ProductLevel.RAW, 100);
        products.put(wheat.getName(), wheat);

        Product flour = new Product("Flour", ProductLevel.SEMI, 100);
        flour.addIngredient(wheat, 1);
        products.put(flour.getName(), flour);

        Product bread = new Product("Bread", ProductLevel.CONSUMER, 100);
        flour.addIngredient(flour, 1);
        products.put(bread.getName(), bread);
    }

    private void initCities() {
        City smallville = new City("Smallville", 200);
        cities.put(smallville.getName(), smallville);
    }
}
