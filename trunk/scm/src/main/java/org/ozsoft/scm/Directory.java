package org.ozsoft.scm;

import java.util.Set;
import java.util.TreeSet;

public class Directory extends VersionedObject {
    
    private final Set<VersionedObject> children;
    
    public Directory(String name, Directory parent, Stream stream, int revision) {
        super(name, parent, stream, revision);
        children = new TreeSet<VersionedObject>();
    }
    
    public Directory(Directory dir, int revision) {
        this(dir.getName(), dir.getParent(), dir.getStream(), revision);
        for (VersionedObject child : dir.getChildren()) {
            dir.addChild(child);
        }
    }
    
    public Set<VersionedObject> getChildren() {
        return children;
    }
    
    public void addChild(VersionedObject child) {
        children.add(child);
    }
    
    public Directory createDirectory(String name, int revision) {
        Directory dir = new Directory(name, this, stream, revision);
        children.add(dir);
        return dir;
    }
    
    public File createFile(String name, int revision) {
        File file = new File(name, this, stream, revision);
        children.add(file);
        return file;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (parent != null) {
            sb.append(parent);
        }
        sb.append(name);
        sb.append('/');
        sb.append(" (");
        sb.append(stream);
        sb.append(':');
        sb.append(revision);
        sb.append(')');
        return sb.toString();
    }
    
}
