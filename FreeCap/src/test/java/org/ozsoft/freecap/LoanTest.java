package org.ozsoft.freecap;

import org.junit.Assert;
import org.junit.Test;

public class LoanTest {

    private static final double EPSILON = 1e-02;

    @Test
    public void test() {
        // $1M in 10 years against 8 %
        Loan loan = new Loan(1000000.0, 10, 8.0);

        // Week 1
        Assert.assertEquals(1000000.0, loan.getDebt(), EPSILON);
        Assert.assertEquals(166.67, loan.getWeeklyInterest(), EPSILON);
        Assert.assertEquals(2083.33, loan.doDownpayment(), EPSILON);

        // Week 2
        Assert.assertEquals(997916.66, loan.getDebt(), EPSILON);
        Assert.assertEquals(166.32, loan.getWeeklyInterest(), EPSILON);
        Assert.assertEquals(2083.33, loan.doDownpayment(), EPSILON);

        // Week 3
        Assert.assertEquals(995833.33, loan.getDebt(), EPSILON);
        Assert.assertEquals(165.97, loan.getWeeklyInterest(), EPSILON);
        Assert.assertEquals(2083.33, loan.doDownpayment(), EPSILON);

        for (int i = 3; i < 10 * 48; i++) {
            Assert.assertEquals(2083.33, loan.doDownpayment(), EPSILON);
        }

        // Verify that loan has been fully paid.
        Assert.assertEquals(0.0, loan.getDebt(), EPSILON);
        Assert.assertEquals(0.0, loan.getWeeklyInterest(), EPSILON);
        Assert.assertEquals(0.0, loan.doDownpayment(), EPSILON);
    }
}
