package org.ozsoft.scm;

public abstract class VersionedObject implements Comparable<VersionedObject> {
    
    protected final String name;
    
    protected final Directory parent;
    
    protected final Stream stream;
    
    protected final int revision;
    
    public VersionedObject(String name, Directory parent, Stream stream) {
        this.name = name;
        this.parent = parent;
        this.stream = stream;
        this.revision = stream.getRevision();
    }
    
    public String getName() {
        return name;
    }
    
    public Directory getParent() {
        return parent;
    }
    
    public Stream getStream() {
        return stream;
    }
    
    public int getRevision() {
        return revision;
    }
    
    @Override
    public int compareTo(VersionedObject obj) {
        return name.compareTo(obj.getName());
    }
    
}
