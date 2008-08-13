package trmi;


import java.rmi.RMISecurityManager;
import java.security.Permission;


public class RemoteSecurityManager extends RMISecurityManager {


    public void checkConnect(String host, int port) {}

    public void checkAccept(String host, int port) {}

    public void checkExec(String s) {}

    public void checkPermission(Permission perm){}

    public void checkPermission(Permission perm, Object context){}


    public static void setup() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RemoteSecurityManager());
        }

    }


}
