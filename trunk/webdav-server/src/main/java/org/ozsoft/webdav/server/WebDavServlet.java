package org.ozsoft.webdav.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation of a WebDAV level 2 server.
 * 
 * @author Oscar Stigter
 */
public class WebDavServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(WebDavServlet.class);

	@Override
	public String getServletInfo() {
		LOG.debug("getServletInfo()");
		return "WebDAV level 2 server";
	}

	@Override
	public void init() throws ServletException {
		super.init();
		LOG.debug("Initialized");
	}

	@Override
	public void destroy() {
		super.destroy();
		LOG.debug("Destroyed");
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getMethod();
		LOG.debug("Request method: " + method);

		for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			String value = request.getHeader(name);
			LOG.debug("Header: " + name + ": " + value);
		}

		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append('\n');
		}
		String requestBody = sb.toString();
		if (requestBody.length() != 0) {
			LOG.debug("Request body:\n" + requestBody);
		}

		if (method.equals("OPTIONS")) {
			doOptions(request, response);
		} else if (method.equals("PROPFIND")) {
			doPropfind(request, response);
		} else if (method.equals("HEAD")) {
			doHead(request, response);
		} else if (method.equals("GET")) {
			doGet(request, response);
		} else if (method.equals("PUT")) {
			doPut(request, response);
		} else if (method.equals("DELETE")) {
			doDelete(request, response);
		} else if (method.equals("POST")) {
			doPost(request, response);
		} else {
			LOG.warn("Unsupported HTTP method: " + method);
			response.setStatus(400);
		}
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doOptions(request, response);
		LOG.debug("OPTIONS");
		response.setStatus(200);
		response.addHeader("Allow",
				"OPTIONS, GET, HEAD, POST, PUT, DELETE, PROPFIND, PROPPATCH, MKCOL, COPY, MOVE, LOCK, UNLOCK");
		response.addHeader("DAV", "1, 2");
		response.addHeader("MS-Author-Via", "DAV");
	}

	private void doPropfind(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		LOG.debug("PROPFIND  " + uri);
		String host = request.getHeader("Host");
		LOG.debug("Host: " + host);

		// Response status and headers.
		response.setStatus(207); // Multi-Status
		response.setContentType("text/xml;charset=UTF-8");
//		response.addHeader("DAV", "1");
//		response.addHeader("MS-Author-Via", "DAV");

		// Response body.

		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append("<D:multistatus xmlns:D=\"DAV:\">\n");

		// Collection.
		sb.append("  <D:response>\n");
		sb.append("    <D:href>/webdav/db</D:href>\n");
		sb.append("    <D:propstat>\n");
		sb.append("      <D:prop>\n");
		sb.append("        <D:displayname>data</D:displayname>\n");
		sb.append("        <D:resourcetype><D:collection/></D:resourcetype>\n");
		sb.append("        <D:creationdate>2009-07-28T12:44:24Z</D:creationdate>\n");
		sb.append("        <D:getlastmodified>Tue, 28 Jul 2009 12:44:24 GMT</D:getlastmodified>\n");
		sb.append("      </D:prop>\n");
		sb.append("      <D:status>HTTP/1.1 200 OK</D:status>\n");
		sb.append("    </D:propstat>\n");
		sb.append("    <D:propstat>\n");
		sb.append("      <D:prop>\n");
		sb.append("        <D:getcontentlength/>\n");
		sb.append("        <D:getcontenttype/>\n");
		sb.append("        <D:getetag/>\n");
		sb.append("      </D:prop>\n");
		sb.append("      <D:status>HTTP/1.1 404 Not Found</D:status>\n");
		sb.append("    </D:propstat>\n");
		sb.append("  </D:response>\n");

		// Document.
		// sb.append("<response>");
		// sb.append("<href>");
		// sb.append(host);
		// sb.append("/");
		// sb.append("doc.xml");
		// sb.append("</href>");
		// sb.append("<propstat>");
		// sb.append("<prop>");
		// sb.append("<displayname>");
		// sb.append("doc.xml");
		// sb.append("</displayname>");
		// sb.append("<resourcetype><resource /></resourcetype>");
		// sb.append("<getcontenttype>text/xml</getcontenttype>");
		// sb.append("<getcontentlength>123</getcontentlength>");
		// sb.append("<getlastmodified></getlastmodified>");
		// sb.append("<name>name1</name>");
		// sb.append("<parentname>parentname1</parentname>");
		// sb.append("<isroot>false</isroot>");
		// sb.append("<ishidden>false</ishidden>");
		// sb.append("<isreadonly>false</isreadonly>");
		// sb.append("<iscollection>false</iscollection>");
		// sb.append("<isstructureddocument>false</isstructureddocument>");
		// sb.append("<defaultdocument></defaultdocument>");
		// sb.append("</prop>");
		// sb.append("<status>HTTP/1.1 200 OK</status>");
		// sb.append("</propstat>");
		// sb.append("</response>");

		sb.append("</D:multistatus>\n");

		String responseBody = sb.toString();
		LOG.debug("Response body:\n" + responseBody);
		response.getWriter().write(responseBody);
	}

	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("HEAD " + uri);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("GET " + uri);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("POST " + uri);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("PUT " + uri);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("DELETE " + uri);
	}

}
