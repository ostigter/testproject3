package org.ozsoft.scm;

import java.util.Set;
import java.util.TreeSet;

public class Directory extends VersionedObject {
    
    private final Set<VersionedObject> children;
    
    public Directory(String name, Directory parent, Stream stream) {
        super(name, parent, stream);
        children = new TreeSet<VersionedObject>();
    }
    
    public Set<VersionedObject> getChildren() {
        return children;
    }
    
    public Directory createDirectory(String name) {
        Directory dir = new Directory(name, this, stream);
        children.add(dir);
        return dir;
    }
    
    public File createFile(String name, String content) {
        File file = new File(name, this, stream, content);
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
        return sb.toString();
    }
    
}
