package org.ozsoft.scm;

import java.util.Map;
import java.util.TreeMap;

public class Stream {
    
    private final String name;
    
    private final Stream parent;
    
    private final Map<Integer, Directory> rootDirs;
    
    private int revision = 1;
    
    public Stream(String name, Stream parent) {
        this.name = name;
        this.parent = parent;
        rootDirs = new TreeMap<Integer, Directory>();
        if (parent == null) {
            rootDirs.put(revision, new Directory("", null, this));
        } else {
            rootDirs.put(revision, parent.getRootDir());
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
        return rootDirs.get(revision);
    }
    
    public void incrementRevision() {
        revision++;
        rootDirs.put(revision, new Directory("", null, this));
    }
    
    public void print() {
        System.out.format("%s:\n", this);
        print(getRootDir());
    }
    
    private void print(VersionedObject obj) {
        System.out.format("  %s\n", obj);
        if (obj instanceof Directory) {
            for (VersionedObject child : ((Directory) obj).getChildren()) {
                print(child);
            }
        }
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
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
