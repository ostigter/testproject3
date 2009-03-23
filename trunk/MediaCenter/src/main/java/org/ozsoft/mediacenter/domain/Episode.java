package org.ozsoft.mediacenter.domain;

import java.io.File;
import java.io.Serializable;

/**
 * An episode of a TV show (series).
 * 
 * @author Oscar Stigter
 */
public class Episode implements Serializable, Comparable<Episode> {

	private static final long serialVersionUID = 1L;
	private File file;
	private String showName;
	private int seasonNr;
	private int episodeNr;
	private String title;
	private boolean isSeen = false;

	public Episode(File file, String showName, int seasonNr, int episodeNr, String title) {
		this.file = file;
		this.showName = showName;
		this.seasonNr = seasonNr;
		this.episodeNr = episodeNr;
		this.title = title;
	}

	public File getFile() {
		return file;
	}

	public String getShowName() {
		return showName;
	}

	public int getSeasonNr() {
		return seasonNr;
	}

	public int getEpisodeNr() {
		return episodeNr;
	}
	
	public String getName() {
		if (title == null || title.length() == 0) {
			return String.format("%s - s%02de%02d",
					showName, seasonNr, episodeNr);
		} else {
			return String.format("%s - s%02de%02d - %s",
					showName, seasonNr, episodeNr, title);
		}
	}
	
	public boolean isSeen() {
		return isSeen;
	}
	
	public void markAsSeen() {
		isSeen = true;
	}

	public int compareTo(Episode episode) {
		return this.toString().compareTo(episode.toString());
	}

	@Override
	public String toString() {
		if (title == null || title.length() == 0) {
			return String.format("s%02de%02d", seasonNr, episodeNr);
		} else {
			return String.format("s%02de%02d - %s", seasonNr, episodeNr, title);
		}
	}

}
