package ozmud.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles the communication using the Telnet protocol.
 */
public class TelnetConnection {

	public static final String[][] COLORS = new String[][] {
			{ "{/BLACK}", "\033[0;30m" }, { "{/GRAY}", "\033[0;37m" },
			{ "{/WHITE}", "\033[1;37m" }, { "{/YELLOW}", "\033[1;33m" },
			{ "{/RED}", "\033[1;31m" }, { "{/BLUE}", "\033[1;34m" },
			{ "{/GREEN}", "\033[1;32m" }, { "{/CYAN}", "\033[1;36m" },
			{ "{/MAGENTA}", "\033[1;35m" }, { "{/BROWN}", "\033[0;33m" },
			{ "{/DGRAY}", "\033[1;30m" }, { "{/DRED}", "\033[0;31m" },
			{ "{/DGREEN}", "\033[0;32m" }, { "{/DBLUE}", "\033[0;34m" },
			{ "{/DMAGENTA}", "\033[0;35m" }, { "{/DCYAN}", "\033[0;36m" } };

	/** Local TCP/IP socket. */
	protected Socket connection;

	/** Input stream for incoming data. */
	protected BufferedInputStream input;

	/** Output stream for outgoing data. */
	protected BufferedOutputStream output;

	/**
	 * Creates a telnet connection based on an already open connection.
	 * 
	 * @param connection
	 *            the open connection
	 */
	public TelnetConnection(Socket connection) throws IOException {
		try {
			this.connection = connection;
			input = new BufferedInputStream(connection.getInputStream());
			output = new BufferedOutputStream(connection.getOutputStream());
		} catch (IOException e) {
			this.connection = null;
			input = null;
			output = null;
			throw new IOException("Could not establish connection: "
					+ e.getMessage());
		}
	}

	/**
	 * Disconnects.
	 */
	public void disconnect() throws IOException {
		try {
			connection.close();
		} catch (IOException e) {
			throw new IOException("Could not properly disconnect: "
					+ e.getMessage());
		} finally {
			connection = null;
			input = null;
			output = null;
		}
	}

	/**
	 * Parses a string for color codes and replaces them with the corresponding
	 * ANSI code.
	 * 
	 * @param s
	 *            string to parse
	 * @return the parsed string
	 */
	public String parseColors(String s) {
		boolean isDone = false;
		int i, p;

		do {
			isDone = true;

			for (i = 0; i < COLORS.length; ++i) {
				p = s.indexOf(COLORS[i][0]);
				if (p != -1) {
					s = s.substring(0, p) + COLORS[i][1]
							+ s.substring(p + COLORS[i][0].length());
					isDone = false;
				}
			}
		} while (!isDone);

		return s;
	}

	/**
	 * Sends a string.
	 * 
	 * @param s
	 *            string to send
	 */
	public void sendString(String s) throws IOException {
		s = parseColors(s);
		try {
			output.write(s.getBytes(), 0, s.getBytes().length);
			output.flush();
		} catch (Exception e) {
			throw new IOException("I/O error while sending data: "
					+ e.getMessage());
		}
	}

	/**
	 * Returns TRUE if data is available.
	 * 
	 * @return TRUE if data is available
	 */
	public boolean dataAvailable() throws IOException {
		boolean isAvailable;
		try {
			isAvailable = (input.available() != 0);
		} catch (IOException e) {
			throw new IOException(
					"I/O error while probing for available data: "
							+ e.getMessage());
		}
		return isAvailable;
	}

	/**
	 * Receives an incoming command, ending with a CR ('\r') character. This
	 * method will block until a command has been completely received. This
	 * method filters out any CR ('\r') or NL ('\n') characters.
	 * 
	 * @return the received command
	 */
	public String receiveCommand() throws IOException {
		StringBuilder s = new StringBuilder();
		byte[] buffer = new byte[256];
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
			throw new IOException("I/O error while receiving data: "
					+ e.getMessage());
		}
		return s.toString();
	}

}
