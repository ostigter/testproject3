package webdav.server;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import webdav.Request;


public class FileSystemBackend implements Backend {
    
    
    private static final String CRLF = "\r\n";
    
    private static final int BUFFER_SIZE = 8192;  // 8 kB
	
	private static final String ROOT_DIR =
			new File("data").getAbsolutePath().replaceAll("\\\\", "/");
	
	
	public FileSystemBackend() {
		File dir = new File(ROOT_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}


	public void propFind(Request request, OutputStream response)
			throws IOException {
        StringBuilder sb = new StringBuilder();
        
        String uri = request.getUri();
		String host = request.getHeaders().get("Host");
		System.out.println("Host: " + host);
        
		String path = ROOT_DIR + uri; 
		File file = new File(path);
		
		if (file.exists()) {
			if (file.isDirectory()) {
			    // Collection resource.
                StringBuilder entity = new StringBuilder();
				entity.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
				entity.append("<multistatus xmlns=\"DAV:\">");
				entity.append("<response>");
				// Collection.
                entity.append("<href>");
                entity.append(host).append("/");
                entity.append("</href>");
				entity.append("<propstat>");
                entity.append("<prop>");
                entity.append("<displayname>");
                entity.append(uri);
                entity.append("</displayname>");
				entity.append("<creationdate>");
				entity.append("2008-01-01T12:00:00+01:00");
				entity.append("</creationdate>");
				entity.append("<resourcetype><collection/></resourcetype>");
				entity.append("</prop>");
                entity.append("<status>HTTP/1.1 200 OK</status>");
				entity.append("</propstat>");
                entity.append("</response>");
                // Document.
				entity.append("<response>");
                entity.append("<href>");
                entity.append(host).append('/');
                entity.append("doc.xml");
                entity.append("</href>");
				entity.append("<propstat>");
                entity.append("<prop>");
                entity.append("<displayname>doc.xml</displayname>");
				entity.append("<resourcetype><resource/></resourcetype>");
				entity.append("<getcontenttype>text/xml</getcontenttype>");
				entity.append("<getcontentlength>123</getcontentlength>");
				entity.append("<getlastmodified></getlastmodified>");
                entity.append("<iscollection>false</iscollection>");
                entity.append("<name>name1</name>");
                entity.append("<parentname>parentname1</parentname>");
                entity.append("<isroot>false</isroot>");
                entity.append("<ishidden>false</ishidden>");
                entity.append("<isreadonly>false</isreadonly>");
                entity.append("<isstructureddocument>false</isstructureddocument>");
                entity.append("<defaultdocument></defaultdocument>");
				entity.append("</prop>");
                entity.append("<status>HTTP/1.1 200 OK</status>");
				entity.append("</propstat>");
                entity.append("</response>");
				entity.append("</multistatus>");

				sb.append("HTTP/1.1 207 Multi-Status").append(CRLF);
                sb.append("DAV: 2").append(CRLF);
                sb.append("MS-Author-Via: DAV").append(CRLF);
                sb.append("Content-Type: text/xml").append(CRLF);
                sb.append("Content-Length: ");
                sb.append(String.valueOf(entity.length())).append(CRLF);
                sb.append(CRLF);
                sb.append(entity);
                
//                System.out.println("\n--- START RESPONSE ---");
//                System.out.println(sb);
//                System.out.println("---- END RESPONSE ----");
                
                response.write(sb.toString().getBytes("utf-8"));
			} else {
			    // Non-collection resource.
			}
		} else {
		    // Resource not found.
            String entity = "<b>ERROR: No resource with the URI '" + uri
                    + "' found.</b>" + CRLF;
            sb.append("HTTP/1.1 404 Not Found").append(CRLF);
            sb.append("Content-Type: text/html").append(CRLF);
            sb.append("Content-Length: ");
            sb.append(String.valueOf(entity.length())).append(CRLF);
            sb.append(CRLF);
            sb.append(entity);
            response.write(sb.toString().getBytes("utf-8"));
		}
	}


	public void get(Request request, OutputStream response)
			throws IOException {
        StringBuilder sb = new StringBuilder();
        
        String uri = request.getUri();
        
		String path = ROOT_DIR + uri; 
		File file = new File(path);

		if (file.exists()) {
			if (file.isDirectory()) {
			    // Collection resource; return a Collection element.
                String date = new Date(file.lastModified()).toString();
				StringBuilder entity = new StringBuilder();
				entity.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
				entity.append("<Collection uri=\"");
				entity.append(uri);
				entity.append("\" lastmodified=\"");
				entity.append(date);
				entity.append("\">\n");
				entity.append("</Collection>\n");

				sb.append("HTTP/1.1 OK 200").append(CRLF);
                sb.append("Content-Type: text/xml").append(CRLF);
                sb.append("Content-Length: ");
                sb.append(String.valueOf(entity.length())).append(CRLF);
                sb.append(CRLF);
                sb.append(entity.toString());
                
                System.out.println("\n--- START RESPONSE ---");
                System.out.println(sb);
                System.out.println("---- END RESPONSE ----");
                
				response.write(sb.toString().getBytes("utf-8"));
			} else {
			    // Non-collection resource; return its contents as byte stream.
			    sb.append("HTTP/1.1 OK 200").append(CRLF);
			    sb.append("Content-Length: ");
			    sb.append(String.valueOf(file.length())).append(CRLF);
			    sb.append(CRLF);
				InputStream is = new FileInputStream(file);
				byte[] buffer = new byte[BUFFER_SIZE];
				int length;
				while ((length = is.read(buffer)) > 0) {
				    response.write(buffer, 0, length);
				}
				is.close();
			}
		} else {
		    // Resource not found.
            String entity = "<b>ERROR: No resource with the URI '" + uri
                    + "' found.</b>" + CRLF;
            sb.append("HTTP/1.1 404 Not Found").append(CRLF);
            sb.append("Content-Type: text/html").append(CRLF);
            sb.append("Content-Length: ");
            sb.append(String.valueOf(entity.length())).append(CRLF);
            sb.append(CRLF);
            sb.append(entity);
            response.write(sb.toString().getBytes("utf-8"));
		}
	}
	
	
}
