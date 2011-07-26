package org.ozsoft.scm;

public class Stream {
    
    private final String name;
    
    private final Stream parent;
    
    private int revision = 1;
    
    private Directory rootDir;
    
    public Stream(String name, Stream parent) {
        this.name = name;
        this.parent = parent;
        if (parent == null) {
            rootDir = new Directory("/", this);
        } else {
            rootDir = parent.getRootDir();
        }
    }
    
    public String getName() {
        return name;
    }
    
    public Stream getParent() {
        return parent;
    }
    
    public int getRevision() {
        return revision;
    }
    
    public Directory getRootDir() {
        return rootDir;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stream) {
            return ((Stream) obj).getName().equals(name);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s:%d", name, revision);
    }

}
