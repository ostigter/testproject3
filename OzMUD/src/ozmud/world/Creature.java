package ozmud.world;

public abstract class Creature {
	
	private String name;
	private String description;
	
	public Creature(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

}
