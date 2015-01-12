package org.ozsoft.blackbeard.domain;

/**
 * Torrent file.
 * 
 * @author Oscar Stigter
 */
public class Torrent {

    /** Title (filename). */
    public String title;

    /** File size. */
    public long size;

    /** Number of seeders. */
    public int seederCount;

    /** Number of leechers. */
    public int leecherCount;

    /** Magnet link URI. */
    public String magnetUri;

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null) {
            if (other instanceof Torrent) {
                return ((Torrent) other).magnetUri.equals(magnetUri);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
