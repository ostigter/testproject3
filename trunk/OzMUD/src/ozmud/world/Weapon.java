package ozmud.world;

public class Weapon extends Item {

	private final int damage;

	public Weapon(String name, String description, int damage, int weight,
			int price) {
		super(name, description, weight, price);
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}

}
