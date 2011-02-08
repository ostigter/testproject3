package org.ozsoft.mudbot;

public class Monster {

    private String name;

    private String alias;

    public Monster(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Object) {
            Monster monster = (Monster) obj;
            return monster.getName().equals(name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }

}
