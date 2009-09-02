package org.ozsoft.webdav;

/**
 * Generic WebDAV constants.
 * 
 * @author Oscar Stigter
 */
public interface WebDavConstants {

    /** The XML declaration. */
    String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    
    String CONTENT_TYPE = "text/xml;charset=UTF-8";
    
    /** The WebDAV namespace URI. */
	String DAV_NS = "DAV:";
	
    /** The display name property. */
    String DISPLAYNAME = "displayname";
    
	/** The resource type property. */
	String RESOURCETYPE = "resourcetype";
	
    /** Resource type for a non-collection resource. */
    String RESOURCE = "<resource />";
    
    /** Resource type for a collection. */
    String COLLECTION = "<collection />";
    
    /** The content type property. */
    String GETCONTENTTYPE = "getcontenttype";
	        
    /** The content length property. */
    String GETCONTENTLENGTH = "getcontentlength";
    
    /** The creation date property. */
    String CREATIONDATE = "creationdate";
    
    /** The modification date property. */
    String GETLASTMODIFIED = "getlastmodified";
    
    /** The etag property. */
    String GETETAG = "getetag";
    
}
