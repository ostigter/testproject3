package org.ozsoft.photobook.entities;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Photo extends BaseEntity {

	private static final long serialVersionUID = -3625810657812632685L;
	
	@Lob
	private byte[] contents;

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	} 

}
