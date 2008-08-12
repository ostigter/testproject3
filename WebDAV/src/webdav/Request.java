package webdav;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;


/**
 * An HTTP request.
 * 
 * @author Oscar Stigter
 */
public class Request {
	
	
    /** Carriage Return (CR) character. */
    private static final char CR = '\r';
    
    /** Line Feed (LF) character. */
    private static final char LF = '\n';
    
    /** State for reading the method. */
    private static final int METHOD  = 0;
    
    /** State for reading the headers. */
    private static final int HEADER  = 1;
    
    /** State for reading the body. */
    private static final int BODY    = 2;
    
    /** HTTP request method */
    private Method method;
    
    /** HTTP request URI. */
    private String uri;
    
    /** HTTP request headers. */
    private Map<String, String> headers;
    
    /** HTTP request body. */
    private String body;
    
	
	/**
	 * Constructs a HTTP request based on an input stream.
	 * 
	 * @param is  the input stream
	 */
    public Request(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int part = METHOD;
        
        headers = new HashMap<String, String>();
        try {
            Reader reader = new InputStreamReader(
                    new ByteArrayInputStream(data), "utf-8");
            while (reader.ready()) {
                char ch = (char) reader.read();
                if (part == METHOD || part == HEADER) {
                    if (ch == CR) {
                        // Skip any CR characters.
                    } else if (ch == LF) {
                        String line = sb.toString();
                        if (part == METHOD) {
                            String[] parts = line.split(" ");
                            if (parts.length == 3) {
                                method = Method.valueOf(parts[0]);
                                if (method == null) {
                                    System.err.println("WARN: Unknown HTTP method: " + parts[0]);
                                }
                                uri = parts[1];
                                String version = parts[2];
                                if (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
                                    System.err.println("ERROR: Unsupported HTTP version: " + version);
                                }
                            } else {
                                System.err.println("ERROR: Bad HTTP request");
                            }
                            part = HEADER;
                        } else {
                            // Header
                            if (line.length() == 0) {
                                // Empty line indicating start of body.
                                part = BODY;
                            } else {
                                int pos = line.indexOf(':');
                                if (pos != -1) {
                                    String name = line.substring(0, pos).trim();
                                    String value = line.substring(pos + 1).trim();
                                    headers.put(name, value);
                                } else {
                                    System.err.println("WARN: Invalid header line: " + line);
                                }
                            }
                        }
                        sb.delete(0, sb.length());
                    } else {
                        sb.append(ch);
                    }
                } else {
                    // Body
                    sb.append(ch);
                }
            }
            body = sb.toString();
        } catch (IOException e) {
            System.err.println(e);
        }
	}
	
	
	public Method getMethod() {
	    return method;
	}
	
	
	public String getUri() {
	    return uri;
	}
	
	
	public Map<String, String> getHeaders() {
	    return headers;
	}
	
	
	public String getBody() {
	    return body;
	}
	
	
}
