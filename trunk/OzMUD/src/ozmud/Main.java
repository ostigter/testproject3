package ozmud;


import ozmud.server.Server;


/**
 * Main class that starts the server.
 * 
 * @author Oscar Stigter
 */
public class Main {


	/**
	 * Application's entry point.
	 * 
	 * @param  args  Any commandline arguments.
	 */
	public static void main(String[] args) {
		new Server().start();
	}


}
