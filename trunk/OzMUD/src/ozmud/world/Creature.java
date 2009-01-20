package ozmud.world;

public abstract class Creature {
	
	private final String name;
	private final Gender gender;
	private final String description;
	
	public Creature(String name, Gender gender, String description) {
		this.name = name;
		this.gender = gender;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public String getDescription() {
		return description;
	}
	
	public abstract void processMessage(String message);

}
