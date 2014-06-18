package org.ozsoft.password;

import java.util.Arrays;

/**
 * Brute-force password generator based on a fixed set of characters and a minimum length.
 * 
 * @author Oscar Stigter
 */
public class PasswordGenerator {

    private final char[] charset;

    private int length;

    private char[] password;

    public PasswordGenerator(String chars, int minLength) {
        if (chars == null || chars.isEmpty()) {
            throw new IllegalArgumentException("Null or empty chars");
        }
        if (minLength < 1) {
            throw new IllegalArgumentException("minLength must be equal to or greater than 1");
        }
        this.charset = chars.toCharArray();
        this.length = minLength;

        password = new char[length];
        Arrays.fill(password, charset[0]);
    }

    public String getNextPassword() {
        String nextPassword = String.valueOf(password);
        increment();
        return nextPassword;
    }

    private void increment() {
        int index = length - 1;
        while (index >= 0) {
            if (password[index] == charset[charset.length - 1]) {
                if (index == 0) {
                    length++;
                    password = new char[length];
                    Arrays.fill(password, charset[0]);
                    break;
                } else {
                    password[index] = charset[0];
                    index--;
                }
            } else {
                password[index] = charset[Arrays.binarySearch(charset, password[index]) + 1];
                break;
            }
        }
    }
}
