package org.ozsoft.scm;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Server {
    
    private final Map<String, Stream> streams;
    
    public Server() {
        streams = new TreeMap<String, Stream>();
    }
    
    public Collection<Stream> getStreams() {
        return streams.values();
    }
    
    public Stream getStream(String name) {
        return streams.get(name);
    }

    public void addStream(Stream stream) {
        streams.put(stream.getName(), stream);
    }
    
}
