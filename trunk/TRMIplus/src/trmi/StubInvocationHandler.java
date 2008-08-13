package trmi;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.MarshalException;
import java.rmi.RemoteException;

/** TRMI+
 * Accepts invocations from the client and forwards them to the remote {@link
 * RemoteObjectWrapper}, where they are executed.<p>
 *
 * TRMI+ : equals(), hashCode() and toString() handling added.
 * 
 * @author Guy Gur-Ari
 */
public class StubInvocationHandler implements InvocationHandler, Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * Constructs the object.
     * 
     * @param wrapper The remote object's wrapper. Invocation requests are
     * delegated to it.
     * 
     * @param recoveryStrategy Strategy for recovering from remote errors.
     */
    public StubInvocationHandler(
            RemoteObjectWrapper wrapper,
            RemoteExceptionRecoveryStrategy recoveryStrategy) {
        this.wrapper = wrapper;
        this.recoveryStrategy = recoveryStrategy;
    }

    /** TRMI+
     * Sends the invocation request to the remote object wrapper, and returns
     * the response. If the remote invocation fails, calls the recovery
     * strategy and tries again.
     *
     * @exception RuntimeException If the recovery strategy fails.
     */
    public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return invokeObjectMethod(proxy, method, args);
        } else {
            return invokeRemoteMethod(proxy, method, args);
        }
    }
    
    /** TRMI+
     * Handles java.lang.Object methods by forwarding them to the wrapper.
     **/
    private Object invokeObjectMethod(
    		Object proxy, Method method, Object[] args) {
        String name = method.getName();

        if (name.equals("hashCode")) {
            int code = wrapper.hashCode();
            // System.out.println("Hashcode: " + code);
            return new Integer(code);

        } else if (name.equals("equals")) {
            Object other = args[0];
            if (other instanceof Proxy) {
                Proxy p = (Proxy) other;
                InvocationHandler h = Proxy.getInvocationHandler(p);
                if (h instanceof StubInvocationHandler) {
                    StubInvocationHandler sih = (StubInvocationHandler) h;
                    other = sih.wrapper;
                }
            }
            boolean b = wrapper.equals(other);
            // System.out.println("Equals: " + b);
            return Boolean.valueOf(b);

        } else if (name.equals("toString")) {
            return wrapper.toString();

        } else {
            throw new IllegalArgumentException(
            "unexpected Object method: " + method);
        }
    }

    /**
     * Handles remote methods.
     **/
	@SuppressWarnings("unchecked")
    private Object invokeRemoteMethod(
    		Object proxy, Method method, Object[] params) 
			throws Throwable, RuntimeException {
        // Loops while we fail to make the call
        while (true) {

            try {

                // Handle the primitives issue
                Class[] convertedTypes = method.getParameterTypes();
                boolean[] primitiveTypes = new boolean[convertedTypes.length];

                convertPrimitiveParamTypes(
                        convertedTypes,
                        primitiveTypes);

                // Convert non-Serializable parameters to TRMI stubs
                convertNonSerializableParams(params);

                // Tell the wrapper to invoke the method
                Object response = wrapper.invokeRemote(
                        method.getName(),
                        convertedTypes,
                        primitiveTypes,
                        params);

                return response;
            } catch (RemoteObjectWrapperException e) {
                // This indicates a bug in this suite
                throw new RuntimeException(
                        "Internal trmi error while invoking "
                        + method + ": " + e);
            } catch (InvocationTargetException e) {
                // The invoked method raised an exception
                throw e.getCause();
            } catch (MarshalException e) {
                // We can't recover from this type of exception
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                wrapper = recoveryStrategy.recoverFromRemoteException(
                        wrapper, e);

                // If a RuntimeException is not thrown by the strategy, we will
                // loop and try again
            }
        }
    }

    /**
     * Converts primitive types to their object-oriented counterparts.
     * 
     * @param types The types to convert. The primitive types are converted
     * in-place.
     * 
     * @param primitiveTypes An out parameter that records which parameters in
     * <code>types</code> are primitives and which aren't.
     */
	@SuppressWarnings("unchecked")
    private void convertPrimitiveParamTypes(
            Class[] types, 
            boolean[] primitiveTypes) throws IllegalArgumentException {

        for (int i = 0; i < types.length; i++) {
            if (types[i].isPrimitive()) {
                primitiveTypes[i] = true;

                if (types[i].equals(Boolean.TYPE)) {
                    types[i] = Boolean.class;
                } else if (types[i].equals(Character.TYPE)) {
                    types[i] = Character.class;
                } else if (types[i].equals(Byte.TYPE)) {
                    types[i] = Byte.class;
                } else if (types[i].equals(Short.TYPE)) {
                    types[i] = Short.class;
                } else if (types[i].equals(Integer.TYPE)) {
                    types[i] = Integer.class;
                } else if (types[i].equals(Long.TYPE)) {
                    types[i] = Long.class;
                } else if (types[i].equals(Float.TYPE)) {
                    types[i] = Float.class;
                } else if (types[i].equals(Double.TYPE)) {
                    types[i] = Double.class;
                } else {
                    throw new IllegalArgumentException(
                            "Unrecognized primitive parameter type: " 
                            + types[i]);
                }
            } else {
                primitiveTypes[i] = false;
            }
        }
    }

    /**
     * Replaces method parameters with TRMI stubs as needed. See {@link
     * trmi.Naming#getParameterStubIfNeeded(Object, Class)} for more details.
     */
    private void convertNonSerializableParams(Object[] params) {
        // Happens when the method is parameter-less
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            params[i] = Naming.getParameterStubIfNeeded(params[i]);
        }
    }

    /** The remote wrapper around the remote object */
    private RemoteObjectWrapper wrapper;
    
    /** Remote error recovery strategy */
    private RemoteExceptionRecoveryStrategy recoveryStrategy;

}
