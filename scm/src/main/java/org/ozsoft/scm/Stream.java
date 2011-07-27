package org.ozsoft.scm;

import java.util.Map;
import java.util.TreeMap;

public class Stream {
    
    private final String name;
    
    private final Stream parent;
    
    private final Map<Integer, Directory> rootDirs;
    
    private int latestRevision = 1;
    
    public Stream(String name, Stream parent) {
        this.name = name;
        this.parent = parent;
        rootDirs = new TreeMap<Integer, Directory>();
        if (parent == null) {
            rootDirs.put(latestRevision, new Directory("", null, this, latestRevision));
        } else {
            int parentRevision = parent.getLatestRevision();
            rootDirs.put(latestRevision, parent.getRootDir(parentRevision));
        }
    }
    
    public String getName() {
        return name;
    }
    
    public Stream getParent() {
        return parent;
    }
    
    public int getLatestRevision() {
        return latestRevision;
    }
    
    public Directory getRootDir(int revision) {
        if (revision > latestRevision) {
            throw new IllegalArgumentException("Invalid revision: " + revision);
        }
        return rootDirs.get(revision);
    }
    
    public void incrementRevision() {
        Directory rootDir = getRootDir(latestRevision);
        latestRevision++;
        rootDirs.put(latestRevision, new Directory(rootDir, latestRevision));
    }
    
    public void print(int revision) {
        System.out.format("%s:%s:\n", name, revision);
        print(getRootDir(revision));
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
        return name;
    }

}
