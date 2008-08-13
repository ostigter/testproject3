package trmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Resides on the server JVM and invokes RMI calls. An instance of this
 * interface exists for each remote object. It accepts invocation requests for
 * the object from the {@link StubInvocationHandler}, and executes them on the
 * locally wrapped object.
 * 
 * @author Guy Gur-Ari
 */
public interface RemoteObjectWrapper extends Remote {

	/**
	 * Returns the interfaces this remote object exposes.
	 */
	@SuppressWarnings("unchecked")
	public Class[] exposedInterfaces() throws RemoteException;
	
	/**
	 * Called by the {@link StubInvocationHandler} on each invocation. Invokes
	 * the given method with the given parameters on the locally wrapped
	 * object.
	 * 
	 * @param methodName The name of the invoked method.
	 * 
	 * @param paramTypes The method's parametertypes. Primitive types are
	 * passed as their object-oriented counterparts (e.g. <code>int</code>
	 * becomes <code>Integer</code>).
	 * 
	 * @param primitiveParams Which of the method's parameters are primitives.
	 * Each item in this array corresponds to the item with the same index in
	 * <code> paramTypes</code>, and determines whether the parameter in that
	 * item is a primitive. This is required because the <code>Class</code>
	 * objects of the primitive types aren't serializable.
	 * 
	 * @param params The method's parameters.
	 * 
	 * @return The object returned from the invoked method.
	 * 
	 * @exception RemoteException	If there was an error remotely invoking
	 * this method.
	 * 
	 * @exception RemoteObjectWrapperException If the object doesn't support
	 * the method to be invoked, either because it doesn't implement it, or
	 * because it is implemented but not exposed remotely (see {@link
	 * #exposedInterfaces()}).
	 * 
	 * @exception InvocationTargetException	If the real object invocation
	 * raised an exception.
	 *
	 * @exception IllegalArgumentException If <code>exposedInterfaces</code>
	 * aren't implemented by <code>wrappedObject</code>.
	 */
	@SuppressWarnings("unchecked")
	public Object invokeRemote(String methodName, 
						 	   Class[] paramTypes, 
						 	   boolean[] primitiveParams, 
						 	   Object[] params)	
	   throws RemoteException, RemoteObjectWrapperException, 
	   InvocationTargetException, IllegalArgumentException;

}
