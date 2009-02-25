package ozmud.world;


public class Skill {


    public static final int MAX_LEVEL = 100;

    private final String name;

    private final int cost;


    public Skill(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }


    public String getName() {
        return name;
    }


    public int getCost() {
        return cost;
    }


}
