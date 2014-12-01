package org.ozsoft.freecap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Company implements GameListener {

    private final String name;

    private final Map<String, Business> businesses;

    private final List<Loan> loans;

    private double cash;

    public Company(String name, double cash) {
        this.name = name;
        this.cash = cash;
        businesses = new HashMap<String, Business>();
        loans = new ArrayList<Loan>();
    }

    public String getName() {
        return name;
    }

    public double getCash() {
        return cash;
    }

    public Collection<Business> getBusinesses() {
        return businesses.values();
    }

    public Business getBusiness(String name) {
        return businesses.get(name);
    }

    public void addBusiness(Business business) {
        businesses.put(business.getName(), business);
    }

    /**
     * Adds a new loan.
     * 
     * @param amount
     *            The amount of money to loan.
     * @param period
     *            The loan period in years.
     * @param interestRate
     *            The annual interest rate (e.g. 8.0).
     */
    public void loan(double amount, int period, double interestRate) {
        loans.add(new Loan(amount, period, interestRate));
        cash += amount;
        System.out.format("%s takes a loan for $%,.0f against %.1f %%, to be paid in %d years.\n", this, amount, interestRate, period);
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public double getLoanDebt() {
        double debt = 0.0;
        for (Loan loan : loans) {
            debt += loan.getDebt();
        }
        return debt;
    }

    public double getWeeklyInterest() {
        double interest = 0.0;
        for (Loan loan : loans) {
            interest += loan.getWeeklyInterest();
        }
        return interest;
    }

    public void pay(double amount) {
        if (amount > cash) {
            throw new IllegalArgumentException(String.format("%s has insufficient cash (%,.2f) to pay %,.2f!", this, cash, amount));
        }

        cash -= amount;
        System.out.format("%s payed $%,.2f (cash: $%,.0f)\n", this, amount, cash);
    }

    public void receivePayment(double amount) {
        cash += amount;
        System.out.format("%s received $%,.2f (cash: $%,.0f)\n", this, amount, cash);
    }

    @Override
    public void doNextTurn() {
        payDebts();
    }

    @Override
    public String toString() {
        return name;
    }

    private void payDebts() {
        double paid = 0.0;
        for (Loan loan : loans) {
            double toPay = loan.getWeeklyInterest() + loan.doDownpayment();
            pay(toPay);
            paid += toPay;

        }
        if (paid > 0) {
            System.out.format("%s pays $%,.2f to the bank for outstanding loans.\n", this, paid);
        }
    }
}
