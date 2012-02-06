package org.ozsoft.photobook.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.ozsoft.photobook.entities.Photo;
import org.ozsoft.photobook.repositories.PersistenceService;

public class PhotoServlet extends HttpServlet {

	private static final long serialVersionUID = 6280329522978416221L;
	
	private final EntityManager em;
	
	private final Query query;
	
	public PhotoServlet() {
		em = PersistenceService.getEntityManager();
		query = em.createQuery("SELECT p FROM Photo p WHERE p.id = :id");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			if (pathInfo.length() > 1 && pathInfo.charAt(0) == '/') {
				try {
					long photoId = Integer.parseInt(pathInfo.substring(1));
					query.setParameter("id", photoId);
					List<Photo> photos = query.getResultList();
					if (photos.size() > 0) {
						Photo photo = photos.get(0);
						byte[] content = photo.getContent();
						response.setContentType("image/jpeg");
						response.setContentLength(content.length);
						InputStream is = new ByteArrayInputStream(content);
						try {
							IOUtils.copy(is, response.getOutputStream());
						} finally {
							IOUtils.closeQuietly(is);
						}
					} else {
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				} catch (NumberFormatException e) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
}
