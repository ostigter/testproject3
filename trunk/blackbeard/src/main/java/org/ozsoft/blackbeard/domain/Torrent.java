package org.ozsoft.blackbeard.domain;

/**
 * Respresents a torrent file.
 * 
 * @author Oscar Stigter
 */
public class Torrent implements Comparable<Torrent> {

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

    /** Whether this torrent has been verified. */
    public boolean isVerified = false;

    /** Score. */
    public int score;

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return magnetUri.hashCode();
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

    @Override
    public int compareTo(Torrent other) {
        // Sort torrents by score (descending).
        if (score > other.score) {
            return -1;
        } else if (score < other.score) {
            return 1;
        } else {
            return 0;
        }
    }
}
