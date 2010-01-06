package org.ozsoft.taskman.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Task entity.
 * 
 * @author Oscar Stigter
 */
@Entity
public class Task {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private long id;

	@Basic
	@Column(nullable = false, unique = true)
	private String summary;

	@Basic
	@Column(nullable = false)
	private Status status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Task) {
			return (((Task) obj).id == id);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return summary;
	}

}
