package org.ozsoft.java3d;

/**
 * Test driver.
 * 
 * @author Oscar Stigter
 */
public class Main {

    /**
     * The application's entry point.
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(String[] args) {
        Airplane airplane = new Airplane();
        airplane.setPitch(0.0);
        airplane.setSpeed(0.0);

        airplane.setHeading(45.0);
        airplane.setPitch(45.0);
        airplane.setSpeed(10.0);
        airplane.updatePosition();
        System.out.println(airplane.getLocation());
      }

}
