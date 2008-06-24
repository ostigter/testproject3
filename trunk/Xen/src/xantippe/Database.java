package xantippe;


import java.util.Set;


/**
 * Lightweight XML database with XQuery, validation and indexing,
 * using a plain file system store.
 * 
 * @author Oscar Stigter
 */
interface Database {


    /**
     * Starts the database.
     * 
     * @throws XmldbException
     *             if the database was already running
     */
    public void start() throws XmldbException;

    
    /**
     * Shuts down the database.
     * 
     * @throws XmldbException
     *             if the database is not running
     */
    public void shutdown() throws XmldbException;

    
    /**
     * Returns whether the database is running.
     * 
     * @return whether the database is running
     */
    public boolean isRunning();

    
    /**
     * Returns the root collection.
     * 
     * @return  the root collection
     * 
     * @throws XmldbException
     *             if the database is not running
     */
    public Collection getRootCollection() throws XmldbException;

    
    public Set<Document> findDocuments(Key[] keys) throws XmldbException;

    
}
