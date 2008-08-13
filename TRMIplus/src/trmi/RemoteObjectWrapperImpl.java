package trmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * Default implementation of <code>RemoteObjectWrapper</code>.
 * 
 * @author Guy Gur-Ari
 * 
 * 23-04-2008 COFF  Added support for inherited interfaces.
 */
public class RemoteObjectWrapperImpl
		extends UnicastRemoteObject implements RemoteObjectWrapper {

	private static final long serialVersionUID = 1L;

	/**
     * Constructs the object.
     *
     * @param wrappedObject	The locally wrapped object, on which methods are
     * invoked.
     *
     * @param exposedInterfaces The interface this object wants to expose
     * remotely.
     *
     * @exception RemoteException Propagated from superclass.
     *
     * @exception IllegalArgumentException If <code>exposedInterfaces</code>
     * aren't implemented by <code>wrappedObject</code>.
     */
	@SuppressWarnings("unchecked")
    public RemoteObjectWrapperImpl(Object wrappedObject,
        Class[] exposedInterfaces) throws RemoteException,
        IllegalArgumentException {
        this.wrappedObject = wrappedObject;

        // Verify the exposed interfaces
        for (int i = 0; i < exposedInterfaces.length; i++) {
            Class iface = exposedInterfaces[i];

            // Make sure it's really an interface
            if (!iface.isInterface()) {
                throw new IllegalArgumentException("Supposed interface '"
                    + iface + "' isn't an interface");
            }

            // Make sure the object implements it
            Class objClass = wrappedObject.getClass();
            if (!implementsInterface(objClass, iface)) {
                throw new IllegalArgumentException("Exposed interface \'" +
                    iface + "\' isn\'t implemented by wrapped object \'" +
                    wrappedObject + "\'");
            }
        }

        // Exposed interfaces were verified; keep them
        this.exposedInterfaces = exposedInterfaces.clone();
    }


    /**
     * See {@link RemoteObjectWrapper#exposedInterfaces()}.
     */
	@SuppressWarnings("unchecked")
    public Class[] exposedInterfaces() {
        return exposedInterfaces.clone();
    }

    /**
     * See {@link RemoteObjectWrapper#invokeRemote(String, Class[], boolean[],
     * Object[])}.
     */
	@SuppressWarnings("unchecked")
    public Object invokeRemote(String methodName, Class[] paramTypes,
        boolean[] primitiveparams, Object[] params)
        throws RemoteObjectWrapperException, RemoteException,
        InvocationTargetException {
        // Get the method to be invoked
        Method method = findMethod(exposedInterfaces, methodName, paramTypes,
            primitiveparams);

        try {
            // Simply invoke the method on the wrapped object, and return the
            // result (creating a stub if necessary). If an exception is
            // thrown, it is propagated.
            Object result = method.invoke(wrappedObject, params);
            result = Naming.getParameterStubIfNeeded(result);
            return result;
        } catch (IllegalAccessException e) {
            // This is thrown by Method.invoke() itself
            e.printStackTrace();

            // The method cannot be invoked
            throw new RemoteObjectWrapperException("Method '" + methodName
                + "' could not be invoked: " + e, e);
        } catch (IllegalArgumentException e) {
            // This too is thrown by Method.invoke() itself
            e.printStackTrace();

            // The method cannot be invoked
            throw new RemoteObjectWrapperException("Method '" + methodName
                + "' could not be invoked: " + e, e);
        }
    }

    /**
     * Finds the requested method to be invoked and returns it.
     *
     * @param ifaces 	The interfaces in which the method may appear.
     *
     * @param methodName 	The name of the requested method.
     *
     * @param paramTypes 	The parameter types of the requested method, as
     * passed to <code>invokeRemote</code>.
     *
     * @param primitiveparams 	Which parameters are primitive, as passed to
     * <code>invokeRemote</code>.
     *
     * @exception RemoteObjectWrapperException If the method could not be found
     * in the given interfaces.
     */
	@SuppressWarnings("unchecked")
    private Method findMethod(Class[] ifaces, String name, Class[] paramTypes,
        boolean[] primitiveparams) throws RemoteObjectWrapperException {
        // Convert the primitive method parameters as needed.
        // It sure is ugly; don't know of a better way to do it.
        for (int i = 0; i < paramTypes.length; i++) {
            if (primitiveparams[i]) {
                if (paramTypes[i].equals(Boolean.class)) {
                    paramTypes[i] = Boolean.TYPE;
                } else if (paramTypes[i].equals(Character.class)) {
                    paramTypes[i] = Character.TYPE;
                } else if (paramTypes[i].equals(Byte.class)) {
                    paramTypes[i] = Byte.TYPE;
                } else if (paramTypes[i].equals(Short.class)) {
                    paramTypes[i] = Short.TYPE;
                } else if (paramTypes[i].equals(Integer.class)) {
                    paramTypes[i] = Integer.TYPE;
                } else if (paramTypes[i].equals(Long.class)) {
                    paramTypes[i] = Long.TYPE;
                } else if (paramTypes[i].equals(Float.class)) {
                    paramTypes[i] = Float.TYPE;
                } else if (paramTypes[i].equals(Double.class)) {
                    paramTypes[i] = Double.TYPE;
                } else {
                    throw new RemoteObjectWrapperException(
                        "Unrecognized primitive parameter type: " +
                        paramTypes[i]);
                }
            }
        }

        // Iterate over the given interfaces and find the method
        for (int i = 0; i < ifaces.length; i++) {
            try {
                return ifaces[i].getMethod(name, paramTypes);
            } catch (NoSuchMethodException e) {
            }
        }
        // Method not found
        throw new RemoteObjectWrapperException("Unable to find method " + name);
    }

    /** Tests if a class or its superclasses implements a given interface.
     * 
     * @param cl    The class to check.
     * @param iface The interface that should be implemented.
     * @return      True if the interface is implemented.
     */
	@SuppressWarnings("unchecked")
    private boolean implementsInterface(Class cl, Class iface) {
        if (cl == null) return false;
        Class[] clInterfaces = cl.getInterfaces();
        for (int j = 0; j < clInterfaces.length; j++) {
            if (iface.isAssignableFrom(clInterfaces[j])) return true;
        }
        return implementsInterface(cl.getSuperclass(), iface);
    }

    /** The locally wrapped object, on which methods are invoked */
    private Object wrappedObject;

    /** The interfaces this object exposes remotely */
	@SuppressWarnings("unchecked")
    private Class[] exposedInterfaces;

}
