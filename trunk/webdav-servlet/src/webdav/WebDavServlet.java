package webdav;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


/**
 * Servlet implementation of a WebDAV level 2 server.
 *
 * @author Oscar Stigter
 */
 public class WebDavServlet extends HttpServlet {
     

     private static final long serialVersionUID = 1L;
     
     private static final Logger logger = Logger.getLogger(WebDavServlet.class);
     
   
	public WebDavServlet() {
		super();
	} 
	

    @Override
    public void init() throws ServletException {
        super.init();
        
        String log4jConfigFile = getInitParameter("log4j-config-file");
        if (log4jConfigFile != null) {
            String servletPath = getServletContext().getRealPath("/");
            File file = new File(servletPath + "/" + log4jConfigFile);
            if (file.exists()) {
                DOMConfigurator.configure(file.getPath());
//                FileAppender appender =
//                    (FileAppender) Logger.getRoot().getAppender("webdav-file-appender");
//                appender.setFile(servletPath + "/webdav.log");
            } else {
                throw new ServletException(
                        "log4j config file not found: " + file.getAbsolutePath());
            }
        } else {
            throw new ServletException(
                    "Missing init parameter: log4j-config-file");
        }
        
        logger.debug("Initialized");
    }   
    

    @Override
	public void destroy() {
		super.destroy();
        logger.debug("Destroyed");
	}  
	
	
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String method = req.getMethod();
        
        logger.debug("Request method: " + method);
        
        for (Enumeration e = req.getHeaderNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            String value = req.getHeader(name);
            logger.debug("Header: " + name + ": " + value);
        }
        
        Reader reader = req.getReader();
        char[] buffer = new char[8192];
        StringBuilder sb = new StringBuilder();
        int len;
        while ((len = reader.read(buffer)) > 0) {
            sb.append(buffer, 0, len);
        }
        logger.debug("Request body:\n" + sb.toString());
        
        if (method.equals("PROPFIND")) {
            doPropfind(req, resp);
        } else if (method.equals("OPTIONS")) {
            doOptions(req, resp);
        } else if (method.equals("GET")) {
            doGet(req, resp);
        } else if (method.equals("HEAD")) {
            doHead(req, resp);
        } else {
            logger.warn("Unsupported HTTP method: " + method);
            resp.setStatus(400);
        }
    }


    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doOptions(req, resp);
        logger.debug("OPTIONS");
        resp.setStatus(200);
        resp.addHeader("Allow",
                "OPTIONS, GET, HEAD, POST, PUT, DELETE, PROPFIND, PROPPATCH, "
                + "MKCOL, COPY, MOVE, LOCK, UNLOCK");
        resp.addHeader("DAV", "1, 2");
        resp.addHeader("MS-Author-Via", "DAV");
    }
    
    
    private void doPropfind(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        System.out.println("URI:  " + uri);
        String host = req.getHeader("Host");
        System.out.println("Host: " + host);

        // Response status and headers.
        resp.setStatus(207);  // Multi-Status
        resp.setContentType("text/xml");
        resp.addHeader("DAV", "1, 2");
        resp.addHeader("MS-Author-Via", "DAV");
        
        // Response body.
        
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        sb.append("<multistatus xmlns=\"DAV:\">");
        
        // Collection.
        sb.append("<response>");
        sb.append("<href>");
        sb.append(host);
        sb.append("/");
        sb.append("MyCollection");
        sb.append("</href>");
        sb.append("<propstat>");
        sb.append("<prop>");
        sb.append("<displayname>");
        sb.append("MyCollection");
        sb.append("</displayname>");
        sb.append("<creationdate>");
        sb.append("2008-01-01T12:00:00+01:00");
        sb.append("</creationdate>");
        sb.append("<resourcetype><collection/></resourcetype>");
        sb.append("</prop>");
        sb.append("<status>HTTP/1.1 200 OK</status>");
        sb.append("</propstat>");
        sb.append("</response>");

//         Document.
//        sb.append("<response>");
//        sb.append("<href>");
//        sb.append(host);
//        sb.append("/");
//        sb.append("doc.xml");
//        sb.append("</href>");
//        sb.append("<propstat>");
//        sb.append("<prop>");
//        sb.append("<displayname>");
//        sb.append("doc.xml");
//        sb.append("</displayname>");
//        sb.append("<resourcetype><resource/></resourcetype>");
//        sb.append("<getcontenttype>text/xml</getcontenttype>");
//        sb.append("<getcontentlength>123</getcontentlength>");
//        sb.append("<getlastmodified></getlastmodified>");
//        sb.append("<name>name1</name>");
//        sb.append("<parentname>parentname1</parentname>");
//        sb.append("<isroot>false</isroot>");
//        sb.append("<ishidden>false</ishidden>");
//        sb.append("<isreadonly>false</isreadonly>");
//        sb.append("<iscollection>false</iscollection>");
//        sb.append("<isstructureddocument>false</isstructureddocument>");
//        sb.append("<defaultdocument></defaultdocument>");
//        sb.append("</prop>");
//        sb.append("<status>HTTP/1.1 200 OK</status>");
//        sb.append("</propstat>");
//        sb.append("</response>");
        
        sb.append("</multistatus>");
        
        String response = sb.toString();
        logger.debug("Response:\n" + response);
        resp.getWriter().write(response);
    }


    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("GET");
    }   
    

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doHead(req, resp);
        logger.debug("HEAD");
    }


    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("POST");
    }       
    

    @Override
    protected void doPut(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("PUT");
    }       
    

    @Override
	protected void doDelete(
	        HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
        logger.debug("DELETE");
	}  	
	

    @Override
	public String getServletInfo() {
        logger.debug("getServletInfo()");
	    return "WebDAV level 2 server";
	}   
	

}
