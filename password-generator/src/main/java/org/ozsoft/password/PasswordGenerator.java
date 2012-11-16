package org.ozsoft.password;

import java.util.Arrays;

public class PasswordGenerator {
    
    private final char[] charset;

    private final int maxLength;
    
    private int length;
    
    private char[] password;
    
    public static void main(String[] args) {
        PasswordGenerator pg = new PasswordGenerator("abcdefghijklmnopqrstuvwxyz", 5, 12);
        pg.generate();
    }

    public PasswordGenerator(String chars, int minLength, int maxLength) {
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
