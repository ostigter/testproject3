package org.ozsoft.scm;

public class File extends VersionedObject {
    
    private final String content;
    
    public File(String name, Directory parent, Stream stream, String content) {
        super(name, parent, stream);
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
    @Override
    public String toString() {
        return String.format("%s%s", parent, name);
    }
    
}
