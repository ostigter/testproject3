package org.ozsoft.java3d;

import java.util.Locale;

/**
 * A 3D location in the world.
 * 
 * @author Oscar Stigter
 */
public class Location {
    
    /** X coordinate (longitude) in meters. */
    public double x = 0.0;
    
    /** Y coordinate (latitude) in meters. */
    public double y = 0.0;
    
    /** Z coordinate (altitude) in meters. */
    public double z = 0.0;
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.US, "(%.2f, %.2f, %.2f)", x, y, z);
    }

}
