package org.ozsoft.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Greeter extends Remote {
    
    String getGreeting(String name) throws RemoteException;

}
