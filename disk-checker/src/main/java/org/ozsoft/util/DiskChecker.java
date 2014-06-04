package org.ozsoft.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class DiskChecker {

    private static final int BUFFER_SIZE = 32 * 1024; // 32 kB
    private static final long KB = 1024L;
    private static final long MB = 1024L * KB;
    private static final long GB = 1024L * MB;

    private static final byte[] buffer = new byte[BUFFER_SIZE];

    private static int errorCount = 0;

    public static void main(String[] args) {
        File dir = null;
        if (args.length == 0) {
            // Current working directory.
            dir = new File(System.getProperty("user.dir"));
        } else {
            // Specific directory.
            dir = new File(args[0]);
            if (!dir.isDirectory()) {
                System.err.format("ERROR: Directory not found: '%s'\n", dir);
            }
        }

        checkDirectory(dir);

        System.out.format("\nCheck complete -- %d errors found.\n\n", errorCount);
    }

    private static void checkDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                checkDirectory(file);
            } else {
                checkFile(file);
            }
        }
    }

    private static void checkFile(File file) {
        String path = file.getAbsolutePath();
        long size = file.length();
        System.out.format("%s (%s)", path, sizeToString(size));
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            while (is.read(buffer) > 0) {
                // Do nothing.
            }
            System.out.println(" [OK]");
        } catch (IOException e) {
            System.out.println(" [ERROR]");
            System.err.println(e);
            errorCount++;
        }
    }

    private static String sizeToString(long size) {
        if (size < KB) {
            return String.format("%d bytes", size);
        } else if (size < MB) {
            return String.format("%d kB", size / KB);
        } else if (size < GB) {
            return String.format("%s MB", formatNumber((double) size / MB));
        } else {
            return String.format("%s GB", formatNumber((double) size / GB));
        }
    }

    private static String formatNumber(double number) {
        double a = Math.abs(number);
        if (a < 10) {
            return String.format(Locale.US, "%.2f", number);
        } else if (a < 100) {
            return String.format(Locale.US, "%.1f", number);
        } else {
            return String.format(Locale.US, "%.0f", number);
        }
    }
}
