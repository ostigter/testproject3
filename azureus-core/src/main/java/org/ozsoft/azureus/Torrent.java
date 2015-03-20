package org.ozsoft.azureus;

import java.util.UUID;

public class Torrent {

    private final String id;

    private final String title;

    private final String magnetUri;

    public Torrent(String title, String magnetUri) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.magnetUri = magnetUri;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMagnetUri() {
        return magnetUri;
    }
}
