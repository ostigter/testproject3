package com.example.calculator;

import java.rmi.RMISecurityManager;
import java.security.Permission;

/**
 * RMI security manager that always allows everything.
 * 
 * @author Oscar Stigter
 */
public class DummySecurityManager extends RMISecurityManager {
    
    /**
     * Set this security manager as the system-wide security manager.
     */
    public static void activate() {
        System.setSecurityManager(new DummySecurityManager());
    }

    @Override
    public void checkPermission(Permission perm) {
        // Always allow everything.
    }
    
}
