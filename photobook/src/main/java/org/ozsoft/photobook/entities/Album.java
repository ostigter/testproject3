package org.ozsoft.photobook.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Album extends BaseEntity {
	
	@Basic
	private String name;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
