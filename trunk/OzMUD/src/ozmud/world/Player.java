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
	
	/** Whether to handle incoming commands. */
	private boolean isHandlingCommands = false;
	

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
		System.out.println(name + " has logged out");
	}
	
	
	public void setHandlingCommands(boolean isHandlingCommands) {
		this.isHandlingCommands = isHandlingCommands;
	}
	
	
	public void start() {
		// Enter the starting room.
		setRoom(world.getRoom(0));
		room.addCreature(this);
		String message = "${sender} appear${s} out of thin air.\n\r";
		room.broadcast(message, this, null);
		
		// Listen for and handle incoming commands.
		connection.addListener(this);
		setHandlingCommands(true);
		sendPrompt();
	}
	
	
	/**
	 * Processes an incoming command.
	 * 
	 * @param  message  the message
	 */
	public void handleCommand(String command) {
		if (connection == null) {
			throw new IllegalStateException("Connection closed");
		}
		
		world.getCommandInterpreter().executeCommand(this, command);
		sendPrompt();
	}


	/**
	 * Sends the prompt.
	 */
	public void sendPrompt() {
		send("> ");
	}
	
	
	/**
	 * Sends a message to the client.
	 */
	public void send(String message) {
		if (connection != null &&
				connection.isOpen() && connectionState == ONLINE) {
			connection.send(message);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ozmud.server.ConnectionListener#messageReceived(java.lang.String)
	 */
	public void messageReceived(String message) {
		if (isHandlingCommands) {
			handleCommand(message);
		}
	}


}
