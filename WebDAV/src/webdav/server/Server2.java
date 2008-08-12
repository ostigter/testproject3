package webdav.server;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server2 {


    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        
        Socket clientSocket = serverSocket.accept();

        InputStream is = clientSocket.getInputStream();
        OutputStream os = clientSocket.getOutputStream();
        
        byte[] buffer = new byte[8192];
        while (is.available() == 0);
        int length = is.available();
        is.read(buffer, 0, length);
        String request = new String(buffer, 0, length);
        System.out.println("\nRequest:\n" + request);
        
        String entity =
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
            "<D:multistatus xmlns:D=\"DAV:\">\n" +
            "  <D:response>\n" +
            "    <D:propstat>\n" +
            "      <D:prop>\n" +
            "        <D:displayname>MyCollection</D:displayname>\n" +
            "        <D:resourcetype><D:collection/></D:resourcetype>\n" +
            "      </D:prop>\n" +
            "      <D:status>HTTP/1.1 200 OK</D:status>\n" +
            "    </D:propstat>\n" +
            "  </D:response>\n" +
            "</D:multistatus>\n";
        String response =
            "HTTP/1.1 207 Multi-Status\n" +
            "Content-Type: text/xml; charset=\"utf-8\"\n" +
            "Content-Length: " + entity.length() + "\n" +
            "\n" +
            entity;
        System.out.println("\nResponse:\n" + response);
        
        os.write(response.getBytes("utf-8"));

        os.close();
        is.close();
        clientSocket.close();
    }


}
