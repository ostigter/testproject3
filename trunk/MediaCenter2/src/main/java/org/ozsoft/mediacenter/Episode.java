package org.ozsoft.mediacenter;

import java.io.File;

public class Episode implements Comparable<Episode> {

	private File file;

	private String showName;

	private int seasonNr;

	private int episodeNr;
	
	private boolean watched = false;

	public Episode(File file, String showName, int seasonNr, int episodeNr) {
		this.file = file;
		this.showName = showName;
		this.seasonNr = seasonNr;
		this.episodeNr = episodeNr;
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
		return String.format("%s - s%02de%02d", showName, seasonNr, episodeNr);
	}
	
	public boolean watched() {
		return watched;
	}
	
	public void markWatched() {
		watched = true;
	}

	public int compareTo(Episode episode) {
		return this.toString().compareTo(episode.toString());
	}

	@Override
	public String toString() {
		return String.format("s%02de%02d", seasonNr, episodeNr);
	}

}
