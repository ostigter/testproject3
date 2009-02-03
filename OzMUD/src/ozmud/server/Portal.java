package ozmud.server;


import java.io.IOException;

import ozmud.Util;
import ozmud.world.Gender;
import ozmud.world.Player;
import ozmud.world.World;


/**
 * Handles the login procedure for existing players and the creation procedure
 * for new player characters.
 */
public class Portal implements Runnable {
	

	/** Welcome text. */
	private static final String WELCOME_TEXT =
			"${CYAN}\n\rYou enter the Realm of Oz.\n\r\n\r"; 

	/** The connection to the client's client. */
	private final Connection connection;
	

	/**
	 * Constructor.
	 * 
	 * @param connection  an open socket
	 */
	public Portal(Connection connection) throws IOException {
		this.connection = connection;
	}


	/**
	 * Thread's main program.
	 */
	public void run() {
		// TODO: Clean up code
		
		try {
			String text = null;
			String name = null;
			String password = null;
			Gender gender = null;
			Player player = null;
	
			while (player == null) {
				boolean nameOK = false;
				while (!nameOK) {
					// Send welcome text.
	//				connection.send("${BLUE}\r\n\r\n###############################\n\r");
	//				connection.send("#####${CYAN}  Welcome to OzMUD!  ${BLUE}#####\n\r");
	//				connection.send("###############################\n\r");
	//				connection.send("${GRAY}\n\r\n\r(c)2009 Oscar Stigter\n\r");
	
					// Ask for player's name.
					connection.send("${GREEN}\n\r\n\rPlease enter your character's name: ");
					text = connection.receive(true);
					if (text.length() == 0) {
						connection.send("${RED}You must enter a name. Please try again.\r\n\r");
					} else if (text.length() > 15) {
						connection.send("${RED}Name is too long (max. 15 characters). Please choose another.\r\n\r");
					} else {
						name = Util.capitalize(text);
						nameOK = true;
					}
				}
	
				// Lookup character.
				player = World.getInstance().getPlayer(name);
				if (player == null) {
					// New character; ask whether to create it now.
					connection.send("${GREEN}\n\r" + name
							+ " is not a registered character.\n\r"
							+ "Create this character? (Yes/No) : ");
					while (!connection.dataAvailable()) {
						// Wait.
					}
					text = connection.receive(true).toLowerCase();
					if (text.length() > 0 && text.charAt(0) == 'y') {
						boolean passwordOK = false;
						while (!passwordOK) {
							// Enter new password.
							connection.send("${GREEN}\n\rType a new password: ");
							while (!connection.dataAvailable()) {
								// Wait.
							}
							password = connection.receive(true);
							if (password.length() == 0) {
								connection.send("${RED}You must enter a password. Please try again.\n\r");
							} else if (password.length() > 15) {
								connection.send("${RED}Password is too long (max. 15 characters). Please choose another.\n\r");
							} else {
								passwordOK = true;
							}
							
							if (passwordOK) {
								// Re-enter password.
								connection .send("${GREEN}Retype the password for verification  : ");
								while (!connection.dataAvailable()) {
									// Wait.
								}
								text = connection.receive(true);
								if (!text.equals(password)) {
									connection.send("${RED}Passwords do not match. Please try again.\n\r");
									passwordOK = false;
								}
							}
						}
						
						// Select gender.
						while (gender == null) {
							connection.send("${GREEN}\n\rPlease choose a gender (Male/Female) : ");
							while (!connection.dataAvailable()) {
								// Wait.
							}
							text = connection.receive(true).toLowerCase();
							if (text.length() > 0) {
								if (text.charAt(0) == 'm') {
									gender = Gender.MALE;
								} else if (text.charAt(0) == 'f') {
									gender = Gender.FEMALE;
								}
							}
							if (gender == null) {
								connection.send("${RED}Invalid selection. Please try again.\n\r");
							}
						}
						
						// Register player.
						player = new Player();
						player.setShortName(name);
						player.setFullName(name);
						player.setGender(gender);
						player.setPassword(password);
						World.getInstance().addPlayer(player);
					}
				} else {
					// Existing player; check password.
					connection.send("${GREEN}Please enter your password: ");
					while (!connection.dataAvailable()) {
						// Wait.
					}
					password = connection.receive(true);
					if (!password.equals(player.getPassword())) {
						connection.send("${RED}Incorrect password.\n\r");
						System.out.println(String.format(
								"*** WARNING: Failed login attempt for character %s!\n",
								name));
						player = null;
					}
				}
			}
	
			System.out.println(String.format("%s has logged in", name));
			player.connect(connection);
			player.send(WELCOME_TEXT);
			player.start();
			
		} catch (IOException e) {
			System.err.println("*** I/O error: " + e.getMessage());
		}
	}


}
