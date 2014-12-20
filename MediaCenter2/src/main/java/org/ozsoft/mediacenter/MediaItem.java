package org.ozsoft.mediacenter;

public abstract class MediaItem {

    protected final String name;

    protected final String path;

    protected final MediaFolder parent;

    public MediaItem(String name, String path, MediaFolder parent) {
        this.name = name;
        this.path = path;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public MediaFolder getParent() {
        return parent;
    }
}
