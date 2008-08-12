package webdav.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	
	private static final int PORT = 5000;
	
	private Backend backend;
	
	private ServerSocket serverSocket;
	
	private boolean isRunning = false;
	
	
	public Server(Backend backend) {
		this.backend = backend;
	}
	
	
    public static void main(String[] args) {
    	Backend backend = new FileSystemBackend();
        Server server = new Server(backend);
        server.start();
    }


	public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            
            isRunning = true;
            System.out.println("HTTP/WebDAV server listening on port " + PORT + "...");
            
            try {
                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
//                  System.out.println("Client connected from '" + clientSocket.getInetAddress() + "'.");
                    new RequestHandler(clientSocket, backend).start();
                }
            } catch (IOException e) {
                System.err.println("ERROR: Could not accept client connection: " + e);
            }
        } catch (IOException e) {
            System.err.println("ERROR: Could not create socket on port " + PORT + ": " + e);
        }
        
        System.out.println("Server shut down.");
	}
	
	
	
	public void shutdown() {
	    isRunning = false;
	}
	

}
