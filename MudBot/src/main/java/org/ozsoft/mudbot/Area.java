package org.ozsoft.mudbot;

import java.util.HashSet;
import java.util.Set;

public class Area {

    private String name;

    public String[] toPath;

    public String[] homePath;

    public String roomDescription;

    public String[] directions;

    public Set<Monster> monsters;
    
    public Set<String> items;

    private int directionIndex;
    
    private long lastVisited = 0L;

    public Area(String name) {
        this.name = name;
        monsters = new HashSet<Monster>();
        items = new HashSet<String>();
        reset();
    }

    public String getName() {
        return name;
    }

    public void reset() {
        directionIndex = 0;
    }

    public String getNextDirection() {
        String direction = null;

        if (directionIndex < directions.length) {
            direction = directions[directionIndex++];
        } else {
            // No more directions; we should be back at the starting room.
            directionIndex = 0;
        }

        return direction;
    }
    
    public long getLastVisitedSince() {
        return System.currentTimeMillis() - lastVisited;
    }
    
    public void updateLastVisisted() {
        lastVisited = System.currentTimeMillis();
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }
    
    public void addItem(String item) {
        items.add(item);
    }

    public boolean isMonster(String name) {
        for (Monster m : monsters) {
            if (m.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean isItem(String name) {
        for (String item : items) {
            if (name.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public Monster getMonster(String name) {
        for (Monster monster : monsters) {
            if (monster.getName().equals(name)) {
                return monster;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Area) {
            Area area = (Area) obj;
            return area.getName().equals(name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }

}
