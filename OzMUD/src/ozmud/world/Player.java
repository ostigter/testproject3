package ozmud.world;


import ozmud.server.Connection;
import ozmud.server.ConnectionListener;


/**
 * A human-controlled player character.
 * 
 * @author Oscar Stigter
 */
public class Player extends Creature implements ConnectionListener {


	/** Connection states. */
	public static final int OFFLINE = 0;
	public static final int ONLINE = 1;
	public static final int LINKDEAD = 2;
	
	/** Players's password. */
	private String password;

	/** Connection. */
	private Connection connection;

	/** Connection state. */
	private int connectionState = OFFLINE;
	

	public Player(String name, Gender gender, String password, World world) {
		super(name, gender, null, world);
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

	
	/**
	 * Connect to an open connection.
	 * 
	 * @param  connection  the connection
	 */
	public void connect(Connection connection) {
		this.connection = connection;
		connectionState = ONLINE;
	}


	/**
	 * Disconnects.
	 */
	public void disconnect() {
		if (connection != null) {
			connection.close();
			connection = null;
		}
		connectionState = OFFLINE;
		System.out.println(getName() + " has disconnected.");
	}
	
	
	/**
	 * Processes an incoming message.
	 * 
	 * @param  message  the message
	 */
	public void processMessage(String message) {
		if (connection == null) {
			throw new IllegalStateException("Connection closed");
		}
		
		connection.send(message);
	}


	public void messageReceived(String message) {
		processMessage(message);
	}


	public void start() {
		setRoom(world.getRoom(0));
		connection.addListener(this);
	}


}
