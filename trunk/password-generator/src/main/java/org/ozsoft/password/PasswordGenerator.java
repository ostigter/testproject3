package org.ozsoft.password;

import java.util.Arrays;

public class PasswordGenerator {
    
    private final char[] charset;

    private final int maxLength;
    
    private int length;
    
    private char[] password;
    
    public static void main(String[] args) {
        PasswordGenerator pg = new PasswordGenerator("abcdefghijklmnopqrstuvwxyz", 5, 12);
//        PasswordGenerator pg = new PasswordGenerator("0123456789", 4, 4);
        pg.generate();
    }

    public PasswordGenerator(String chars, int minLength, int maxLength) {
        if (chars == null || chars.isEmpty()) {
            throw new IllegalArgumentException("Null or empty chars");
        }
        if (minLength < 1) {
            throw new IllegalArgumentException("minLength must be equal to or greater than 1");
        }
        if (maxLength < minLength) {
            throw new IllegalArgumentException("maxLength must be equal to or greater than minLength");
        }
        this.charset = chars.toCharArray();
        this.length = minLength;
        this.maxLength = maxLength;
    }
    
    public void generate() {
        password = new char[length];
        Arrays.fill(password, charset[0]);
        while (length <= maxLength) {
            String attempt = toString();
            System.out.println(attempt);
            increment();
        }
    }

    public void increment() {
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

    public String toString() {
        return String.valueOf(password);
    }
    
}
