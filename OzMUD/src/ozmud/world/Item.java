package ozmud.world;


/**
 * An item that can be carried by a creature.
 * 
 * @author Oscar Stigter
 */
public abstract class Item extends MudObject {


    /** The serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** The weight in stones. */
    private double weight;

    /** The value in gold. */
    private int value;

    
    /**
     * Returns the weight in stones.
     * 
     * @return  The weight in stones
     */
    public double getWeight() {
        return weight;
    }
    

    /**
     * Sets the weight in stones.
     * 
     * @param weight  The weight in stones
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }


    /**
     * Returns the value in gold pieces.
     * 
     * @return  The value in gold pieces
     */
    public int getValue() {
        return value;
    }
    

    /**
     * Sets the value in gold pieces.
     * 
     * @param value  The value in gold pieces
     */
    public void setValue(int value) {
        this.value = value;
    }


}
