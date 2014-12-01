package org.ozsoft.freecap;

/**
 * Linear loan.
 * 
 * @author Oscar Stigter
 */
public class Loan {

    /** Numbers of weeks per year (assuming 4 weeks per month). */
    private static final int WEEKS_PER_YEAR = 4 * 12;

    /** Duration of the loan in weeks. */
    private final int duration;

    /** Annual interest rate. */
    private final double interestRate;

    /** Remaining amount of debt. */
    private double debt;

    /** Remaining duration of the loan in weeks. */
    private int remainingDuration;

    public Loan(double amount, int duration, double interestRate) {
        this.debt = amount;
        this.duration = duration * WEEKS_PER_YEAR;
        this.interestRate = interestRate;
        remainingDuration = this.duration;
    }

    public double getDebt() {
        return debt;
    }

    public double getWeeklyInterest() {
        return (interestRate / duration / 100) * debt;
    }

    public double doDownpayment() {
        if (remainingDuration > 0) {
            double payedAmount = debt / remainingDuration;
            debt -= payedAmount;
            remainingDuration--;
            return payedAmount;
        } else {
            return 0.0;
        }
    }
}
