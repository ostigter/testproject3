package org.ozsoft.blackbeard.parsers;

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.ShowStatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Parser for TVMaze show search results (JSON).
 * 
 * @author Oscar Stigter
 */
public class ShowListParser {

    public static List<Show> parse(String text) throws ParseException {
        ArrayList<Show> shows = new ArrayList<Show>();

        try {
            JsonArray resultArray = new JsonParser().parse(text).getAsJsonArray();
            for (JsonElement elem : resultArray) {
                JsonObject resultObject = elem.getAsJsonObject();
                JsonObject showObject = resultObject.get("show").getAsJsonObject();
                int id = showObject.get("id").getAsInt();
                String name = showObject.get("name").getAsString();
                String link = showObject.get("url").getAsString();
                String status = showObject.get("status").getAsString();
                Show show = new Show(id, name, link, ShowStatus.parse(status));
                shows.add(show);
            }
        } catch (Exception e) {
            throw new ParseException("Could not parse TVMaze show search results", e);
        }

        return shows;
    }
}
