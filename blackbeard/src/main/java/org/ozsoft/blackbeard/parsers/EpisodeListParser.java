package org.ozsoft.blackbeard.parsers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ozsoft.blackbeard.domain.Episode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Parser for TVMaze episode lists (JSON).
 * 
 * @author Oscar Stigter
 */
public class EpisodeListParser {

    private static int MAX_EPISODE_NR = 100;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static List<Episode> parse(String text) throws ParseException {
        List<Episode> episodes = new ArrayList<Episode>();

        try {
            JsonArray resultArray = new JsonParser().parse(text).getAsJsonArray();
            for (JsonElement elem : resultArray) {
                JsonObject episodeObject = elem.getAsJsonObject();
                String title = episodeObject.get("name").getAsString();
                int seasonNr = episodeObject.get("season").getAsInt();
                JsonElement numberElem = episodeObject.get("number");
                int episodeNr = numberElem.isJsonNull() ? 0 : numberElem.getAsInt();
                Date airDate = null;
                JsonElement airDateElem = episodeObject.get("airdate");
                if (!airDateElem.isJsonNull()) {
                    // Add 1 day to increase chance that episode is aired and available for download.
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(DATE_FORMAT.parse(airDateElem.getAsString()));
                    cal.add(Calendar.DATE, 1);
                    airDate = cal.getTime();
                }
                String link = episodeObject.get("url").getAsString();
                int id = seasonNr * MAX_EPISODE_NR + episodeNr;
                Episode episode = new Episode(id, seasonNr, episodeNr, title, airDate, link);
                episodes.add(episode);
            }
        } catch (Exception e) {
            throw new ParseException("Could not parse TVMaze episode list", e);
        }
        return episodes;
    }
}
