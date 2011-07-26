package org.ozsoft.scm;

public class File extends VersionedObject {
    
    private final String content;
    
    public File(String name, Stream stream, String content) {
        super(name, stream);
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
}
