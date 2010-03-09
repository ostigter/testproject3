package org.ozsoft.bpm;

public class Transition {
	
	private final String name;
	
	private final String destination;
	
	public Transition(String name, String destination) {
		this.name = name;
		this.destination = destination;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDestination() {
		return destination;
	}

}
