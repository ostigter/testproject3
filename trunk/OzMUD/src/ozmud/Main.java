package ozmud;


import ozmud.server.Server;


/**
 * Main class starting the server.
 * 
 * @author Oscar Stigter
 */
public class Main {


	/**
	 * Application's entry point.
	 * 
	 * @param  args  command line arguments
	 */
	public static void main(String[] args) {
		new Server().start();
	}


}
