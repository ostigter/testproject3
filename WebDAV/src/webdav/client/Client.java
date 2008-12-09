package webdav.client;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import sun.misc.BASE64Encoder;


public class Client {
    
    
    private static final int PORT = 5000; 
    
    private static final String USERNAME = "admin"; 
    
    private static final String PASSWORD = ""; 
    
    private static final String CRLF = "\r\n";
    
    private static final int BUFFER_SIZE = 64 * 1024;  // 64 kB
    
    private static final byte[] buffer = new byte[BUFFER_SIZE];
    
    
    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", PORT);
        
        String credentials = new BASE64Encoder().encode(
                (USERNAME + ":" + PASSWORD).getBytes());
        
        String entity =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + CRLF +
            "<D:propfind xmlns:D=\"DAV:\"><D:allprop/></D:propfind>" + CRLF;
        String request =
            "PROPFIND /db HTTP/1.1" + CRLF +
            "Host: localhost:8080" + CRLF +
            "Authorization: Basic " + credentials + CRLF +
            "Depth: 1" + CRLF +
            "Content-Type: text/xml; charset=\"utf-8\"" + CRLF +
            "Content-Length: " + entity.length() + CRLF +
            CRLF +
            entity;
        
//        String entity = "";
//        String request =
//            "OPTIONS / HTTP/1.1" + CRLF +
//            "Host: localhost:8080" + CRLF +
//            "Authorization: Basic " + credentials + CRLF +
//            "Content-Type: text/xml; charset=\"utf-8\"" + CRLF +
//            "Content-Length: " + entity.length() + CRLF +
//            CRLF +
//            entity;
        
        OutputStream os = socket.getOutputStream();
        os.write(request.getBytes());
        
        InputStream is = socket.getInputStream();
        int length = is.read(buffer);
        String response = new String(buffer, 0, length);
        System.out.println("--- START RESPONSE ---");
        System.out.println(response);
        System.out.println("---- END RESPONSE ----");

        is.close();
        os.close();
        socket.close();
    }
    

}
