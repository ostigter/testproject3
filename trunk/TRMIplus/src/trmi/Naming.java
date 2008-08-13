package trmi;

import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



/** TRMI+
 * A replacement to {@link java.rmi.Naming} that handles the creation of {@link 
 * RemoteObjectWrapper}s for bound objects, and of proxies using {@link
 * StubInvocationHandler} for lookups.<p>
 * 
 * Note that, while the methods have the same semantics as those in
 * <code>java.rmi.Naming </code>, the parameters and return types of
 * <code>bind</code>, <code>rebind</code>, and <code>lookup</code> were changed
 * because they can now work with any <code>Object</code>, not only with
 * <code>Remote</code> ones.<p>
 *
 * <h2>Recovery Strategy Factory</h2>
 * The user can control the creation of {@link
 * RemoteExceptionRecoveryStrategy}s for objects that are looked up using
 * {@link #setRecoveryStrategyFactory(RemoteExceptionRecoveryStrategyFactory)}
 * and {@link #getRecoveryStrategyFactory()}. Recovery strategies for looked
 * up objects are created by the factory.
 *
 * @see RemoteExceptionRecoveryStrategy
 * @see RemoteExceptionRecoveryStrategyFactory
 * @author Guy Gur-Ari
 */
public class Naming {

    /* --- Naming Interface --- */

    /** TRMI+
     * Binds the given object for remote use, exposing the given interfaces.
     * Interfaces that the object implements but that do not appear on the
     * given array are not exposed remotely.<p>
     * 
     * The object that is actually mapped is an instance of {@link
     * RemoteObjectWrapper}, which wraps <code>obj</code>. The binding is
     * performed using {@link java.rmi.Naming}.
     * 
     * Performs a REbind at the RMI Registry.
     * 
     * @param name The name under which to bind the object.
     * @param obj The object to bind.
     * @param ifaces The interfaces which <code>obj</code> implements, and
     * should be exposed remotely.
     * 
     * @exception IllegalArgumentException If <code>obj</code> doesn't
     * implement the given interfaces.
     * @exception AlreadyBoundException If name is already bound.
     * @exception MalformedURLException If the name is not an appropriately
     * formatted URL.
     * @exception RemoteException If the registry could not be contacted.
     * @exception AccessException If this operation is not permitted (if
     * originating from a non-local host, for example).
     */
    @SuppressWarnings("unchecked")
    public static void bind(String name, Object obj, Class[] ifaces) 
    throws IllegalArgumentException, AlreadyBoundException, 
        MalformedURLException, RemoteException {
        Object currentObj = namedObjects.get(name);
        if (currentObj != null) {
            throw new AlreadyBoundException(
                "Name '" + name + "' ALREADY BOUND to " + currentObj);
        }
        RemoteObjectWrapperImpl currentWrapper = objectWrappers.get(obj);
        if (currentWrapper != null) {
            throw new AlreadyBoundException(
                "Object '" + obj + "' ALREADY BOUND under another name.");
        }

        RemoteObjectWrapperImpl wrapper = 
            new RemoteObjectWrapperImpl(obj, ifaces);
        java.rmi.Naming.rebind(name, wrapper);  // Note RMI REbind.
        namedObjects.put(name, obj);
        objectWrappers.put(obj, wrapper);
    }

    /** TRMI+
     * Rebinds the specified name to a new remote object. Any existing
     * binding for the name is replaced. See {@link #bind(String, Object,
     * Class[])} for how objects are bound, and for the meaning of
     * parameters and exceptions.
     */
    @SuppressWarnings("unchecked")
    public static void rebind(String name, Object obj, Class[] ifaces) 
    throws ClassCastException, RemoteException, MalformedURLException {
        Object currentObj = namedObjects.get(name);
        if (currentObj != null) {
            try {
                unbind(name);
            } catch (NotBoundException ex) {  // Should not occur.
                throw new RemoteException("Name '" + name + "' NOT BOUND!", ex);
            }
        }
        try {
            bind(name, obj, ifaces);
        } catch (AlreadyBoundException ex) {  // Should not occur.
            throw new RemoteException("Name '" + name + "' ALREADY BOUND!", ex);
        }
    }

    /**
     * Same as {@link java.rmi.Naming#list(String)}.
     */
    public static String[] list(String name) throws RemoteException,
    MalformedURLException {
        return java.rmi.Naming.list(name);
    }

    /**
     * Returns a stub reference for the remote object associated with the
     * specified name. For objects that were bound using transparent-RMI, a
     * stub is returned that supports the exposed interfaces. For objects
     * that were bound using standard RMI (i.e.  those that implement
     * <code>Remote</code>), the standard RMI stub is returned.
     * 
     * @param name	The object's name URL.
     * 
     * @exception NotBoundException If the name is not currently bound.
     * @exception RemoteException If the registry could not be contacted.
     * @exception AccessException If this operation is not permitted.
     * @exception MalformedURLException If the name is not an appropriately
     * formatted URL.
     */
    public static Object lookup(String name) throws NotBoundException,
    RemoteException, AccessException, MalformedURLException {
        Remote remoteObj = java.rmi.Naming.lookup(name);

        // If this is a transparent-RMI object, handle it accordingly
        if (remoteObj instanceof RemoteObjectWrapper) {
            RemoteObjectWrapper wrapper = (RemoteObjectWrapper) remoteObj;
            return createStub(name, wrapper);
        }

        // Otherwise, return the standard RMI stub
        else {
            return remoteObj;
        }
    }

    /** TRMI+
     * Same as {@link java.rmi.Naming#unbind(String)}.
     */
    public static void unbind(String name) throws RemoteException,
    NotBoundException, MalformedURLException {
        Object currentObject = namedObjects.remove(name);
        if (currentObject == null) {
            throw new NotBoundException("Name '" + name + "' NOT BOUND!");
        }
        java.rmi.Naming.unbind(name);
        // System.out.println("Unbound '" + name + "'.");
        RemoteObjectWrapperImpl currentWrapper =
            objectWrappers.remove(currentObject);
        if (currentWrapper == null) {  // Should never occur!
            throw new NotBoundException(
              "Object " + currentObject + " named '" + name + "' NOT WRAPPED!");
        }
        try {
            UnicastRemoteObject.unexportObject(currentWrapper, true);
        }
        catch (NoSuchObjectException ex) {
            throw new NotBoundException(
                "UNEXPORTING Wrapper for '" + name + "' FAILED!");
        }
        // System.out.println("Wrapper for '" + name + "' unexported: " + ok);
    }

    /**
     * Can be used by {@link RemoteExceptionRecoveryStrategy}s to lookup a
     * <code>RemoteObjectWrapper</code> by a given name. The parameters and
     * possible exceptions are similar to those of {@link #lookup(String)}.
     */
    public static RemoteObjectWrapper lookupRemoteObjectWrapper(String name) 
        throws NotBoundException, RemoteException, AccessException, 
    MalformedURLException {
        Remote remoteObj = java.rmi.Naming.lookup(name);

        if (remoteObj instanceof RemoteObjectWrapper) {
            return (RemoteObjectWrapper) remoteObj;
        } else {
            throw new NotBoundException("Object '" + name + "' is bound but "
                    + "is not a RemoteObjectWrapper (probably wrapped by "
                    + "standard RMI");
        }
    }

    /** TRMI+
     * Gets a TRMI stub for the given object, IGNORING the
     * given interfaces. <b>For internal TRMI use only.</b>
     *
     * @param obj The object to wrap with a stub.
     * @param ifaces The interfaces to expose.
     * @return A stub for the given object.
     */
    @SuppressWarnings("unchecked")
    public static Object getStub(Object obj, Class[] ifaces) {
        RemoteObjectWrapper wrapper = objectWrappers.get(obj);
        if (wrapper == null) {
            throw new RemoteRuntimeException(
                "TRMI Object " + obj + " NOT BOUND!");
        }
        try {
            return createStub(null, wrapper);
        } catch (RemoteException e) {
            // An exception should never be thrown because the call to
            // RemoteObjectWrapper is local to the JVM..
            throw new RemoteRuntimeException("Impossible exception thrown: " + e);
        }
    }

    /**
     * Examines the given object's declared type to determine whether a TRMI
     * stub is needed. If it is, uses <code>getStub</code> to create the
     * stub. <b>For internal TRMI use only.</b><p>
     *
     * A stub is created for non-Serializable and non-Remote parameters.
     * When a non-Serializable, non-Remote parameter is passed to a
     * remote TRMI object, TRMI automatically wraps it with a TRMI stub that
     * refers to the original object, so that the object can be used as a
     * callback. All of the parameter object's implemented interfaces are
     * exposed by the stub.
     *
     * @param obj The examined object. Usually a method parameter or return
     * value.
     *
     * @return If a stub is needed and can be created, it is created and
     * returned. Otherwise, <code>obj</code> is returned.
     */
    public static Object getParameterStubIfNeeded(Object obj) {
        if (obj==null) {
            return null;
        }

        boolean needsStub;

        if (Proxy.isProxyClass(obj.getClass())) {
            // Dynamic proxies are handled differently, because they are
            // serializable by default, while their invocation handlers are
            // usually not.

            // Check the invocation handler. If it is serializable, then we
            // will let it pass. Otherwise, create a stub for it.
            Object invocationHandler = Proxy.getInvocationHandler(obj);

            if (invocationHandler instanceof java.io.Serializable) {
                needsStub = false;
            } 
            else {
                needsStub = true;
            }
        } 
        else if (obj instanceof java.io.Serializable) {
            // Serializable copies are handled by RMI
            needsStub = false;
        } 
        else if (obj instanceof Remote) {
            // Skip the class if it can be handled by RMI (this includes TRMI
            // stubs)
            needsStub = false;
        } 
        else {
            needsStub = true;
        }

        if (needsStub) {
            // Create a stub if we can
            if (obj.getClass().getInterfaces().length > 0) {
                return getStub(obj, obj.getClass().getInterfaces());
            } 
        }

        // If we get here the type can't be handled by either RMI or TRMI, so
        // it will probably generate a marshalling exception when it gets to
        // RMI. Good luck anyway...
        return obj;
    }


    /* --- Recovery Strategy Factory --- */

    /**
     * Returns the current recovery strategy factory.
     */
    public static synchronized RemoteExceptionRecoveryStrategyFactory 
    getRecoveryStrategyFactory() {
        return recoveryStrategyFactory;
    }

    /**
     * Sets the recovery strategy factory. See the class documentation for more
     * details.
     *
     * @param factory	The new factory.
     */
    public static synchronized void setRecoveryStrategyFactory(
            RemoteExceptionRecoveryStrategyFactory factory) {
        recoveryStrategyFactory = factory;
    }

    /** TRMI+
     * Returns the map from names to their bound objects.
     */
    public static Map<String, Object> namedObjects() {
        return namedObjects;
    }

    /* --- Private --- */

    /**
     * Creates and returns a local stub for the remote object wrapper.
     * 
     * @param name	  The object name.
     * @param wrapper The remote object wrapper.
     */
    @SuppressWarnings("unchecked")
    private static Object createStub(String name, RemoteObjectWrapper wrapper) 
    throws RemoteException {
        Class[] exposedInterfaces = wrapper.exposedInterfaces();

        // Create the invocation handler
        RemoteExceptionRecoveryStrategy recoveryStrategy;

        if (name != null) {
            recoveryStrategy = recoveryStrategyFactory.getRecoveryStrategy(
                    name, exposedInterfaces);
        } else {
            recoveryStrategy = recoveryStrategyFactory.getRecoveryStrategy(
                    exposedInterfaces);
        }

        StubInvocationHandler invocationHandler = new StubInvocationHandler(
                wrapper, recoveryStrategy);

        // Create a proxy that supports the object's exposed interfaces
        Object stub = Proxy.newProxyInstance(
                trmi.Naming.class.getClassLoader(),
                exposedInterfaces,
                invocationHandler);

        return stub;		
    }

    /** The recovery strategy factory */
    private static RemoteExceptionRecoveryStrategyFactory 
        recoveryStrategyFactory = 
        new DefaultRemoteExceptionRecoveryStrategyFactory();
    
    /** TRMI+ Mapping from names to (bound) objects. */
    private static Map<String, Object> namedObjects =
        Collections.synchronizedMap(new HashMap<String, Object>());
    
    /** TRMI+ Mapping from (bound) objects to their TRMI+ wrappers. */
    private static Map<Object, RemoteObjectWrapperImpl> objectWrappers =
        Collections.synchronizedMap(
            new HashMap<Object, RemoteObjectWrapperImpl>());

}

