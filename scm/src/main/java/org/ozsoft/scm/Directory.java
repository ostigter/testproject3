package org.ozsoft.scm;

import java.util.ArrayList;
import java.util.List;

public class Directory extends VersionedObject {
    
    private final List<VersionedObject> children;
    
    public Directory(String name, Stream stream) {
        super(name, stream);
        children = new ArrayList<VersionedObject>();
    }
    
    public List<VersionedObject> getChildren() {
        return children;
    }
    
    public void addChild(VersionedObject child) {
        children.add(child);
    }

}
