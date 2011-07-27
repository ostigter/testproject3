package org.ozsoft.scm;

public abstract class VersionedObject implements Comparable<VersionedObject> {
    
    protected final String name;
    
    protected final Directory parent;
    
    protected final Stream stream;
    
    protected final int revision;
    
    public VersionedObject(String name, Directory parent, Stream stream, int revision) {
        this.name = name;
        this.parent = parent;
        this.stream = stream;
        this.revision = revision;
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
    public boolean equals(Object obj) {
        if (obj instanceof VersionedObject) {
            VersionedObject vo = (VersionedObject) obj;
            return (vo.getName().equals(name) && vo.getStream().equals(stream) && vo.getRevision() == revision);
        } else {
            return false;
        }
    }
    
    @Override
    public int compareTo(VersionedObject obj) {
        return name.compareTo(obj.getName());
    }
    
}
