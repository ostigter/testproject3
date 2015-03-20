package org.ozsoft.azureus;

public class TorrentException extends Exception {

    private static final long serialVersionUID = 7870408700371712177L;

    public TorrentException(String message) {
        super(message);
    }

    public TorrentException(String message, Throwable cause) {
        super(message, cause);
    }
}
