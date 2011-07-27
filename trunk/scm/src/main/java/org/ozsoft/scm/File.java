package org.ozsoft.scm;

public class File extends VersionedObject {
    
    public File(String name, Directory parent, Stream stream, int revision) {
        super(name, parent, stream, revision);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(parent);
        sb.append(name);
        sb.append(" (");
        sb.append(stream);
        sb.append(':');
        sb.append(revision);
        sb.append(')');
        return sb.toString();
    }
    
}
