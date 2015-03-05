package org.ozsoft.blackbeard.domain;

import java.io.Serializable;

/**
 * Respresents a torrent file.
 * 
 * @author Oscar Stigter
 */
public class Torrent implements Serializable, Comparable<Torrent> {

    private static final long serialVersionUID = 4899913829337667040L;

    private static final long MB = 1024 * 1024;

    /** Title (filename). */
    private final String title;

    /** File size. */
    private final long size;

    /** Number of seeders. */
    private final int seederCount;

    /** Number of leechers. */
    private final int leecherCount;

    /** Magnet link URI. */
    private final String magnetUri;

    /** Whether this torrent has been verified. */
    private final boolean isVerified;

    /** Score. */
    private int score;

    public Torrent(String title, long size, int seederCount, int leecherCount, String magnetUri, boolean isVerified) {
        this.title = title;
        this.size = size;
        this.seederCount = seederCount;
        this.leecherCount = leecherCount;
        this.magnetUri = magnetUri;
        this.isVerified = isVerified;
        calculateScore();
    }

    public String getTitle() {
        return title;
    }

    public long getSize() {
        return size / MB;
    }

    public int getSeederCount() {
        return seederCount;
    }

    public int getLeecherCount() {
        return leecherCount;
    }

    public String getMagnetUri() {
        return magnetUri;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public int getScore() {
        return score;
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
        return other.score - score;
    }

    @Override
    public String toString() {
        return title;
    }

    private void calculateScore() {
        int score = 0;

        if (title.contains("[eztv]") || title.contains("[ettv]")) {
            score += 2;
        }

        if (title.contains("DIMENSION") || title.contains("LOL") || title.contains("YIFY")) {
            score += 2;
        }

        if (seederCount > 100) {
            score += 2;
        } else if (seederCount > 10) {
            score += 1;
        } else if (seederCount == 0) {
            score -= 5;
        }

        if (isVerified) {
            score += 1;
        }

        this.score = score;
    }
}
