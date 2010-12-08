package org.ozsoft.xmlindexer;

public class Index {

    private final String name;

    private final String path;

    // TODO: Use Object as index value instead of String.
    private String value;

    public Index(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
