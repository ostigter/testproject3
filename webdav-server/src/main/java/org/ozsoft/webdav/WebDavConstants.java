package org.ozsoft.webdav;

/**
 * Generic WebDAV constants.
 * 
 * @author Oscar Stigter
 */
public interface WebDavConstants {

	/** The WebDAV namespace URI. */
	String DAV_NS = "DAV:";
	
    /** The display name property. */
    String DISPLAY_NAME = "displayname";
    
	/** The resource type property. */
	String RESOURCE_TYPE = "resourcetype";
	
    /** Resource type for a non-collection resource. */
    String RESOURCE = "<resource />";
    
    /** Resource type for a collection. */
    String COLLECTION = "<collection />";
    
    /** The content type property. */
    String CONTENT_TYPE = "getcontenttype";
	        
    /** The content length property. */
    String CONTENT_LENGTH = "getcontentlength";
    
    /** The creation date property. */
    String CREATED = "creationdate";
    
    /** The modification date property. */
    String MODIFIED = "getlastmodified";
    
    /** The etag property. */
    String E_TAG = "getetag";
    
}
