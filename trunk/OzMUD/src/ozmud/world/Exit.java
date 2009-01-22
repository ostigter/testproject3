package ozmud.world;


public class Exit {
	

	private String name;
	
	private int roomId;
	
	
	public Exit(String name, int roomId) {
		this.name = name;
		this.roomId = roomId;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public int getRoomId() {
		return roomId;
	}


}
