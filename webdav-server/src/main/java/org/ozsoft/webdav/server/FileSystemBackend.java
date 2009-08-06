package org.ozsoft.webdav.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.ozsoft.webdav.WebDavException;

/**
 * File system WebDAV backend.
 * 
 * Serves files (resources) and directories (collections) stored in a specific directory.
 *  
 * @author Oscar Stigter
 */
public class FileSystemBackend implements WebDavBackend {
	
	/** Buffer size for copying streams. */
	private static final int BUFFER_SIZE = 8192;  // 8 kB
	
	/** Root directory. */
	private final File ROOT_DIR;
	
	/**
	 * Constructor.
	 * 
	 * @param path
	 *            The root directory path.
	 */
	public FileSystemBackend(String path) {
		if (path == null || path.length() == 0) {
			throw new IllegalArgumentException("Null or empty path");
		}
		ROOT_DIR = new File(path);
		if (ROOT_DIR.isDirectory()) {
			throw new IllegalArgumentException("Root directory not found: " + path);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String uri) {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		return new File(ROOT_DIR, uri).exists();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#isCollection(java.lang.String)
	 */
	@Override
	public boolean isCollection(String uri) {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		return new File(ROOT_DIR, uri).isDirectory();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getChildrenNames(java.lang.String)
	 */
	@Override
	public String[] getChildrenNames(String uri) {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		File dir = new File(ROOT_DIR, uri);
		if (dir.isDirectory()) {
			return dir.list();
		} else {
			throw new IllegalStateException("Not a collection: " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#createCollection(java.lang.String)
	 */
	@Override
	public void createCollection(String uri) throws WebDavException {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		File dir = new File(ROOT_DIR, uri);
		if (!dir.exists()) {
			boolean created = dir.mkdirs();
			if (!created) {
				throw new WebDavException(String.format("Could not create collection: %s", uri));
			}
		} else {
			if (dir.isFile()) {
				throw new WebDavException(String.format("Could not create collection '%s', resource exists", uri));
			} else {
				throw new WebDavException(String.format("Collection already exists: %s", uri));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#createResource(java.lang.String)
	 */
	@Override
	public void createResource(String uri) throws WebDavException {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		File file = new File(ROOT_DIR, uri);
		if (!file.exists()) {
			try {
				boolean created = file.createNewFile();
				if (!created) {
					throw new WebDavException(String.format("Could not create resource: %s", uri));
				}
			} catch (IOException e) {
				throw new WebDavException(String.format("Could not create resource: %s", uri), e);
			}
		} else {
			if (file.isDirectory()) {
				throw new WebDavException(String.format("Could not create file '%s', collection exists", uri));
			} else {
				throw new WebDavException(String.format("File already exists: %s", uri));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getContentLength(java.lang.String)
	 */
	@Override
	public long getContentLength(String uri) {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		File file = new File(ROOT_DIR, uri);
		if (file.isFile()) {
			return file.length();
		} else {
			return 0L;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getCreated(java.lang.String)
	 */
	@Override
	public Date getCreated(String uri) throws WebDavException {
		// Since Java doesn't support the creation date on files, just return the last modified date.
		return getLastModified(uri);
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getLastModified(java.lang.String)
	 */
	@Override
	public Date getLastModified(String uri) throws WebDavException {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		File file = new File(ROOT_DIR, uri);
		if (file.exists()) {
			return new Date(file.lastModified());
		} else {
			throw new WebDavException("Resource not found: " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getContent(java.lang.String)
	 */
	@Override
	public InputStream getContent(String uri) throws WebDavException {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		InputStream is = null;
		File file = new File(ROOT_DIR, uri);
		if (file.exists()) {
			if (file.isFile()) {
				try {
					is = new FileInputStream(file);
				} catch (IOException e) {
					throw new WebDavException("Could not read resource: " + uri, e);
				}
			} else {
				throw new WebDavException("Resource is a collection");
			}
		} else {
			throw new WebDavException("Resource not found: " + uri);
		}
		return is;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#setContent(java.lang.String, java.io.InputStream, java.lang.String, java.lang.String)
	 */
	@Override
	public void setContent(String uri, InputStream content, String contentType, String encoding)
			throws WebDavException {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		File file = new File(ROOT_DIR, uri);
		if (file.exists()) {
			if (file.isFile()) {
				try {
					BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
					byte[] buffer = new byte[BUFFER_SIZE];
					int read = 0;
					while ((read = content.read(buffer)) > 0) {
						os.write(buffer, 0, read);
					}
					os.close();
				} catch (IOException e) {
					throw new WebDavException("Could not write resource: " + uri, e);
				}
			} else {
				throw new WebDavException("Resource is a collection");
			}
		} else {
			throw new WebDavException("Resource not found: " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#delete(java.lang.String)
	 */
	@Override
	public void delete(String uri) throws WebDavException {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		File file = new File(ROOT_DIR, uri);
		if (file.exists()) {
			boolean deleted = file.delete();
			if (!deleted) {
				throw new WebDavException("Could not delete resource: " + uri);
			}
		} else {
			throw new WebDavException("Resource not found: " + uri);
		}
	}

}
