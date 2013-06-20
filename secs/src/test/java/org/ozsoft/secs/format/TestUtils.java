package org.ozsoft.secs.format;

import org.junit.Assert;

public abstract class TestUtils {

    public static void assertEquals(byte[] expected, byte[] actual) {
        boolean isEqual = (actual.length == expected.length);
        for (int i = 0; isEqual && (i < actual.length); i++) {
            if (actual[i] != expected[i]) {
                isEqual = false;
            }
        }
        if (!isEqual) {
            B expectedB = new B(expected);
            B actualB = new B(actual);
            Assert.fail(String.format("Byte arrays differ -- expected: %s; actual: %s", expectedB, actualB));
        }
    }

}
