package com.jsonobjects;

/**
 * Entity mapped to table QUESTION_TYPE.
 */
public class JQuestionType {

	private Long id;
	/** Not-null value. */
	private String type_name;

	public JQuestionType() {
	}

	public JQuestionType(Long id) {
		this.id = id;
	}

	public JQuestionType(Long id, String type_name) {
		this.id = id;
		this.type_name = type_name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** Not-null value. */
	public String getType_name() {
		return type_name;
	}

	/** Not-null value; ensure this value is available before it is saved to the database. */
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	@Override
	public String toString() {
		return "QuestionType [id=" + id + ", type_name=" + type_name + "]";
	}

}
