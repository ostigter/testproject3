package org.ozsoft.azureus;

import java.util.Locale;

public class TorrentUtils {

    /** Bytes per kilobyte (kiB). */
    public static final int KILOBYTE = 1024;

    /** Bytes per megabyte (MiB). */
    public static final int MEGABYTE = 1024 * KILOBYTE;

    private TorrentUtils() {
        // Empty implementation.
    }

    public static String throughputToString(long speed) {
        if (speed > MEGABYTE) {
            return String.format(Locale.US, "%.2f MB/s", speed / (double) MEGABYTE);
        } else {
            return String.format(Locale.US, "%d kB/s", speed / KILOBYTE);
        }
    }
}
