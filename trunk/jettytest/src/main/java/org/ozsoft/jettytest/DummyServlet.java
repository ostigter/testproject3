package org.ozsoft.jettytest;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DummyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(DummyServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	// Analyse request.
    	String method = request.getMethod();
    	String uri = request.getRequestURI();
    	String from = request.getRemoteAddr();
    	LOG.debug(String.format("Handling request from address %s with method %s and URI '%s'", from, method, uri));
    	LOG.debug("ContentType: " + request.getContentType());
    	LOG.debug("ContentLength: " + request.getContentLength());
    	StringBuilder sb = new StringBuilder();
    	BufferedReader reader = request.getReader();
    	String line = null;
    	while ((line = reader.readLine()) != null) {
    		sb.append(line);
    		sb.append('\n');
    	}
    	String requestBody = sb.toString();
    	LOG.debug("Request body:\n" + requestBody);
    	
    	// Create response.
        response.setContentType("text/xml");
        response.setStatus(HttpServletResponse.SC_OK);
        sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<Response>OK</Response>\n");
        String responseBody = sb.toString();
        LOG.debug("Response body:\n" + responseBody);
        response.getWriter().write(responseBody);
	}
	
}
