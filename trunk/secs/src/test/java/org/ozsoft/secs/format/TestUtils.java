package org.ozsoft.secs.format;

import org.junit.Assert;

public abstract class TestUtils {

    public static void assertEquals(byte[] expected, byte[] actual) {
        if (actual.length != expected.length) {
            Assert.fail(String.format("Byte arrays are of different length (expected: %d, actual: %d)", expected.length, actual.length));
        }
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] != expected[i]) {
                Assert.fail(String.format("Byte %d is not equal (expected: %d, actual: %d)", expected[i], actual[i]));
            }
        }
    }

}
