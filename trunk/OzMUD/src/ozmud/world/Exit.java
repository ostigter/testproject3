package ozmud.world;


/**
 * An exit in a room, leading to another room.
 * 
 * @author Oscar Stigter
 */
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


    @Override
    public String toString() {
        return name;
    }
    
    
}
