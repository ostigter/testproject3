package com.example.calculator;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the Calculator service.
 * 
 * @author Oscar Stigter
 */
public interface Calculator extends Remote {

    /** The service ID. */
    String SERVICE_ID = "Calculator";

    /**
     * Adds two integers.
     * 
     * @param i
     *            The first integer.
     * @param j
     *            The second integer.
     * 
     * @return The sum of the two integers.
     * 
     * @throws RemoteException
     *             In case of a connection error.
     */
    int add(int i, int j) throws RemoteException;

}
