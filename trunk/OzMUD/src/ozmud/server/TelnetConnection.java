package ozmud.server;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import ozmud.Util;


/**
 * Telnet connection with ANSI color support.
 */
public class TelnetConnection extends AbstractConnection {


	/** ANSI color codes. */
	private static final String[][] COLORS = new String[][] {
			{ "${BLACK}",    "\033[0;30m" },
			{ "${GRAY}",     "\033[0;37m" },
			{ "${WHITE}",    "\033[1;37m" },
			{ "${YELLOW}",   "\033[1;33m" },
			{ "${RED}",      "\033[1;31m" },
			{ "${BLUE}",     "\033[1;34m" },
			{ "${GREEN}",    "\033[1;32m" },
			{ "${CYAN}",     "\033[1;36m" },
			{ "${MAGENTA}",  "\033[1;35m" },
			{ "${BROWN}",    "\033[0;33m" },
			{ "${DGRAY}",    "\033[1;30m" },
			{ "${DRED}",     "\033[0;31m" },
			{ "${DGREEN}",   "\033[0;32m" },
			{ "${DBLUE}",    "\033[0;34m" },
			{ "${DMAGENTA}", "\033[0;35m" },
			{ "${DCYAN}",    "\033[0;36m" } };

	/** Buffer size in bytes. */
	private static final int BUFFER_SIZE = 8192;
	
	/** Buffer for incoming data. */
	private final byte[] buffer = new byte[BUFFER_SIZE];

	/** TCP/IP socket. */
	private Socket socket;

	/** Input stream for incoming data. */
	private BufferedInputStream input;

	/** Output stream for outgoing data. */
	private BufferedOutputStream output;
	
	/** Whether ANSI colors are enabled. */
	private boolean colorsEnabled = false;
	

	/**
	 * Constructor.
	 * 
	 * @param  socket  an open TCP/IP socket
	 * 
	 * @throws  IOException  if the connection could not be opened
	 */
	public TelnetConnection(Socket socket) throws IOException {
		if (socket == null) {
			throw new IllegalArgumentException("Null socket");
		}

		if (!socket.isConnected()) {
			throw new IllegalStateException("Socket not open");
		}
		
		try {
			this.socket = socket;
			input = new BufferedInputStream(socket.getInputStream());
			output = new BufferedOutputStream(socket.getOutputStream());
			super.open();
		} catch (IOException e) {
			throw new IOException(
					"Could not open telnet connection: " + e.getMessage());
		}
	}

	/**
	 * Sends a message.
	 * The messages may include ANSI color codes. 
	 * 
	 * @param  s  message to send
	 */
	public void send(String message) {
		if (message == null) {
			throw new IllegalArgumentException("Null message");
		}
		
		if (!isOpen()) {
			throw new IllegalStateException("Connection closed");
		}
		
		message = parseColors(message);
		
		try {
			output.write(message.getBytes(), 0, message.getBytes().length);
			output.flush();
		} catch (IOException e) {
			System.err.println("I/O error while sending data: " + e.getMessage());
		}
	}


	/**
	 * Returns true if data is available.
	 * 
	 * @return true if data is available
	 */
	public boolean dataAvailable() {
		if (!isOpen()) {
			throw new IllegalStateException("Connection closed");
		}
		
		boolean dataAvailable = false;
		try {
			dataAvailable = (input.available() != 0);
		} catch (IOException e) {
			System.err.println(
					"I/O error while probing for available data: "
					+ e.getMessage());
		}
		return dataAvailable;
	}


	/**
	 * Close the connection.
	 */
	public void close() {
		if (!isOpen()) {
			throw new IllegalStateException("Connection already closed");
		}
		
		super.close();
		
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println(
					"Could not properly close connection: " + e.getMessage());
		} finally {
			socket = null;
			input = null;
			output = null;
		}
	}


	/**
	 * Receives an incoming message, ending with a CR or LF character.
	 * Blocks until a message has been completely received.
	 * 
	 * @return  the received message
	 */
	public String receive() {
		if (!isOpen()) {
			throw new IllegalStateException("Connection closed");
		}
		
		// TODO: Receive any and all data immediately; no filtering or blocking.
		StringBuilder s = new StringBuilder();
		int b = -1;
		boolean done = false;
		try {
			while (!done) {
				int available = input.available();
				if (available > 0) {
					int read = input.read(buffer);
					for (int i = 0; i < read; i++) {
						b = buffer[i];
						if (b == '\r' || b == '\n') {
							done = true;
						} else {
							s.append((char) b);
						}
					}
				}
			}
		} catch (IOException e) {
			System.err.println(
					"I/O error while receiving data: " + e.getMessage());
		}
		return s.toString();
	}

	
	/**
	 * Parses a string for color codes and replaces them with the corresponding
	 * ANSI code if color support is enabled, otherwise removing them.
	 * 
	 * @param s  string to parse
	 * @return the parsed string
	 */
	private String parseColors(String s) {
		for (String[] color : COLORS) {
			s = Util.replace(s, color[0], colorsEnabled ? color[1] : null);
		}
		return s;
	}


}
