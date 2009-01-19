package ozmud.world;

public abstract class Item {
	
	private String name;
	private String description;
	private int weight;
	private int price;
	
	public Item(String name, String description, int weight, int price) {
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getPrice() {
		return price;
	}

}
