package webdav.server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import webdav.Method;
import webdav.Request;


public class RequestHandler extends Thread {
	

	private static final String CRLF = "\r\n";
	
    private Backend backend;
    
	private InputStream is;
	
	private OutputStream os;
	

	public RequestHandler(Socket socket, Backend backend) {
	    setDaemon(true);
        this.backend = backend;
	    try {
    		is = socket.getInputStream();
    		os = socket.getOutputStream();
	    } catch (IOException e) {
	        System.err.println("ERROR: " + e);
	    }
	}
	
	
	@Override
	public void run() {
	    boolean isRunning = true;
        try {
            while (isRunning) {
                if (is.available() > 0) {
                    int length = is.available();
                    byte[] buffer = new byte[length];
                    is.read(buffer, 0, length);
                    Request request = new Request(buffer);
                    handleRequest(request);
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // Ignore.
                    }
                }
            }
		} catch (IOException e) {
			System.err.println("ERROR: Could not handle request: " + e);
		}
	}
	
	
	private void handleRequest(Request request) throws IOException {
	    
        Method method = request.getMethod();
        String uri = request.getUri();
        String body = request.getBody();

        System.out.println("\n--- START REQUEST ---");
        System.out.println("Method: " + method);
        System.out.println("URI:    " + uri);
        Map<String, String> headers = request.getHeaders();
        for (String name : headers.keySet()) {
            System.out.println("Header: '" + name + "' = '" + headers.get(name) + "'");
        }
        System.out.println("Body: \n" + body);
        System.out.println("---- END REQUEST ----");
        
        if (method != null) {
            String response; 
            switch (request.getMethod()) {
                case OPTIONS:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "Allow: OPTIONS, GET, HEAD, POST, PUT, DELETE, PROPFIND, PROPPATCH, MKCOL, COPY, MOVE, LOCK, UNLOCK" + CRLF +
                        "DAV: 1" + CRLF +
                        "MS-Author-Via: DAV" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case PROPFIND:
                    backend.propFind(request, os);
                    break;
                case GET:
                    backend.get(request, os);
                    break;
                case HEAD:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case PUT:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case POST:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case DELETE:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case MKCOL:
                    response =
                        "HTTP/1.1 201 Created" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case COPY:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case MOVE:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case LOCK:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                case UNLOCK:
                    response =
                        "HTTP/1.1 200 OK" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
                    break;
                default:
                    // Unknown method.
                    response =
                        "HTTP/1.1 501 Not Implemented" + CRLF +
                        "DAV: 1" + CRLF +
                        "Content-Length: 0" + CRLF +
                        CRLF;
                    os.write(response.getBytes("utf-8"));
            }
        } else {
            System.err.println("ERROR: Could not parse request!");
        }
	}
	
	
}
