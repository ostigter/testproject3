package ozmud.world;


import java.io.IOException;

import ozmud.server.Connection;
import ozmud.server.ConnectionListener;


/**
 * A human-controlled player character.
 * 
 * @author Oscar Stigter
 */
public class Player extends Creature implements ConnectionListener {


	/** Offline connection state. */
	public static final int OFFLINE = 0;

	/** Online connection state. */
	public static final int ONLINE = 1;

//	/** Linkdead connection state. */
//	public static final int LINKDEAD = 2;
	
	/** The prompt. */
	private static final String PROMPT = "> ";
	
	/** Players's password. */
	private String password;

	/** Connection. */
	private Connection connection;

	/** Connection state. */
	private int connectionState = OFFLINE;
	
//	/** Whether to handle incoming commands. */
//	private boolean isHandlingCommands = false;
	

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
	 * Disconnects and ends the session.
	 */
	public void disconnect() {
		if (connection != null) {
			connection.close();
			connection = null;
		}
		connectionState = OFFLINE;
		System.out.println(name + " has logged out");
	}
	
	
	/**
	 * Starts the session.
	 */
	public void start() {
		// Enter the starting room.
		moveTo(world.getRoom(0));

		// Handle incoming commands.
		connection.addListener(this);
		connection.setReceiving(true);
		// TODO: Configure ANSI color support.
		connection.setColorsEnabled(true);

		// Announce presence. 
		String message = "${sender} appear${s} out of thin air.\n\r";
		broadcast(message, null);
		
		// Look around.
		handleCommand("look");
	}
	
	
	/**
	 * Sends the prompt.
	 */
	public void sendPrompt() {
		send(PROMPT);
	}
	
	
	/**
	 * Sends a message to the client.
	 */
	public void send(String message) {
		if (connection != null &&
				connection.isOpen() && connectionState == ONLINE) {
			try {
				connection.send(message);
			} catch (IOException e) {
				System.err.println("*** ERROR: I/O error while sending message: " + e.getMessage());
			}
		}
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


	/*
	 * (non-Javadoc)
	 * @see ozmud.server.ConnectionListener#messageReceived(java.lang.String)
	 */
	public void messageReceived(String message) {
		handleCommand(message);
	}


}
