package ozmud.server;

import java.io.IOException;
import java.net.Socket;

import ozmud.world.Player;
import ozmud.world.World;

/**
 * Handles the login procedure for existing players and the creation of new
 * players.
 */
public class Portal implements Runnable {

	/** Telnet connection. */
	private TelnetConnection connection;

	/** The world. */
	private World world;

	/** Flag indicating whether connected or not. */
	private boolean isRunning = true;

	/**
	 * Creates a portal with the specified connection.
	 * 
	 * @param connection
	 *            the open connection
	 */
	public Portal(Socket connection, World world) throws IOException {
		try {
			this.connection = new TelnetConnection(connection);
			this.world = world;
			// System.out.println("Portal connected to client.");
		} catch (IOException e) {
			throw new IOException("Portal could not connect to client: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * Thread's main program.
	 */
	public void run() {
		String text = null;
		String name = null;
		String password = null;
		boolean nameOK = false;
		boolean passwordOK = false;
		Player player = null;

		while (isRunning) {
			try {
				do {
					do {
						nameOK = true;

						// Send intro
						connection
								.sendString("{/BLUE}\r\n\r\n###############################\n\r");
						connection
								.sendString("{/BLUE}#####{/CYAN}  Welcome to OzMUD!  {/BLUE}#####\n\r");
						connection
								.sendString("{/BLUE}###############################\n\r");
						connection
								.sendString("{/GRAY}\n\r\n\r(c)2009 Oscar Stigter\n\r");

						// Ask for name
						connection
								.sendString("{/GREEN}\n\r\n\rPlease enter your character's name: ");
						name = connection.receiveCommand();
						if (name.length() == 0) {
							nameOK = false;
							connection
									.sendString("{/RED}You must enter a name. Please try again.\r\n\r");
						}
						if (name.length() > 15) {
							nameOK = false;
							connection
									.sendString("{/RED}Name is too long (max. 15 characters). Please choose another.\r\n\r");
						}
					} while (!nameOK);

					// Lookup player
					player = world.getPlayer(name);
					if (player == null) {
						// New player
						connection
								.sendString("{/GREEN}\n\r"
										+ name
										+ " is not a registered character.\n\rCreate this character? (y/n) : ");
						while (!connection.dataAvailable())
							;
						text = connection.receiveCommand();
						if ((text.length() != 0)
								&& (text.charAt(0) == 'y' || text.charAt(0) == 'Y')) {
							do {
								do {
									passwordOK = true;
									connection
											.sendString("{/GREEN}\n\rType a new password for this character: ");
									while (!connection.dataAvailable())
										;
									password = connection.receiveCommand();
									if (password.length() == 0) {
										passwordOK = false;
										connection
												.sendString("{/RED}You must enter a password. Please try again.\n\r");
									}
									if (password.length() > 12) {
										passwordOK = false;
										connection
												.sendString("{/RED}Password is too long (max. 12 characters). Please choose another.\n\r");
									}
								} while (!passwordOK);
								connection
										.sendString("{/GREEN}Retype the password for verification  : ");
								while (!connection.dataAvailable())
									;
								text = connection.receiveCommand();
								if (!text.equals(password)) {
									passwordOK = false;
									connection
											.sendString("{/RED}Passwords are not identical. Please try again.\n\r");
								}
							} while (!passwordOK);
							world.addPlayer(name, password);
							nameOK = true;
						}
					} else {
						// Existing player
						passwordOK = false;
						connection
								.sendString("{/GREEN}\n\rPlease enter your password: ");
						while (!connection.dataAvailable())
							;
						password = connection.receiveCommand();
						// System.out.println("Login attempt: player '" + name +
						// "' with password '" + password + "'.");
						if (!password.equals(player.getPassword())) {
							connection
									.sendString("{/RED}Incorrect password.\n\r");
						} else {
							passwordOK = true;
						}
					}
				} while (!(nameOK && passwordOK));

				connection
						.sendString("{/CYAN}\n\r\n\rYou enter the Realm of Oz...\n\r\n\r\n\r\n\r");

				// Connect player to this connection.
				player.connect(connection);

				// Let player continue in new thread.
				new Thread(player).start();

				// End this portal thread.
				isRunning = false;

			} catch (Exception e) {
				System.err.println("*** Portal error: " + e.getMessage());
			}
		}
	}

}
