package org.ozsoft.photobook.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Version
	private int version;
	
	public long getId() {
		return id;
	}
	
	protected void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	protected void setVersion(int version) {
		this.version = version;
	}

}
