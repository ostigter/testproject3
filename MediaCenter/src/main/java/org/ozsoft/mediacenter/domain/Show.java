package org.ozsoft.mediacenter.domain;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * A TV show (series).
 * 
 * @author Oscar Stigter
 */
public class Show implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Episode[] EMPTY_ARRAY = new Episode[0];
	private final String name;
	private final Set<Episode> episodes;

	public Show(String name) {
		this.name = name;
		episodes = new TreeSet<Episode>();
	}

	public String getName() {
		return name;
	}

	public void addEpisode(Episode episode) {
		episodes.add(episode);
	}
	
	public void removeEpisode(Episode episode) {
		episodes.remove(episode);
	}

	public Episode[] getEpisodes() {
		return episodes.toArray(EMPTY_ARRAY);
	}
	
	public int getEpisodeCount() {
		return episodes.size();
	}

	@Override
	public String toString() {
		return name;
	}

}
