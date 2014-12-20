package org.ozsoft.mediacenter;

public class MediaFile extends MediaItem {

    public MediaFile(String name, String path, MediaFolder parent) {
        super(name, path, parent);
    }

    @Override
    public String toString() {
        return name;
    }
}
