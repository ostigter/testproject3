package org.ozsoft.secs.format;

/**
 * SECS data item.
 * 
 * @param <T>
 *            Underlying value class.
 * 
 * @author Oscar Stigter
 */
public interface Data<T> {

    /**
     * Returns the value.
     * 
     * @return The value.
     */
    T getValue();

    /**
     * Sets the value.
     * 
     * @param value
     *            The value.
     */
    void setValue(T value);

    /**
     * Returns the length. <br />
     * <br />
     * 
     * For items that can contain multiple items, this method returns the number
     * of items it currently contains.
     * 
     * @return The length.
     */
    int length();

    /**
     * Returns a byte array with the data item serialized as bytes according to
     * the SEMI E05 SECS-II standard.
     * 
     * @return The byte array.
     */
    byte[] toByteArray();

    /**
     * Returns the SML text representing this data item.
     * 
     * @return The SML text.
     */
    String toSml();

}
