package org.ozsoft.scm;

public abstract class VersionedObject {
    
    private final String name;
    
    private final Stream stream;
    
    private final int revision;
    
    public VersionedObject(String name, Stream stream) {
        this.name = name;
        this.stream = stream;
        this.revision = stream.getRevision();
    }
    
    public String getName() {
        return name;
    }
    
    public Stream getStream() {
        return stream;
    }
    
    public int getRevision() {
        return revision;
    }

}
