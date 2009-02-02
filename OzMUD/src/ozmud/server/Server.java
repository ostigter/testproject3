package ozmud.server;


import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import ozmud.world.World;


/**
 * OzMUD server.
 */
public class Server {


	/** Local port clients can connect to. */
	private static final int PORT = 5000;

	/** The world. */
	private final World world;

	/** TCP/IP socket clients can connect to. */
	private ServerSocket serverSocket;
	
	/** Indicates whether the server is running or not. */
	protected boolean isRunning = false;


	/**
	 * Constructor.
	 */
	public Server() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.err.println("*** Error: Could not create server socket.");
			System.exit(1);
		}
		
		world = new World();
		
		System.out.println("Server initialized.");
	}


	/**
	 * Starts the server.
	 * 
	 * Contains the main loop for the server which handles incoming client
	 * requests.
	 */
	public void start() {
		isRunning = true;
		System.out.println("Server started.");
		System.out.println("Waiting for clients to connect on port " + PORT
				+ "...");
		while (isRunning) {
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected from "
						+ clientSocket.getInetAddress() + ".");
				TelnetConnection connection =
						new TelnetConnection(clientSocket);
				Portal portal = new Portal(world, connection);
				new Thread(portal).start();
			} catch (InterruptedIOException e) {
				System.err.println(
						"*** ERROR: Interrupted: " + e.getMessage());
			} catch (IOException e) {
				System.err.println(
						"*** Error: Could not connect client: "
						+ e.getMessage());
			}
		}
	}


	/**
	 * Shuts down the server.
	 */
	public void shutdown() {
		System.out.println("Shutting down server...");
		try {
			serverSocket.close();
			System.out.println("Server shut down.");
		} catch (Exception e) {
			System.err.println(
					"*** Error: Could not close server socket properly: "
					+ e.getMessage());
		}
	}


}
