package org.ozsoft.blackbeard.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.ozsoft.blackbeard.domain.Show;

public class Configuration implements Serializable {

    private static final long serialVersionUID = 6200553605465987303L;

    private static final String DATA_FILE_NAME = ".blackbeard";

    private final File dataFile;

    private Map<Integer, Show> shows;

    public Configuration() {
        File homeDir = new File(System.getProperty("user.home"));
        dataFile = new File(homeDir, DATA_FILE_NAME);
    }

    @SuppressWarnings("unchecked")
    public void load() {
        if (dataFile.isFile()) {
            try (ObjectInputStream dis = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)))) {
                shows = (Map<Integer, Show>) dis.readObject();
            } catch (IOException e) {
                System.err.println("ERROR: Could not load configuration from file");
                e.printStackTrace(System.err);
            } catch (ClassNotFoundException e) {
                // Can never happen.
            }
        } else {
            // Create new configuration.
            shows = new HashMap<Integer, Show>();
        }
    }

    public void save() {
        try (ObjectOutputStream dos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)))) {
            dos.writeObject(shows);
        } catch (IOException e) {
            System.err.println("ERROR: Could not save configuration to file");
            e.printStackTrace(System.err);
        }
    }

    public Show[] getShows() {
        return new TreeSet<Show>(shows.values()).toArray(new Show[0]);
    }

    public Show getShow(int id) {
        return shows.get(id);
    }

    public void addShow(Show show) {
        shows.put(show.getId(), show);
    }

    public void deleteShow(int id) {
        shows.remove(id);
    }
}
