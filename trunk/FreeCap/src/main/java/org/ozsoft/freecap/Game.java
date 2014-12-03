package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public class Game implements GameListener {

    private final Map<String, City> cities;

    private final Map<String, Company> companies;

    public Game() {
        cities = new HashMap<String, City>();
        companies = new HashMap<String, Company>();
    }

    public static void main(String[] args) {
        new Game().run();
    }

    public void run() {
        initCities();
        initCompanies();
        initBusinesses();
        setSuppliers();

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

    private void addBusiness(Business business) {
        business.getCompany().addBusiness(business);
    }

    @Override
    public void doNextTurn() {
        // Produce raw products.
        for (Company company : companies.values()) {
            for (Business business : company.getBusinesses()) {
                if (business.getProduct().getLevel() == ProductLevel.RAW) {
                    business.doNextTurn();
                }
            }
        }

        // Produce intermediate products.
        for (Company company : companies.values()) {
            for (Business business : company.getBusinesses()) {
                if (business.getProduct().getLevel() == ProductLevel.INTERMEDIATE) {
                    business.doNextTurn();
                }
            }
        }

        // Produce and consume finished products.
        for (Company company : companies.values()) {
            for (Business business : company.getBusinesses()) {
                if (business.getProduct().getLevel() == ProductLevel.FINISHED) {
                    business.doNextTurn();
                }
            }
        }

        for (Company company : companies.values()) {
            company.doNextTurn();
        }

        for (City city : cities.values()) {
            city.doNextTurn();
        }
    }

    private void initCities() {
        City smallville = new City("Smallville", 200);
        cities.put(smallville.getName(), smallville);
    }

    private void initCompanies() {
        Company ozCorp = new Company("OzCorp", 0);
        addCompany(ozCorp);
        ozCorp.loan(1000000.00, 10, 8.0);
    }

    private void initBusinesses() {
        Company ozCorp = getCompany("OzCorp");
        City smallville = getCity("Smallville");

        addBusiness(new Factory(BusinessType.WHEAT_FARM, 1, ozCorp, smallville));
        addBusiness(new Factory(BusinessType.FLOUR_MILL, 1, ozCorp, smallville));
        addBusiness(new Factory(BusinessType.BAKERY, 1, ozCorp, smallville));
        addBusiness(new Shop(BusinessType.GROCERY_SHOP, 1, ozCorp, smallville));
    }

    private void setSuppliers() {
        Company ozCorp = getCompany("OzCorp");

        Business wheatFarm = ozCorp.getBusiness("Wheat Farm 'WF01'");
        wheatFarm.setSalesPrice(1.00);

        Factory flourMill = (Factory) ozCorp.getBusiness("Flour Mill 'FM01'");
        flourMill.setSupplier(wheatFarm);
        flourMill.setSalesPrice(1.50);

        Factory bakery = (Factory) ozCorp.getBusiness("Bakery 'BK01'");
        bakery.setSupplier(flourMill);
        bakery.setSalesPrice(2.00);

        Business groceryShop = ozCorp.getBusiness("Grocery Shop 'GS01'");
        groceryShop.setSupplier(bakery);
        groceryShop.setSalesPrice(2.50);
    }
}
