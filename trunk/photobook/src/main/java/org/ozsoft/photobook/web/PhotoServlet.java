package org.ozsoft.photobook.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.ozsoft.photobook.services.PhotoService;

public class PhotoServlet extends HttpServlet {

	private static final long serialVersionUID = 6009957501665177592L;
	
	@EJB
    private PhotoService photoService;
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			if (pathInfo.length() > 1 && pathInfo.charAt(0) == '/') {
				try {
					long id = Integer.parseInt(pathInfo.substring(1));
					System.out.format("*** Get content of photo %d\n", id);
					InputStream is = photoService.getContent(id);
					if (is != null) {
						response.setContentType("image/jpeg");
						IOUtils.copy(is, response.getOutputStream());
						IOUtils.closeQuietly(is);
					} else {
						System.out.format("*** Photo not found: %d\n", id);
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				} catch (NumberFormatException e) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
}
