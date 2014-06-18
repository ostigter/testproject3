package org.ozsoft.password;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PasswordGeneratorTest {

    @Test
    public void test() {
        PasswordGenerator pg = new PasswordGenerator("abc", 1);
        assertEquals("a", pg.getNextPassword());
        assertEquals("b", pg.getNextPassword());
        assertEquals("c", pg.getNextPassword());
        assertEquals("aa", pg.getNextPassword());
        assertEquals("ab", pg.getNextPassword());
        assertEquals("ac", pg.getNextPassword());
        assertEquals("ba", pg.getNextPassword());
        assertEquals("bb", pg.getNextPassword());
        assertEquals("bc", pg.getNextPassword());
        assertEquals("ca", pg.getNextPassword());
        assertEquals("cb", pg.getNextPassword());
        assertEquals("cc", pg.getNextPassword());
        assertEquals("aaa", pg.getNextPassword());
        assertEquals("aab", pg.getNextPassword());
        assertEquals("aac", pg.getNextPassword());
        assertEquals("aba", pg.getNextPassword());
        assertEquals("abb", pg.getNextPassword());
        assertEquals("abc", pg.getNextPassword());

        pg = new PasswordGenerator("01", 4);
        assertEquals("0000", pg.getNextPassword());
        assertEquals("0001", pg.getNextPassword());
        assertEquals("0010", pg.getNextPassword());
        assertEquals("0011", pg.getNextPassword());
        assertEquals("0100", pg.getNextPassword());
        assertEquals("0101", pg.getNextPassword());
        assertEquals("0110", pg.getNextPassword());
        assertEquals("0111", pg.getNextPassword());
    }
}
