package org.ozsoft.mediacenter.server;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.ozsoft.mediacenter.domain.Episode;
import org.ozsoft.mediacenter.domain.Show;

/**
 * The media library.
 * 
 * @author Oscar Stigter
 */
public class Library {

    /** The logger. */
    private static final Logger LOG = Logger.getLogger(Library.class);
    
	/** Pattern 1, e.g. "greys anatomy s03e08 new kid in town". */
	private static final Pattern pattern1 =
			Pattern.compile("s(\\d+)e(\\d+)");

	/** Pattern 2, e.g. "greys anatomy 13x08 new kid in town". */
	private static final Pattern pattern2 =
			Pattern.compile("(\\d+)x(\\d+)");
	
	/** Pattern 3, e.g. "greys anatomy 308 new kid in town". */
	private static final Pattern pattern3 =
			Pattern.compile("\\w\\d+\\w");
	
	/** Empty array of Show objects. */
	private static final Show[] EMPTY_SHOW_ARRAY = new Show[0];
	
	private final Configuration config; 

	/** TV shows mapped by their name. */
	private final Map<String, Show> shows;
	
	public Library(Configuration config) {
		this.config = config;
		shows = new TreeMap<String, Show>();
		LOG.debug("Created");
		refresh();
	}
	
	public void refresh() {
		LOG.info("Refreshing");
		long time = System.currentTimeMillis();
        for (String path : config.getShowRoots()) {
            scanDirectory(path);
        }
		long duration = System.currentTimeMillis() - time;
		LOG.info(String.format("Refresh finished in %d ms; found %d TV shows and %d epsiodes",
				duration, getShowCount(), getEpisodeCount()));
	}
	
	public int getShowCount() {
		return shows.size();
	}
	
	public int getEpisodeCount() {
		int episodeCount = 0;
		for (Show show : shows.values()) {
			episodeCount += show.getEpisodeCount();
		}
		return episodeCount;
	}
	
	public Show[] getShows() {
		return shows.values().toArray(EMPTY_SHOW_ARRAY);
	}
	
	private void scanDirectory(String path) {
		LOG.info(String.format(
				"Scanning directory '%s'", path));
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			scanDirectory(dir);
		}
	}

	private void scanDirectory(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isFile() && isVideoFile(file)) {
				scanFile(file);
			}
		}
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				scanDirectory(file);
			}
		}
	}
	
	private boolean isVideoFile(File file) {
		return file.getName().endsWith(".avi"); 
	}

	private void scanFile(File file) {
		String fileName = getCleanFileName(file);
		
		String showName = null;
		int seasonNr = -1;
		int episodeNr = -1;
		String title = "";
		
		// Try pattern 1, e.g. "greys anatomy s03e08 some title".
		Matcher m = pattern1.matcher(fileName);
		if (m.find()) {
			showName = fileName.substring(0, m.start()).trim();
			try {
				seasonNr = Integer.parseInt(m.group(1));
				episodeNr = Integer.parseInt(m.group(2));
			} catch (NumberFormatException e) {
				// Impossible (guaranteed by regex).
			}
			title = fileName.substring(m.end()).trim();
		} else {
			// Try pattern 2, e.g. "greys anatomy 3x08 some title".
			m = pattern2.matcher(fileName);
			if (m.find()) {
				showName = fileName.substring(0, m.start()).trim();
				try {
					seasonNr = Integer.parseInt(m.group(1));
					episodeNr = Integer.parseInt(m.group(2));
					title = fileName.substring(m.end()).trim();
				} catch (NumberFormatException e) {
					// Impossible (guaranteed by regex).
				}
			} else {
				// Try pattern 3, e.g. "greys anatomy 308 some title"
				m = pattern3.matcher(fileName);
				if (m.find()) {
					showName = fileName.substring(0, m.start()).trim();
					String s = m.group();
					if (s.length() == 3) {
						try {
							seasonNr = Integer.parseInt(s.substring(0, 1));
							episodeNr = Integer.parseInt(s.substring(1));
						} catch (NumberFormatException e) {
							// Impossible (guaranteed by regex).
						}
					} else if (s.length() == 4) {
						try {
							seasonNr = Integer.parseInt(s.substring(0, 2));
							episodeNr = Integer.parseInt(s.substring(2));
						} catch (NumberFormatException e) {
							// Impossible (guaranteed by regex).
						}
					}
					title = fileName.substring(m.end()).trim();
				}
			}
		}
		
		if (showName != null && seasonNr != -1 && episodeNr != -1) {
			showName = getCleanName(showName);
			Episode episode = new Episode(
					file, showName, seasonNr, episodeNr, title);
			Show show = shows.get(showName);
			if (show == null) {
				show = new Show(showName);
				shows.put(showName, show);
			}
			show.addEpisode(episode);
		} else {
			LOG.warn(String.format("Could not parse file name: '%s'", file.getName()));
		}
	}
	
	private static String getCleanFileName(File file) {
		String name = file.getName().toLowerCase();
		name = name.replaceAll("\\.", " ");
		name = name.replaceAll("_", " ");
		name = name.replaceAll(" - ", " ");
		name = name.replaceAll(" avi$", "");
		name = name.replaceAll(" hdtv ", " ");
		name = name.replaceAll(" hdtv-.*\\w", " ");
		name = name.replaceAll(" dvdrip ", " ");
		name = name.replaceAll(" repack ", " ");
		name = name.replaceAll(" proper ", " ");
		name = name.replaceAll(" ac3 ", " ");
		name = name.replaceAll(" ws ", " ");
		name = name.replaceAll("ac3", "");
		name = name.replaceAll("xvid-.*", " ");
		return name;
	}
	
	private static String getCleanName(String name) {
		if (name.startsWith("the ")) {
			name = name.substring(4) + ", the";
		} else if (name.startsWith("a ")) {
			name = name.substring(2) + ", a";
		}
		return name;
	}
	
}
