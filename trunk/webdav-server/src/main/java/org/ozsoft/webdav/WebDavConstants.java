package org.ozsoft.webdav;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Generic WebDAV constants.
 * 
 * @author Oscar Stigter
 */
public interface WebDavConstants {

    /** Standard XML declaration. */
    String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    
    /** XML content type (MIME type). */ 
    String XML_CONTENT_TYPE = "text/xml;charset=UTF-8";
    
    /** Official WebDAV namespace URI. */
	String DAV_NS = "DAV:";
	
    /** Display name property. */
    String DISPLAYNAME = "displayname";
    
	/** Resource type property. */
	String RESOURCETYPE = "resourcetype";
	
    /** Resource type property value for a non-collection resource. */
    String RESOURCE = "";
    
    /** Resource type property value for a collection. */
    String COLLECTION = "<collection />";
    
    /** Content type property. */
    String GETCONTENTTYPE = "getcontenttype";
	        
    /** Content length property. */
    String GETCONTENTLENGTH = "getcontentlength";
    
    /** Creation date property. */
    String CREATIONDATE = "creationdate";
    
    /** Modification date property. */
    String GETLASTMODIFIED = "getlastmodified";
    
    /** Etag property. */
    String GETETAG = "getetag";
    
    /** Creation date format. */
    DateFormat CREATION_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    /** Last modification date format. */
    DateFormat LASTMODIFIED_DATE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

}
