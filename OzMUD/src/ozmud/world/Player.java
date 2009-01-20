package ozmud.world;

import java.io.IOException;

import ozmud.server.TelnetConnection;

public class Player extends Creature implements Runnable {

	/** Connection states. */
	public static final int OFFLINE = 0;
	public static final int ONLINE = 1;
	public static final int LINKDEAD = 2;

	/** Players's password. */
	private String password;

	/** Telnet connection. */
	private TelnetConnection connection;

	/** Connection state. */
	private int connectionState = OFFLINE;
	

	public Player(String name, Gender gender, String password) {
		super(name, gender, null);
		setPassword(password);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the connection state.
	 * 
	 * @return the connection state
	 */
	public int getConnectionState() {
		return connectionState;
	}

	public void connect(TelnetConnection connection) {
		this.connection = connection;
		connectionState = ONLINE;
	}

	/**
	 * Disconnects the player from the client.
	 */
	public void disconnect() {
		// location.removeCreature(this);

		try {
			connection.disconnect();
		} catch (IOException e) {
			System.err.println("*** Warning: Could not properly disconnect "
					+ getName() + ": " + e);
		}

		connection = null;
		connectionState = OFFLINE;

		System.out.println(getName() + " has disconnected.");
	}
	
	/**
	 * Processes an incoming message.
	 * 
	 * @param  message  the message
	 */
	public void processMessage(String message) {
		// Send message to player's telnet client.
		connection.send(message);
	}

	public void run() {
	}

}
