package org.ozsoft.azureus;

public enum TorrentStatus {

    STOPPED("Stopped"),

    QUEUED("Queued"),

    DOWNLOADING("Downloading"),

    DOWNLOADED("Downloaded"),

    FAILED("Failed"),

    ;

    private final String name;

    TorrentStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
