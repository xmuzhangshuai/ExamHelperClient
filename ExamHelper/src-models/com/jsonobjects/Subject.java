package com.jsonobjects;

/**
 * Entity mapped to table SUBJECT.
 */
public class Subject {

	private Long id;
	/** Not-null value. */
	private String subject_name;

	public Subject() {
	}

	public Subject(Long id) {
		this.id = id;
	}

	public Subject(Long id, String subject_name) {
		this.id = id;
		this.subject_name = subject_name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Subject [id=" + id + ", subject_name=" + subject_name + "]";
	}

	/** Not-null value. */
	public String getSubject_name() {
		return subject_name;
	}

	/** Not-null value; ensure this value is available before it is saved to the database. */
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

}
