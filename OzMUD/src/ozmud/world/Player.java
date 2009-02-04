package ozmud.world;


import java.io.IOException;

import ozmud.commands.CommandInterpreter;
import ozmud.server.Connection;
import ozmud.server.ConnectionListener;


/**
 * A human-controlled player character.
 * 
 * @author Oscar Stigter
 */
public class Player extends Creature implements ConnectionListener {


	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The prompt. */
	private static final String PROMPT = "${GRAY}> ";
	
	/** The players's password. */
	private String password;

	/** The connection to the player's client. */
	private Connection connection;
	
	/** Command interpreter. */
	private CommandInterpreter commandInterpreter;

	/** Connection state. */
	private ConnectionState connectionState = ConnectionState.OFFLINE;
	

	/**
	 * Default constructor.
	 */
	public Player() {
		commandInterpreter = World.getInstance().getCommandInterpreter();
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
	public ConnectionState getConnectionState() {
		return connectionState;
	}

	
	/**
	 * Connect to an open connection.
	 * 
	 * @param  connection  the connection
	 */
	public void connect(Connection connection) {
		this.connection = connection;
		connectionState = ConnectionState.ONLINE;
	}


	/**
	 * Disconnects and ends the session.
	 */
	public void disconnect() {
		if (connection != null) {
			connection.close();
			connection = null;
		}
		connectionState = ConnectionState.OFFLINE;
		System.out.println(getShortName() + " has logged out");
	}
	
	
	/**
	 * Starts the session.
	 */
	public void start() {
		// Enter the starting room.
		moveTo(World.getInstance().getStartingRoom());

		// Handle incoming commands.
		connection.addListener(this);
		connection.setReceiving(true);
		// TODO: Configure ANSI color support.
		connection.setColorsEnabled(true);

		// Announce presence. 
		String message = "${CYAN}${sender} appear${s} out of thin air.\n\r";
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
				connection.isOpen() &&
					connectionState == ConnectionState.ONLINE) {
			try {
				connection.send(message);
			} catch (IOException e) {
				System.err.println(
						"*** ERROR: I/O error while sending message: "
						+ e.getMessage());
			}
		}
	}
	
	
	/**
	 * Processes an incoming command.
	 * 
	 * @param  message  The message
	 */
	public void handleCommand(String command) {
		if (connection == null) {
			throw new IllegalStateException("Connection closed");
		}
		
		commandInterpreter.executeCommand(this, command);
	}


	/*
	 * (non-Javadoc)
	 * @see ozmud.server.ConnectionListener#messageReceived(java.lang.String)
	 */
	public void messageReceived(String message) {
		handleCommand(message);
		sendPrompt();
	}


}
