package org.ozsoft.xmldb;

public class NodeRecord {
    
    public final static int DOCUMENT = 0x00; 
    
    public final static int ELEMENT = 0x01; 
    
    public final static int ATTRIBUTE = 0x02; 
    
    public final static int TEXT = 0x03; 
    
    private final long id;
    
    private final int type;
    
    private long offset;
    
    private long length;
    
    public NodeRecord(long id, int type) {
        this(id, type, -1L, 0L);
    }
    
    public NodeRecord(long id, int type, long offset, long length) {
        this.id = id;
        this.type = type;
        setOffset(offset);
        setLength(length);
    }
    
    public long getId() {
        return id;
    }
    
    public int getType() {
        return type;
    }
    
    public long getOffset() {
        return offset;
    }
    
    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLength() {
        return length;
    }
    
    public void setLength(long length) {
        this.length = length;
    }

}
