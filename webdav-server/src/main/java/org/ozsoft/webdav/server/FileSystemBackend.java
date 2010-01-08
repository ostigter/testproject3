package org.ozsoft.webdav.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;
import org.ozsoft.webdav.WebDavException;

/**
 * File system WebDAV backend.
 * 
 * Serves files (resources) and directories (collections) stored in a specific
 * root directory.
 * 
 * @author Oscar Stigter
 */
public class FileSystemBackend implements WebDavBackend {
	
	/** Buffer size for copying streams. */
	private static final int BUFFER_SIZE = 8192;
	
	/** The filename filter. */
	private static final FilenameFilter FILENAME_FILTER = new WebDavFilenameFilter();  
    
	/** The root directory. */
	private final File ROOT_DIR;
	
	/** The supported MIME types. */
	private final Map<String, String> mimeTypes;
	
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
		
		// Set root directory.
		ROOT_DIR = new File(path);
		if (!ROOT_DIR.isDirectory()) {
			throw new IllegalArgumentException("Root directory not found: " + path);
		}
		
		// Define supported MIME types.
		mimeTypes = new HashMap<String, String>();
		mimeTypes.put("txt", "text/plain");
        mimeTypes.put("xml", "text/xml");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("jpg", "image/jpg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("pdf", "application/pdf");
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
			return dir.list(FILENAME_FILTER);
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
		if (dir.exists()) {
			String message = null;
			if (dir.isDirectory()) {
				message = String.format("Collection '%s' already exists", uri);
			} else {
				message = String.format("Non-collection resource '%s' exists", uri);
			}
			throwWebDavException(HttpServletResponse.SC_CONFLICT, message);
		}
		
		boolean created = dir.mkdirs();
		if (!created) {
			throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					String.format("Error creating collection '%s'", uri));
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
		if (file.exists()) {
			throwWebDavException(HttpServletResponse.SC_CONFLICT,
					String.format("Resource '%s' already exists", uri));
		}

		try {
			boolean created = file.createNewFile();
			if (!created) {
				throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						String.format("Error creating resource '%s'", uri));
			}
		} catch (IOException e) {
			throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					String.format("Error creating resource '%s'", uri), e);
		}
	}

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getContentType(java.lang.String)
     */
    @Override
    public String getContentType(String uri) {
        String extention = "";
        if (uri.length() > 0) {
            int pos = uri.lastIndexOf('.');
            if (pos != 0) {
                extention = uri.substring(pos).toLowerCase();
            }
        }
        String type = mimeTypes.get(extention);
        if (type != null) {
            return type;
        } else {
            // Unknown type; handle as binary file.
            return "application/octet-stream";
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
		// Since Java doesn't support the creation date on files, just return the modification date.
		return getModified(uri);
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getModified(java.lang.String)
	 */
	@Override
	public Date getModified(String uri) throws WebDavException {
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		
		File file = new File(ROOT_DIR, uri);
		if (!file.exists()) {
			throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
					String.format("Resource '%s' not found", uri));
		}

		return new Date(file.lastModified());
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

		File file = new File(ROOT_DIR, uri);
		if (!file.exists()) {
			throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
					String.format("Resource '%s' not found", uri));
		}
		
		if (file.isDirectory()) {
			throw new IllegalStateException("getContent() called on collection");
		}

		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
		} catch (IOException e) {
			throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					String.format("Error reading resource '%s'", uri), e);
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
		if (!file.exists()) {
			throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
					String.format("Resource '%s' not found", uri));
		}

		if (file.isDirectory()) {
			throwWebDavException(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
					String.format("Cannot set the content of a collection", uri));
		}

		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = content.read(buffer)) > 0) {
				os.write(buffer, 0, read);
			}
			os.close();
		} catch (IOException e) {
			throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					String.format("Error writing resource '%s'", uri), e);
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
		if (!file.exists()) {
			throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
					String.format("Resource '%s' not found", uri));
		}

		boolean deleted = file.delete();
		if (!deleted) {
			throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					String.format("Error deleting resource '%s'", uri));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#copy(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void copy(String uri, String destination, boolean overwrite) throws WebDavException {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("Null or empty uri");
        }

        if (destination == null || destination.length() == 0) {
            throw new IllegalArgumentException("Null or empty destination");
        }
        
        File sourceFile = new File(ROOT_DIR, uri);
		if (!sourceFile.exists()) {
			throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
					String.format("Resource '%s' not found", uri));
		}
        
        File destinationFile = new File(ROOT_DIR, destination);
        if (destinationFile.exists() && !overwrite) {
            throwWebDavException(HttpServletResponse.SC_PRECONDITION_FAILED,
            		String.format("Destination resource '%s' exists (no overwrite)", destination));
        }
        
        //TODO: Copy collections.
        
    	try {
    		//TODO: Improve I/O error handling.
            InputStream is = new BufferedInputStream(new FileInputStream(sourceFile));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(destinationFile));
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
            	os.write(buffer, 0, length);
            }
            os.close();
            is.close();
    	} catch (IOException e) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		String.format("Error copying resource '%s' to '%s'", uri, destination), e);
    	}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#move(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void move(String uri, String destination, boolean overwrite) throws WebDavException {
        if (uri == null || uri.length() == 0) {
            throw new IllegalArgumentException("Null or empty uri");
        }
        if (destination == null || destination.length() == 0) {
            throw new IllegalArgumentException("Null or empty destination");
        }
        
        File sourceFile = new File(ROOT_DIR, uri);
		if (!sourceFile.exists()) {
			throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
					String.format("Resource '%s' not found", uri));
		}

		//TODO: Move collections.
		
		File destinationFile = new File(ROOT_DIR, destination);
        if (!destinationFile.exists()) {
        	sourceFile.renameTo(destinationFile);
        } else {
        	if (!overwrite) {
                throwWebDavException(HttpServletResponse.SC_PRECONDITION_FAILED,
                		String.format("Destination resource '%s' exists (no overwrite)", destination));
        	}
    		boolean deleted = destinationFile.delete();
    		if (!deleted) {
                throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		String.format("Error deleting existing destination resource '%s'", destination));
    		}
        }
        
        if (!destinationFile.exists()) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            		String.format("Error moving resource '%s' to '%s'", uri, destination));
        }
	}
	
	/**
	 * Throws a WebDAV exception.
	 * 
	 * @param statusCode
	 *            The HTTP status code.
	 * @param message
	 *            The message.
	 * 
	 * @throws WebDavException
	 *             The exception.
	 */
	private void throwWebDavException(int statusCode, String message) throws WebDavException {
		throwWebDavException(statusCode, message, null);
	}

	/**
	 * Throws a WebDAV exception with a nested exception as the cause.
	 * 
	 * @param statusCode
	 *            The HTTP status code.
	 * @param message
	 *            The message.
	 * @param t
	 *            The cause (nested exception).
	 * 
	 * @throws WebDavException
	 *             The exception.
	 */
	private void throwWebDavException(int statusCode, String message, Throwable t) throws WebDavException {
		if (Log.isDebugEnabled()) {
			Log.debug(message);
		}
		throw new WebDavException(statusCode, message, t);
	}

    /**
     * Filename filter that filters out certain files and directories that
     * should not be listed.
     * 
     * @author Oscar Stigter
     */
	private static class WebDavFilenameFilter implements FilenameFilter {

	    /*
	     * (non-Javadoc)
	     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	     */
	    @Override
        public boolean accept(File dir, String name) {
            return !name.equals(".svn");
        }
	    
	} // WebDavFileFilter class

}
