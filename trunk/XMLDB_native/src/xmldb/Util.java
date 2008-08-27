package xmldb;


import java.util.Locale;


public class Util {
    
    private static final Runtime runtime = Runtime.getRuntime();
    

    public static String getMemoryUsage() {
        long totalBytes = runtime.totalMemory();
        long freeBytes = runtime.freeMemory();
        double usedMB  = (totalBytes - freeBytes) / 1048576.0;
        return String.format(Locale.US, "Memory usage:  %.2f MB", usedMB);
    }
    
}
