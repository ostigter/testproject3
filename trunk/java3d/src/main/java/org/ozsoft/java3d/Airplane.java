package org.ozsoft.java3d;

import javax.vecmath.Vector3d;

/**
 * An airplane.
 * 
 * @author Oscar Stigter
 */
public class Airplane {
    
    private final static double RADIANS = Math.PI / 180;
    
    /** The 3D location in the world. */
    private final Location location = new Location();
    
    /** The heading in degrees. */
    private double heading = 0.0;
    
    /** The pitch in degrees. */
    private double pitch = 0.0;
    
    /** The speed in meters per second. */
    private double speed = 0.0;
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location.x = location.x;
        this.location.y = location.y;
        this.location.z = location.z;
    }
    
    public double getPitch() {
        return pitch;
    }
    
    public double getHeading() {
        return heading;
    }
    
    public void setHeading(double heading) {
        this.heading = heading;
    }
    
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**
     * Updates the position.
     */
    public void updatePosition() {
        // The heading angle in radians (converted from polar north to radian zero). 
        double ah = (heading - 90.0) * RADIANS;
        
        // The pitch angle in radians. 
        double ap = pitch * RADIANS;

        // The movement on the X-scale. 
        double dx = Math.cos(ah);
        
        // The movement on the Y-scale.
        double dy = -1.0 * Math.sin(ah);
        
        // The movement on the Z-scale.
        double dz = Math.sin(ap);
        
//        // The distance moved in the XY plane. 
//        double dXY = Math.sqrt((dx * dx) + (dy * dy)); 
//
//        // The distance moved in the XZ plane. 
//        double dXZ = Math.sqrt((dx * dx) + (dz * dz)); 
//
//        // The distance moved in the YZ plane. 
//        double dYZ = Math.sqrt((dy * dy) + (dz * dz));
        
        Vector3d v = new Vector3d(dx, dy, dz);
        v.normalize();

        // Update position.
        location.x += v.x * speed;
        location.y += v.y * speed;
        location.z += v.z * speed;
    }

}
